package com.kkb.dts.consumer;

import com.alibaba.dts.formats.avro.Record;
import com.kkb.dts.consumer.listener.ConsumerListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class DatabaseKafkaListener implements BeanClassLoaderAware , InitializingBean {

    private  ExecutorService consumerExecutor;
    private ClassLoader classLoader;

    @Autowired
    private ObjectProvider<ConsumerListener<Record>> listeners;

    @Override
    public void afterPropertiesSet() throws Exception {
        //使用两个线程保证高效的并行处理以及使用队列保证有序，当队列缓冲已经无法满足kafka消费的速度时，就会启动 BlockingWaitPolicy
        //keepAliveTime 是决定maxSize thread 如何工作的决定性因素
        consumerExecutor = new ThreadPoolExecutor(2, 2,
                0, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<>(20000), new DtsConsumerThreadFactory(classLoader), new BlockingWaitPolicy());
    }


    @KafkaListener(id = "consume-data", topics = "${com.kkb.kafka.consumer.topic-name}")
    public void handleDatabaseMeta(Message<byte[]> message, @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment acknowledgment) throws IOException {
        byte[] content = message.getPayload();
        DatumReader<Record> reader = new SpecificDatumReader<>(Record.class);
        DecoderFactory decoderFactory = DecoderFactory.get();
        Decoder decoder = decoderFactory.binaryDecoder(content, null);
        Record record = reader.read(null, decoder);

        //默认只找到第一个符合的listener进行处理
        Optional<ConsumerListener<Record>> listenerOptional = listeners.stream().parallel()
                .filter(listener -> listener.support(record))
                .findFirst();

        if (!listenerOptional.isPresent()) {
            acknowledgment.acknowledge();
            return;
        }

        log.info("Receive data from dts, tableName: {}, operation:{} ", record.getObjectName(), record.getOperation());
        listenerOptional.ifPresent(listener-> listener.listen(record, acknowledgment));

    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }



    /**
     * 自定义生成的 ThreadFactory
     */
    public static class DtsConsumerThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private final ClassLoader classLoader;

        DtsConsumerThreadFactory(ClassLoader classLoader) {
            SecurityManager s = System.getSecurityManager();
            this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "dts-consumer-";
            this.classLoader = classLoader;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);

            if (classLoader != null)
                t.setContextClassLoader(classLoader);

            return t;
        }
    }

    /**
     * 当队列已经无法放入当前任务时，则阻塞等待
     */
    public static class BlockingWaitPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
            //如果由于其他原因导致当前线程被中断而丢失数据，则直接忽略当前中断
            boolean interrupt = false;
            for (; ; ) {
                try {
                    //对应于 put操作，实现思路都是一样的，但是put（有可能会导致）数据放入之后出错
                    if (executor.getQueue().offer(task, 30, TimeUnit.SECONDS))
                        break;
                } catch (InterruptedException e) {
                    interrupt = true;
                }
            }
            if (interrupt)
                Thread.currentThread().interrupt();
        }
    }
}

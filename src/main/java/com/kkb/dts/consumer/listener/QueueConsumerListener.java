package com.kkb.dts.consumer.listener;

import com.kkb.dts.common.MetricLinkedBlockingQueue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * 装饰模式用于增强 ConsumerListener
 *
 * @param <T>
 * @author zhangyang
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QueueConsumerListener<T> extends Thread implements
        ConsumerListener<T>, BeanFactoryAware, InitializingBean, BeanClassLoaderAware, SmartInitializingSingleton {

    private static final int DEFAULT_CAPACITY = 20000;
    private static final double DEFAULT_THRESHOLD = 0.5;
    final MetricLinkedBlockingQueue<RecordData<T>> blockingQueue;
    final ConsumerListener<T> targetListener;


    AutowireCapableBeanFactory beanFactory;

    public QueueConsumerListener(String queueName, ConsumerListener<T> listener) {
        this(queueName, DEFAULT_CAPACITY, listener);
    }

    public QueueConsumerListener(String queueName, Integer capacity, ConsumerListener<T> listener) {
        this(queueName, listener, capacity, DEFAULT_THRESHOLD);
    }

    public QueueConsumerListener(String queueName, ConsumerListener<T> listener, Integer capacity, Double threshold) {
        Assert.notNull(listener, "Listener must not be null");
        Assert.hasText(queueName, "QueueName must not be null");
        this.targetListener = listener;
        capacity = capacity == null ? DEFAULT_CAPACITY : capacity;
        threshold = threshold == null ? DEFAULT_THRESHOLD : threshold;
        this.blockingQueue = new MetricLinkedBlockingQueue<>(queueName, capacity, threshold);
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        Assert.isInstanceOf(AutowireCapableBeanFactory.class, beanFactory, "The beanFactory must be instanceof AutowireCapableBeanFactory");
        this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
    }

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        setContextClassLoader(classLoader);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setDaemon(false);
        setPriority(Thread.NORM_PRIORITY);
        beanFactory.autowireBean(targetListener);
        beanFactory.initializeBean(targetListener, targetListener.getClass().getName());
    }

    @Override
    public void afterSingletonsInstantiated() {
        //启动当前线程
        start();

        //开启队列监控
//        blockingQueue.startMonitor();
    }


    @Override
    public void listen(T record, Acknowledgment acknowledgment) {
        RecordData<T> recordData = RecordData.create(record, acknowledgment);
        putDataUninterrupt(recordData);
    }


    /**
     * 将数据放入对应的队列中，且忽略中断
     *
     * @param recordData 从dts中获取的对应的数据
     */
    private void putDataUninterrupt(RecordData<T> recordData) {
        boolean interrupt = false;
        for (; ; ) {
            try {
                //如果无法放入当前队列，则循环放入，使用offer 是为了保证当前数据一定被放入到队列中
                if (blockingQueue.offer(recordData, 1, TimeUnit.MINUTES))
                    break;
            } catch (InterruptedException e) {
                interrupt = Thread.interrupted();
            }
        }
        if (interrupt)
            Thread.currentThread().interrupt();
    }


    @Override
    public void run() {
        for (; ; ) {
            try {
                RecordData<T> recordData = blockingQueue.take();
                log.info("Received data from queue: {}", blockingQueue.getQueueName());

                //由具体的listener 进行处理
                targetListener.listen(recordData.getRecord(), recordData.acknowledgment);
            } catch (Exception e) {
                //todo 如果捕捉到异常则忽略，默认继续执行
                log.error("Sync dts data to database, catch exception: ", e);
            }
        }
    }


    @Override
    public boolean support(T record) {
        if (record == null)
            return false;

        return targetListener.support(record);
    }


    @Getter
    @ToString
    @AllArgsConstructor(staticName = "create")
    static class RecordData<T> {
        private final T record;
        private final Acknowledgment acknowledgment;
    }


}

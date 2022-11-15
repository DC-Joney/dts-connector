package com.kkb.dts.consumer.listener;

import org.springframework.kafka.support.Acknowledgment;

/**
 * 用于从 DTS Kafka 进行数据消费
 * @author zhangyang
 */
public interface ConsumerListener<T> {

    boolean support(T record);

    void listen(T record, Acknowledgment acknowledgment);

    static <S> QueueBuilder<S> builder(ConsumerListener<S> listener){
        return new QueueListenerBuilder<>(listener);
    }


    /**
     *  通过 QueueListener 对当前的ConsumerListener 进行包装
     */
    interface QueueBuilder<S> {
        /**
         * 设置queue 的名称
         */
        QueueBuilder<S> name(String queueName);

        /**
         * 设置Queue 的容量大小
         */
        QueueBuilder<S> capacity(Integer capacity);

        /**
         * 设置监控队列增长率的阈值因子
         */
        QueueBuilder<S> threshold(Double threshold);

        /**
         * 构建对应的包装器
         */
        QueueConsumerListener<S> build();
    }

}

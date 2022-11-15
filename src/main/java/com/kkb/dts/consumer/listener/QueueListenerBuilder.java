package com.kkb.dts.consumer.listener;

import org.springframework.util.Assert;

/**
 * @author zhangyang
 */
class QueueListenerBuilder<S> implements ConsumerListener.QueueBuilder<S> {

    private Integer capacity;
    private Double threshold;
    private String queueName;
    private ConsumerListener<S> listener;

    public QueueListenerBuilder(ConsumerListener<S> consumerListener) {
        Assert.notNull(consumerListener,"The targetListener must not be null");
        this.listener = consumerListener;
    }

    @Override
    public ConsumerListener.QueueBuilder<S> name(String queueName) {
        this.queueName = queueName;
        return this;
    }

    @Override
    public ConsumerListener.QueueBuilder<S> capacity(Integer capacity) {
        Assert.isTrue(capacity > 0,"QueueListener for capacity must be > 0");
        this.capacity = capacity;
        return this;
    }

    @Override
    public ConsumerListener.QueueBuilder<S> threshold(Double threshold) {
        Assert.isTrue(threshold > 0,"QueueListener for threshold must be > 0");
        this.threshold = threshold;
        return this;
    }

    @Override
    public QueueConsumerListener<S> build() {
        return new QueueConsumerListener<>(queueName, listener, capacity, threshold);
    }
}

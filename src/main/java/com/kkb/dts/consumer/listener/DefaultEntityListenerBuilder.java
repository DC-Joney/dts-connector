package com.kkb.dts.consumer.listener;

import com.kkb.dts.consumer.handler.MessageHandler;
import com.kkb.dts.entity.IdEntity;

import java.util.function.BiPredicate;

/**
 * 构建对应的DefaultEntityListener
 */
class DefaultEntityListenerBuilder<S, T extends IdEntity> implements EntityConsumerListener.Builder<S, T> {

    private Class<T> entityClass;
    private EntityConverter<S, T> converter;
    private BiPredicate<S, EntityInfo<?>> predicate;
    private MessageHandler<T> messageHandler;
    private String tableName;
    private String topicName;

    public DefaultEntityListenerBuilder(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public EntityConsumerListener.Builder<S, T> entityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    @Override
    public EntityConsumerListener.Builder<S, T> messageHandler(MessageHandler<T> handler) {
        this.messageHandler = handler;
        return this;
    }

    @Override
    public EntityConsumerListener.Builder<S, T> converter(EntityConverter<S, T> converter) {
        this.converter = converter;
        return this;
    }

    @Override
    public EntityConsumerListener.Builder<S, T> validate(BiPredicate<S, EntityInfo<?>> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public EntityConsumerListener.Builder<S, T> topic(String topicName) {
        this.topicName = topicName;
        return this;
    }

    @Override
    public EntityConsumerListener.Builder<S, T> table(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public EntityConsumerListener<S, T> build() {
        return new DefaultEntityConsumerListener<S, T>(entityClass, tableName, topicName, messageHandler, predicate, converter);
    }

    @Override
    public ConsumerListener.QueueBuilder<S> wrapperQueue() {
        return ConsumerListener.builder(this.build());
    }
}

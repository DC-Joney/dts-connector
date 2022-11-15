package com.kkb.dts.consumer.listener;

import com.kkb.dts.consumer.handler.MessageHandler;
import com.kkb.dts.entity.IdEntity;

import java.util.function.BiPredicate;

public interface EntityConsumerListener<S, T> extends ConsumerListener<S> {

    /**
     * 获取entity 的信息
     */
    EntityInfo<T> entityInfo();


    static <S, T extends IdEntity> Builder<S, T> builder(Class<T> entityClass) {
        return new DefaultEntityListenerBuilder<>(entityClass);
    }

    /**
     * 构建EntityListener 实例
     */
    interface Builder<S, T extends IdEntity> {

        /**
         * entity Class
         */
        Builder<S, T> entityClass(Class<T> entityClass);

        /**
         * entity 转换器
         */
        Builder<S, T> converter(EntityConverter<S, T> converter);

        /**
         * entity 消息处理
         */
        Builder<S, T> messageHandler(MessageHandler<T> handler);


        /**
         * 当前消息是否匹配当前Entity
         */
        Builder<S, T> validate(BiPredicate<S, EntityInfo<?>> predicate);


        Builder<S, T> topic(String topicName);


        Builder<S, T> table(String tableName);


        /**
         * 构建 EntityConsumerListener 实例
         */
        EntityConsumerListener<S, T> build();

        /**
         * 通过QueueListener 对当前listener 进行增强
         */
        QueueBuilder<S> wrapperQueue();
    }


}

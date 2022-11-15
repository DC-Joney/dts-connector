package com.kkb.dts.consumer.listener;

import com.kkb.dts.consumer.MessageData;
import com.kkb.dts.consumer.handler.MessageHandler;
import com.kkb.dts.entity.IdEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.kafka.support.Acknowledgment;

import java.util.function.BiPredicate;

@Slf4j
public class DefaultEntityConsumerListener<S, T extends IdEntity> extends AbstractEntityConsumerListener<S, T> {

    private final EntityConverter<S, T> converter;
    private final MessageHandler<T> messageHandler;
    private final BiPredicate<S, EntityInfo<?>> predicate;

    public DefaultEntityConsumerListener(Class<T> entityClass,String tableName, String topicName,
                                         MessageHandler<T> messageHandler, BiPredicate<S, EntityInfo<?>> predicate, EntityConverter<S, T> converter) {
        super(entityClass, tableName, topicName);
        this.converter = converter;
        this.messageHandler = messageHandler;
        this.predicate = predicate;
    }

    @Override
    public EntityConverter<S, T> getConverter() {
        return converter;
    }

    @Override
    protected void handleMessage(MessageData<T> data, Acknowledgment acknowledgment) throws Exception {
        messageHandler.handle(data, entityInfo(), acknowledgment);
    }

    @Override
    public boolean support(S record) {
        return predicate.test(record, entityInfo());
    }
}

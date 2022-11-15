package com.kkb.dts.consumer.handler;

import com.kkb.dts.consumer.MessageData;
import com.kkb.dts.consumer.listener.EntityInfo;
import org.springframework.kafka.support.Acknowledgment;

public interface MessageHandler<T> {

    void handle(MessageData<T> data, EntityInfo<T> entityInfo, Acknowledgment acknowledgment) throws Exception;

}

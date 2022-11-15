package com.kkb.dts.consumer.streagy;

import org.springframework.core.convert.converter.Converter;
import org.springframework.kafka.support.Acknowledgment;

/**
 * 各个表存入kafka的策略
 */
public interface TopicStrategy<T, R> {

    void handleData(T record, Acknowledgment acknowledgment);

    interface Builder<T, R> {

        void setConverter(Converter<T, R> converter);

        TopicStrategy<T, R> build();
    }


}

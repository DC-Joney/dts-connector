package com.kkb.dts.consumer.listener;

import com.kkb.dts.consumer.MessageData;

/**
 * Entity 转换器
 * @author zhangyang
 */
public interface EntityConverter<S, T> {

    MessageData<T> convert(S record, EntityInfo<T> entityInfo);
}

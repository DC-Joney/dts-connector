package com.kkb.dts.consumer.listener.dts;

import com.alibaba.dts.formats.avro.Record;
import com.kkb.dts.consumer.MessageData;
import com.kkb.dts.consumer.listener.EntityConverter;
import com.kkb.dts.consumer.listener.EntityInfo;
import com.kkb.dts.utils.RecordUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.DataBinder;

/**
 * 转换器，用于将 dts 的{@link Record} 数据类型转为自定义的实体
 * @author zhangyang
 */
@Slf4j
public class RecordConverter<T> implements EntityConverter<Record, T> {

    private final ConversionService conversionService;

    public RecordConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }



    @Override
    @SuppressWarnings("unchecked")
    public MessageData<T> convert(@NonNull Record record, EntityInfo<T> entityInfo) {
        Class<?> entityClass = entityInfo.getEntityClass();
        if (entityClass == null)
            return (MessageData<T>) MessageData.EMPTY;

        try {
            T beforeImage = null;
            T afterImage = null;
            MessageData.MessageDataBuilder<T> builder = MessageData.builder();
            MutablePropertyValues beforePropertyValues = RecordUtils.convertBeforeImageToMap(record);
            if (beforePropertyValues != null){
                beforeImage = (T) entityClass.newInstance();
                bindData(beforePropertyValues, beforeImage);
            }

            MutablePropertyValues dataPropertyValues = RecordUtils.convertAfterImageToMap(record);
            if (dataPropertyValues != null){
                afterImage = (T) entityClass.newInstance();
                bindData(dataPropertyValues, afterImage);
            }

            MessageData<T> messageData = builder.tableName(record.getObjectName())
                    .operation(record.getOperation())
                    .changeBefore(beforeImage)
                    .changeAfter(afterImage)
                    .build();

            log.info("Parse data success: {}", messageData);
            return messageData;

        } catch (InstantiationException | IllegalAccessException e) {
            log.error("", e);
            return (MessageData<T>) MessageData.EMPTY;
        }
    }


    private void bindData(PropertyValues propertyValues, Object data) {
        DataBinder dataBinder = new DataBinder(data);
        dataBinder.setIgnoreUnknownFields(true);
        dataBinder.setConversionService(conversionService);
        dataBinder.bind(propertyValues);
    }

}

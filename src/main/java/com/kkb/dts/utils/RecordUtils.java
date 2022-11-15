package com.kkb.dts.utils;

import com.alibaba.dts.formats.avro.Field;
import com.alibaba.dts.formats.avro.Record;
import lombok.experimental.UtilityClass;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@UtilityClass
public class RecordUtils {

    /**
     * converter beforeImage to Map
     *
     * @param record 获取对应的记录
     */
    public MutablePropertyValues convertBeforeImageToMap(Record record) {
        return convertImageToMap(record.getFields(), record.getBeforeImages());
    }


    /**
     * converter beforeImage to Map
     *
     * @param record 获取对应的记录
     */
    public MutablePropertyValues convertAfterImageToMap(Record record) {
        return convertImageToMap(record.getFields(), record.getAfterImages());
    }


    @SuppressWarnings("unchecked")
    public MutablePropertyValues convertImageToMap(Object fieldData, Object imageData) {

        if (imageData == null || fieldData == null)
            return null;

        if (!ClassUtils.isAssignable(List.class, imageData.getClass()) || !ClassUtils.isAssignable(List.class, fieldData.getClass())) {
            return null;
        }

        List<Object> images = (List<Object>) imageData;
        List<Field> fields = (List<Field>) fieldData;

        if (CollectionUtils.isEmpty(images) && CollectionUtils.isEmpty(images))
            return null;

        return fieldsToMap(fields, images, new MutablePropertyValues());

    }


    private MutablePropertyValues fieldsToMap(List<Field> fields, List<Object> values, MutablePropertyValues propertyValues) {

        //fields的位置与 values 中的位置是对应的
        for (int i = 0; i < fields.size(); i++) {

            if (values.get(i) == null)
                continue;

            valueToMap(fields.get(i), values.get(i), propertyValues);
        }
        return propertyValues;
    }

    private void valueToMap(Field field, Object value, MutablePropertyValues propertyValues) {
        switch (field.getDataTypeNumber()) {
            case 1:
            case 3:
            case 8:
                setIntValue(field, value, propertyValues);
                break;
            case 7:
                setTimeStamp(field, value, propertyValues);
                break;
            case 12:
                setDateValue(field, value, propertyValues);
                break;
            case 15:
            case 253:
                setStringValue(field, value, propertyValues);
                break;
            default:
                propertyValues.addPropertyValue(field.getName(), value);
                break;
        }
    }

    private void setIntValue(Field field, Object value, MutablePropertyValues propertyValues) {
        com.alibaba.dts.formats.avro.Integer integer = (com.alibaba.dts.formats.avro.Integer) value;
        propertyValues.addPropertyValue(DbKeyUtil.convertToJava(field.getName()), integer.getValue());
    }

    private void setStringValue(Field field, Object value, MutablePropertyValues propertyValues) {
        com.alibaba.dts.formats.avro.Character character = (com.alibaba.dts.formats.avro.Character) value;
        String s = new String(character.getValue().array());//, "utf8"
        propertyValues.addPropertyValue(DbKeyUtil.convertToJava(field.getName()), s);
    }

    private void setDateValue(Field field, Object value, MutablePropertyValues propertyValues) {
        com.alibaba.dts.formats.avro.DateTime dateTime = (com.alibaba.dts.formats.avro.DateTime) value;
        int year = dateTime.getYear(),
                month = dateTime.getMonth(),
                day = dateTime.getDay(),
                hour = dateTime.getHour(),
                minute = dateTime.getMinute(),
                second = dateTime.getSecond();
        Calendar calendar = Calendar.getInstance();
        /*
         * 这里month需要减1
         * 小时要加8
         */
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.add(Calendar.HOUR_OF_DAY, 8);
        propertyValues.addPropertyValue(DbKeyUtil.convertToJava(field.getName()), calendar.getTime());
    }

    private void setTimeStamp(Field field, Object value, MutablePropertyValues propertyValues) {
        com.alibaba.dts.formats.avro.Timestamp timestamp = (com.alibaba.dts.formats.avro.Timestamp) value;
        propertyValues.addPropertyValue(DbKeyUtil.convertToJava(field.getName()), new Date(timestamp.getTimestamp() * 1000));
    }

}

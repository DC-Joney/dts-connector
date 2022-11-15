package com.kkb.dts.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

/**
 * 将普通的java对象序列化为 数组
 *
 * @author zhangyang
 */
public class ListsSerializer<T> extends JsonSerializer<T> {

    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (!(t instanceof List)) {
            jsonGenerator.writeStartArray(1);
            jsonGenerator.writeObject(t);
            jsonGenerator.writeEndArray();
        }
    }
}

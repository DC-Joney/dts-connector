package com.kkb.dts.consumer;

import com.alibaba.dts.formats.avro.Operation;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kkb.dts.common.json.ListsSerializer;
import lombok.*;

@Builder
@Getter
@ToString
@JsonAutoDetect
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageData<T> {

    @JsonIgnore
    public static final MessageData<?> EMPTY = new MessageData<>();

    @JsonProperty("table")
    private String tableName;

    @JsonProperty("action")
    private Operation operation;

    @JsonProperty("before")
    @JsonSerialize(using = ListsSerializer.class)
    private T changeBefore;

    @JsonSerialize(using = ListsSerializer.class)
    @JsonProperty("data")
    private T changeAfter;

}

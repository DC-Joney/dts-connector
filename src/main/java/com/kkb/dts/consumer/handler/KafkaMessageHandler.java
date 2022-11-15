package com.kkb.dts.consumer.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkb.dts.consumer.MessageData;
import com.kkb.dts.consumer.listener.EntityInfo;
import com.kkb.dts.entity.IdEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * 消息数据处理handler
 *
 * @author zhangyang
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaMessageHandler<T extends IdEntity> implements MessageHandler<T> {

    private static final String TABLE_HEADER = "TABLE_NAME";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper jsonMapper;

    public KafkaMessageHandler(ProducerFactory<String, String> producerFactory, ObjectMapper objectMapper) {
        this.kafkaTemplate = new KafkaTemplate<>(producerFactory);
        this.jsonMapper = objectMapper;
    }

    @Override
    public void handle(MessageData<T> data, EntityInfo<T> entityInfo, Acknowledgment acknowledgment) throws JsonProcessingException {
        String messageKey = data.getChangeAfter() != null ? data.getChangeAfter().getId() : data.getChangeBefore().getId();
        String jsonValue = jsonMapper.writeValueAsString(data);
        Message<String> message = createMessage(jsonValue, messageKey, entityInfo);
        kafkaTemplate.send(message).addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(@NonNull Throwable error) {
                log.error("发送消息成功, 消息id: {}, 消息对应的表：{}, 消息对应的操作: {}, \n错误原因为：{}",
                        messageKey, entityInfo.getTableName(), data.getOperation(), error);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("发送消息成功, 消息id: {}, 消息对应的表：{}, 消息对应的操作: {}, 消息响应结果：{}",
                        messageKey, entityInfo.getTableName(), data.getOperation(), result.getRecordMetadata());
                acknowledgment.acknowledge();
            }
        });

    }

    /**
     * 创建 Message
     *
     * @param payload    序列化后的实体对象
     * @param messageKey 消息KEY
     * @param entityInfo entity 信息
     */
    private Message<String> createMessage(String payload, String messageKey, EntityInfo<?> entityInfo) {
        return MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, entityInfo.getTopicName())
                .setHeader(KafkaHeaders.MESSAGE_KEY, messageKey)
                .setHeaderIfAbsent(KafkaHeaders.TIMESTAMP, System.currentTimeMillis())
                .setHeaderIfAbsent(KafkaHeaders.TIMESTAMP_TYPE, "CREATE_TIME")
                .setHeaderIfAbsent(TABLE_HEADER, entityInfo.getTableName())
                .setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}

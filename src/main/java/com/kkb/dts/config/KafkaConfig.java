package com.kkb.dts.config;

import com.alibaba.dts.formats.avro.Record;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkb.dts.consumer.handler.KafkaMessageHandler;
import com.kkb.dts.consumer.listener.EntityConsumerListener;
import com.kkb.dts.consumer.listener.EntityConverter;
import com.kkb.dts.consumer.listener.QueueConsumerListener;
import com.kkb.dts.consumer.listener.dts.EntityPredicate;
import com.kkb.dts.consumer.listener.dts.RecordConverter;
import com.kkb.dts.entity.*;
import com.kkb.dts.properties.EntityMappingProperties;
import com.kkb.dts.properties.KafkaConnectorProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

@Configuration
@EnableConfigurationProperties(EntityMappingProperties.class)
public class KafkaConfig implements BeanFactoryAware {

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ObjectMapper jsonMapper;

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * 用于判断当前
     */
    @Bean
    public EntityPredicate entityPredicate() {
        return new EntityPredicate();
    }

    @Bean
    public <S, T> EntityConverter<Record, T> entityConverter() {
        return new RecordConverter<>(conversionService);
    }


    /**
     * ImUser 配置
     */
    @Bean
    public QueueConsumerListener<Record> imUserConsumerListener() {
        return EntityConsumerListener.<Record, ImUser>builder(ImUser.class)
                .converter(entityConverter())
                .validate(entityPredicate())
                .messageHandler(kafkaMessageHandler())
                .wrapperQueue()
                .name("ImUserQueue")
                .build();
    }


    /**
     * imGroup 配置
     */
    @Bean
    public QueueConsumerListener<Record> imGroupConsumerListener() {
        return EntityConsumerListener.<Record, ImGroup>builder(ImGroup.class)
                .converter(entityConverter())
                .validate(entityPredicate())
                .messageHandler(kafkaMessageHandler())
                .wrapperQueue()
                .name("ImGroupQueue")
                .build();
    }


    /**
     * imGroupMember 配置
     */
    @Bean
    public QueueConsumerListener<Record> imGroupMemberConsumerListener() {
        return EntityConsumerListener.<Record, ImGroupMember>builder(ImGroupMember.class)
                .converter(entityConverter())
                .validate(entityPredicate())
                .messageHandler(kafkaMessageHandler())
                .wrapperQueue()
                .name("ImGroupMemberQueue")
                .build();
    }


    /**
     * imFriend 配置
     */
    @Bean
    public QueueConsumerListener<Record> imFriendConsumerListener() {
        return EntityConsumerListener.<Record, ImFriend>builder(ImFriend.class)
                .converter(entityConverter())
                .validate(entityPredicate())
                .messageHandler(kafkaMessageHandler())
                .wrapperQueue()
                .name("ImFriendQueue")
                .build();
    }


    /**
     * snapImUser 配置
     */
    @Bean
    public QueueConsumerListener<Record> snapImUserConsumerListener() {
        return EntityConsumerListener.<Record, SnapshotImUser>builder(SnapshotImUser.class)
                .validate(entityPredicate())
                .converter(entityConverter())
                .messageHandler(kafkaMessageHandler())
                .wrapperQueue()
                .name("SnapImUserQueue")
                .build();
    }


    /**
     * snapImFriend 配置
     */
    @Bean
    public QueueConsumerListener<Record> snapImFriendConsumerListener() {
        return EntityConsumerListener.<Record, SnapshotImFriend>builder(SnapshotImFriend.class)
                .validate(entityPredicate())
                .converter(entityConverter())
                .messageHandler(kafkaMessageHandler())
                .wrapperQueue()
                .name("SnapImFriendQueue")
                .build();
    }

    /**
     * 我们无法保证ProducerFactory的单例性
     */
    @SuppressWarnings("unchecked")
    public <T extends IdEntity> KafkaMessageHandler<T> kafkaMessageHandler() {
        ProducerFactory<String, String> producerFactory = beanFactory.getBean(ProducerFactory.class);
        return new KafkaMessageHandler<>(producerFactory, jsonMapper);
    }


    /**
     * 这里利用BeanPostProcessor 的优先初始化机制，来保证将bean注册到beanDefinitionMap中
     */
    @Configuration
    @RequiredArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @ConditionalOnProperty(value = "com.kkb.kafka.admin.auto-create-topic", havingValue = "true")
    static class EntityTopicConfig implements BeanPostProcessor, InitializingBean,BeanFactoryAware {

        final EntityMappingProperties mappingProperties;
        final KafkaConnectorProperties properties;
        BeanDefinitionRegistry registry;

        @Override
        public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
            Assert.isInstanceOf(BeanDefinitionRegistry.class, beanFactory, "The beanFactory must be instance of BeanDefinitionRegistry");
            this.registry = (BeanDefinitionRegistry) beanFactory;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            for (EntityMappingProperties.Entity entity : mappingProperties.getMapping().values()) {
                String topicName = entity.getTopic();
                //计算Topic的 partition 分区
                int partitions = properties.getListener().getConcurrency() * properties.getAdmin().getInstanceCount();
                //创建新的Topic 名称
                NewTopic newTopic = new NewTopic(topicName, partitions, properties.getAdmin().getReplication()).configs(properties.getAdmin().getConfigs());
                BeanDefinition beanDefinition = BeanDefinitionBuilder
                        .genericBeanDefinition(NewTopic.class, () -> newTopic)
                        .setAutowireMode(AbstractAutowireCapableBeanFactory.AUTOWIRE_BY_TYPE)
                        .setRole(BeanDefinition.ROLE_APPLICATION).getBeanDefinition();
                registry.registerBeanDefinition(topicName, beanDefinition);
            }

        }
    }
}

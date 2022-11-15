package com.kkb.dts.config;


import com.kkb.dts.properties.KafkaConnectorProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.transaction.KafkaTransactionManager;

/**
 * kafka 配置信息
 * copy from {@link org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration}
 *
 * @author zhangyang
 */
//@Configuration(proxyBeanMethods = false)
@Configuration
@ConditionalOnClass(KafkaTemplate.class)
@EnableConfigurationProperties(KafkaConnectorProperties.class)
public class KafkaConnectorConfiguration {

    private final KafkaConnectorProperties properties;

    public KafkaConnectorConfiguration(KafkaConnectorProperties properties) {
        this.properties = properties;
    }

    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory,
                                             ProducerListener<Object, Object> kafkaProducerListener,
                                             ObjectProvider<RecordMessageConverter> messageConverter) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        return kafkaTemplate;
    }

    @Bean
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }

    @Bean
    public ConsumerFactory<String, Object> kafkaConsumerFactory() {
        DefaultKafkaConsumerFactory<String, Object> factory = new DefaultKafkaConsumerFactory<>(
                this.properties.buildConsumerProperties());
//        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }

    /**
     * 保证ProducerFactory的多实例
     */
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ProducerFactory<?, ?> kafkaProducerFactory() {
        DefaultKafkaProducerFactory<?, ?> factory = new DefaultKafkaProducerFactory<>(
                this.properties.buildProducerProperties());
        String transactionIdPrefix = this.properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        return factory;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.kafka.producer.transaction-id-prefix")
    public KafkaTransactionManager<?, ?> kafkaTransactionManager(ProducerFactory<?, ?> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }



    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ?> kafkaListenerContainerFactory() {
        KafkaConnectorProperties.Listener listener = properties.getListener();
        ConcurrentKafkaListenerContainerFactory<String, ?> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory());

        PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        propertyMapper.from(listener::getConcurrency).to(factory::setConcurrency);
        propertyMapper.from(listener::getType)
                .whenEqualTo(KafkaConnectorProperties.Listener.Type.BATCH).toCall(()-> factory.setBatchListener(true));

        ContainerProperties containerProperties = factory.getContainerProperties();
        propertyMapper.from(listener::getAckMode).to(containerProperties::setAckMode);
        propertyMapper.from(listener::getPollTimeOut).to(containerProperties::setPollTimeout);
        propertyMapper.from(listener::getClientId).whenHasText().to(containerProperties::setClientId);

        return factory;
    }



    @Configuration
    @ConditionalOnProperty(value = "com.kkb.kafka.admin.auto-create-topic", havingValue = "true", matchIfMissing = false)
    static class TopicConfiguration {

        @Autowired
        private KafkaConnectorProperties properties;

        @Bean
        public KafkaAdmin kafkaAdmin() {
            KafkaAdmin kafkaAdmin = new KafkaAdmin(this.properties.buildAdminProperties());
            kafkaAdmin.setFatalIfBrokerNotAvailable(this.properties.getAdmin().isFailFast());
            return kafkaAdmin;
        }


    }


}


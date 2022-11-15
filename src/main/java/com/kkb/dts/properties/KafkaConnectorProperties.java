package com.kkb.dts.properties;

import lombok.Data;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.*;

/**
 * DTS kafka 配置
 *
 * copy from {@link org.springframework.boot.autoconfigure.kafka.KafkaProperties}
 *
 * @author zhangyang
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "com.kkb.kafka")
public class KafkaConnectorProperties {

    private List<String> bootstrapServers = new ArrayList<>(Collections.singletonList("localhost:9092"));

    /**
     * ID to pass to the server when making requests. Used for server-side logging.
     */
    private String clientId;

    /**
     * Additional properties, common to producers and consumers, used to configure the
     * client.
     */
    private final Map<String, String> properties = new HashMap<>();

    /**
     * 消费者
     */
    private Consumer consumer = new Consumer();

    /**
     * 监听器
     */
    private Listener listener = new Listener();

    /**
     * 生产者
     */
    private Producer producer = new Producer();


    private Admin admin = new Admin();


    @Data
    public static class Admin {
        private final Security security = new Security();

        private final Sasl sasl = new Sasl();

        /**
         * 是否自动创建Topic，如果开启则会默认注入 KafkaAdmin 以及生成对应的topic
         */
        private boolean autoCreateTopic = false;

        /**
         * Comma-delimited list of host:port pairs to use for establishing the initial
         * connections to the Kafka cluster. Overrides the global property, for consumers.
         */
        private List<String> bootstrapServers;

        /**
         * 分区数 = 实例数 * listener concurrency
         */
        private int instanceCount = 1;

        /**
         * partition 对应的副本数量
         */
        private short replication = 1;


        /**
         * 是否快速失败
         */
        private boolean failFast;

        /**
         * 创建KafkaAdmin 时的配置信息
         */
        private Map<String, String> properties = new HashMap<>();

        /**
         * New Partition config
         */
        private Map<String,String> configs = new HashMap<>();

        public Map<String, Object> buildProperties() {
            Properties properties = new Properties();
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            map.from(this::getBootstrapServers).to(properties.in(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
            return properties.with(this.security, this.sasl, this.properties);
        }

    }


    @Data
    public static class Consumer {

        private final Security security = new Security();

        private final Sasl sasl = new Sasl();

        /**
         * 消费者默认的topic
         */
        private String topicName;

        /**
         * Comma-delimited list of host:port pairs to use for establishing the initial
         * connections to the Kafka cluster. Overrides the global property, for consumers.
         */
        private List<String> bootstrapServers;

        /**
         * ID to pass to the server when making requests. Used for server-side logging.
         */
        private String clientId;

        /**
         * Whether the consumer's offset is periodically committed in the background.
         */
        private Boolean enableAutoCommit;

        /**
         * Maximum amount of time the server blocks before answering the fetch request if
         * there isn't sufficient data to immediately satisfy the requirement given by
         * "fetch-min-size".
         */
        private Duration fetchMaxWait;


        /**
         * Unique string that identifies the consumer group to which this consumer
         * belongs.
         */
        private String groupId;

        /**
         * Deserializer class for keys.
         */
        private Class<?> keyDeserializer = StringDeserializer.class;

        /**
         * Deserializer class for values.
         */
        private Class<?> valueDeserializer = StringDeserializer.class;

        /**
         * Maximum number of records returned in a single call to poll().
         */
        private Integer maxPollRecords;

        private String sessionTimeoutMs;

        private String requestTimeoutMs;

        /**
         * Additional consumer-specific properties used to configure the client.
         */
        private final Map<String, String> properties = new HashMap<>();

        public Map<String, Object> buildProperties() {
            Properties properties = new Properties();
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();

            map.from(this::getBootstrapServers).to(properties.in(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
            map.from(this::getClientId).to(properties.in(ConsumerConfig.CLIENT_ID_CONFIG));
            map.from(this::getEnableAutoCommit).to(properties.in(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG));
            map.from(this::getFetchMaxWait).asInt(Duration::toMillis)
                    .to(properties.in(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG));
            map.from(this::getGroupId).to(properties.in(ConsumerConfig.GROUP_ID_CONFIG));
            map.from(this::getKeyDeserializer).to(properties.in(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
            map.from(this::getValueDeserializer).to(properties.in(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
            map.from(this::getMaxPollRecords).to(properties.in(ConsumerConfig.MAX_POLL_RECORDS_CONFIG));
            map.from(this::getRequestTimeoutMs).to(properties.in(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG));
            map.from(this::getSessionTimeoutMs).to(properties.in(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG));
            return properties.with(this.security, this.sasl, this.properties);
        }

    }


    @Data
    public static class Producer {
        /**
         * 用于设置Producer 端的权限信息
         */
        private Security security = new Security();

        /**
         * 设置 Producer 端关于SASL 的认证
         */
        private Sasl sasl = new Sasl();
        private List<String> bootstrapServers;
        private String clientId;
        private Class<?> keySerializer = StringSerializer.class;
        private Class<?> valueSerializer = StringSerializer.class;

        /**
         * 重试的次数
         */
        private Integer retries;

        /**
         * 事物ID 的前缀，用于觉得是否要开启事物
         */
        private String transactionIdPrefix;

        private final Map<String, String> properties = new HashMap<>();

        public Map<String, Object> buildProperties() {
            Properties properties = new Properties();
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            map.from(this::getBootstrapServers).to(properties.in(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));

            map.from(this::getClientId).to(properties.in(ProducerConfig.CLIENT_ID_CONFIG));
            map.from(this::getKeySerializer).to(properties.in(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
            map.from(this::getRetries).to(properties.in(ProducerConfig.RETRIES_CONFIG));
            map.from(this::getValueSerializer).to(properties.in(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
            return properties.with(this.security, this.sasl, this.properties);
        }
    }


    @Data
    public static class Listener {
        public enum Type {

            /**
             * Invokes the endpoint with one ConsumerRecord at a time.
             */
            SINGLE,

            /**
             * Invokes the endpoint with a batch of ConsumerRecords.
             */
            BATCH

        }

        private Type type = Type.SINGLE;

        /**
         * 设置ack的模式
         */
        private ContainerProperties.AckMode ackMode;

        /**
         * Number of threads to run in the listener containers.
         */
        private Integer concurrency;

        private String clientId;

        private Long pollTimeOut;

    }


    public static class Security {

        /**
         * Security protocol used to communicate with brokers.
         */
        private String protocol;

        public String getProtocol() {
            return this.protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public Map<String, Object> buildProperties() {
            Properties properties = new Properties();
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            map.from(this::getProtocol).to(properties.in(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG));
            return properties;
        }
    }

    @Data
    public static class Sasl {

        private static final String jaasTemplate = PlainLoginModule.class.getName() + " required username=\"%s-%s\" password=\"%s\";";

        /**
         * 用于设置阿里云DTS消费组对应的 group-id 主要用于认证（与消费组 id 是不一样的）
         */
        private String groupId;

        /**
         * 用户名称
         */
        private String userName;

        /**
         * 密码
         */
        private String password;

        /**
         *  {@link SaslConfigs#SASL_MECHANISM_DOC}
         */
        private String saslMechanism;

        public Map<String, Object> buildProperties() {
            Properties properties = new Properties();
            if (StringUtils.hasText(userName) && StringUtils.hasText(password) && StringUtils.hasText(groupId)) {
                String jaasCfg = String.format(Sasl.jaasTemplate, userName, groupId, password);
                PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
                map.from(this::getSaslMechanism).to(properties.in(SaslConfigs.SASL_MECHANISM));
                map.from(jaasCfg).to(properties.in(SaslConfigs.SASL_JAAS_CONFIG));
            }
            return properties;
        }
    }


    /**
     * 构建通用的配置信息
     */
    private Map<String, Object> buildCommonProperties() {
        Map<String, Object> properties = new HashMap<>();
        if (this.bootstrapServers != null) {
            properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        }
        if (this.clientId != null) {
            properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, this.clientId);
        }
        if (!CollectionUtils.isEmpty(this.properties)) {
            properties.putAll(this.properties);
        }
        return properties;
    }

    /**
     * 构建消费者配置
     */
    public Map<String, Object> buildConsumerProperties() {
        Map<String, Object> properties = buildCommonProperties();
        properties.putAll(this.consumer.buildProperties());
        return properties;
    }

    /**
     * Create an initial map of producer properties from the state of this instance.
     * <p>
     * This allows you to add additional properties, if necessary, and override the
     * default kafkaProducerFactory bean.
     *
     * @return the producer properties initialized with the customizations defined on this
     * instance
     */
    public Map<String, Object> buildProducerProperties() {
        Map<String, Object> properties = buildCommonProperties();
        properties.putAll(this.producer.buildProperties());
        return properties;
    }

    /**
     * 构建KafkaAdmin的配置信息
     */
    public Map<String, Object> buildAdminProperties() {
        Map<String, Object> properties = buildCommonProperties();
        properties.putAll(this.admin.buildProperties());
        return properties;
    }


    @SuppressWarnings("serial")
    private static class Properties extends HashMap<String, Object> {

        <V> java.util.function.Consumer<V> in(String key) {
            return (value) -> put(key, value);
        }

        Properties with(Security security, Map<String, String> properties) {
            putAll(security.buildProperties());
            putAll(properties);
            return this;
        }

        Properties with(Security security, Sasl sasl, Map<String, String> properties) {
            putAll(sasl.buildProperties());
            putAll(security.buildProperties());
            putAll(properties);
            return this;
        }

    }


}

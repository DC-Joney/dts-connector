package com.kkb.dts.consumer.listener;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kkb.dts.annotation.EntityMapping;
import com.kkb.dts.consumer.MessageData;
import com.kkb.dts.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

import java.lang.annotation.Annotation;

/**
 * 抽象实现，用于提供基本的功能实现，以应对与下游不同的具体实现
 */
@Slf4j
public abstract class AbstractEntityConsumerListener<S, T extends IdEntity> implements EntityConsumerListener<S, T>, EmbeddedValueResolverAware, InitializingBean {

    //缓存所有tableClass对应的tableName
    private static final LoadingCache<Class<?>, EntityMappingInfo> entityMappingCache;
    static {
        entityMappingCache = CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .softValues()
                .weakKeys()
                .build(new EntityMappingValueCacheLoader());
    }

    private EntityInfo<T> entityInfo;
    private StringValueResolver valueResolver;

    @Autowired
    private ConfigurableBeanFactory beanFactory;

    public AbstractEntityConsumerListener(Class<T> entityClass, String tableName, String topicName) {
        this.entityInfo = EntityInfo.create(entityClass, tableName, topicName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        EntityMappingInfo mappingInfo = entityMappingCache.getUnchecked(entityInfo.getEntityClass());

        //优先使用传入的 tableName
        String tableName = entityInfo.getTableName();
        if (StringUtils.isEmpty(tableName))
            tableName = mappingInfo.getTableName();

        Assert.hasText(tableName, "Current entity not exists tableName");
        //通过valueResolver 来解析对应的tableName名称
         tableName = valueResolver.resolveStringValue(tableName);

        //优先使用传入的 topicName
        String topicName = entityInfo.getTopicName();
        if (StringUtils.isEmpty(topicName))
            topicName = mappingInfo.getTopicName();

        Assert.hasText(topicName, "Current entity not exists topicName");
        //通过valueResolver 来解析对应的topic名称
        topicName = valueResolver.resolveStringValue(topicName);

        //从新构建新的EntityInfo
        entityInfo = EntityInfo.create(entityInfo.getEntityClass(), tableName, topicName);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.valueResolver = valueResolver;
    }

    @Override
    public EntityInfo<T> entityInfo() {
        return entityInfo;
    }

    @Override
    public void listen(S record, Acknowledgment acknowledgment) {
        try {
            MessageData<T> dataRecord = getConverter().convert(record, entityInfo);
            if (dataRecord != null) {
                handleMessage(dataRecord, acknowledgment);
            }
        } catch (Exception ex) {
            log.error("Handle data error: ", ex);
        }
    }

    public abstract EntityConverter<S, T> getConverter();

    /**
     * 是否支持当前接受到的数据类型
     */
    public abstract boolean support(S record);

    /**
     * @param data 解析出的数据
     */
    protected abstract void handleMessage(MessageData<T> data, Acknowledgment acknowledgment) throws Exception;


    private static class EntityMappingValueCacheLoader extends CacheLoader<Class<?>, EntityMappingInfo> {

        private static final String TOPIC_ATTRIBUTE = "topic";
        private static final String TABLE_ATTRIBUTE = "table";

        @Override
        public EntityMappingInfo load(@NonNull Class<?> entityClass) throws Exception {
            Annotation annotation = AnnotationUtils.findAnnotation(entityClass, EntityMapping.class);
            if (annotation == null)
                return EntityMappingInfo.empty();

            AnnotationAttributes attributes = AnnotationUtils.getAnnotationAttributes(annotation, false, true);
            String topicName = attributes.getString(TOPIC_ATTRIBUTE);
            String tableName = attributes.getString(TABLE_ATTRIBUTE);
            return EntityMappingInfo.create(tableName, topicName);
        }
    }


    private static class AnnotationValueCacheLoader extends CacheLoader<Class<?>, AnnotationAttributes> {

        private final Class<? extends Annotation> annotationClass;

        public AnnotationValueCacheLoader(Class<? extends Annotation> annotationClass) {
            this.annotationClass = annotationClass;
        }

        @Override
        public AnnotationAttributes load(@NonNull Class<?> entityClass) throws Exception {
            Annotation annotation = AnnotationUtils.findAnnotation(entityClass, annotationClass);
            if (annotation == null)
                return null;

            return AnnotationUtils.getAnnotationAttributes(annotation, false, true);
        }
    }

    @Getter
    @AllArgsConstructor(staticName = "create")
    @NoArgsConstructor(staticName = "empty")
    static class EntityMappingInfo {
        private String tableName;
        private String topicName;
    }


}

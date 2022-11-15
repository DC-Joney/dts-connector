package com.kkb.dts.consumer.listener;


import lombok.AllArgsConstructor;

/**
 * 表示当前Entity的元数据信息
 * @author zhangyang
 */
@AllArgsConstructor(staticName = "create")
public class EntityInfo<T> {


    /**
     * 当前Entity 对应的class
     */
    private Class<T> entityClass;

    /**
     * 当前Entity 对应的表名称
     */
    private String tableName;

    /**
     * 当前Entity对应的Topic名称
     */
    private String topicName;

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTopicName() {
        return topicName;
    }

    @Override
    public String toString() {
        return "EntityInfo{" +
                "entityClass=" + entityClass +
                ", tableName='" + tableName + '\'' +
                '}';
    }
}

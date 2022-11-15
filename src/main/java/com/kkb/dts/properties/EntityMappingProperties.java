package com.kkb.dts.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "com.kkb")
public class EntityMappingProperties {

    /**
     * entity 与 info 对照信息
     */
    private Map<String, Entity> mapping = new HashMap<>();


    @Data
    public static class Entity {
        private String table;
        private String topic;
    }

}

package com.kkb.dts.annotation;

import java.lang.annotation.*;

/**
 * entity 实体类与 topic、table的对照信息
 * @author zhangyang
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EntityMapping {

    /**
     * topic 名称
     */
    String topic() default "";

    /**
     * 表名称
     */
    String table() default "";
}

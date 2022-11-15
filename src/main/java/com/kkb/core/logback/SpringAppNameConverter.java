package com.kkb.core.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextVO;
import org.apache.commons.lang3.StringUtils;

/**
 * todo 此类拷贝于 kkb-cloud-core
 * <p>
 * SpringAppNameConverter class description
 * 在将logback.xml放在仓库，通过config去加载后，解析器无法第一时间解析
 * springAppName(将logback.xml放在classpath路径可以,搞不懂为什么)，最后只能自己写个解析器，
 * 根据<springProperty scope="context" name="springAppName" source="spring.application.name" />属性中的name
 * 去提取，并且在logback.xml中配置word converter
 * <conversionRule conversionWord="appName" converterClass="com.kkb.core.logback.SpringAppNameConverter"/>
 * 在patten中使用%appName来解析
 *
 * @author lbzheng@kaikeba.com
 * @date 2019-03-20
 */
public class SpringAppNameConverter extends ClassicConverter {

    /**
     * use logback config <springProperty scope="context" name="springAppName" source="spring.application.name" />
     */
    public static final String SPRING_APP_NAME = "springAppName";

    @Override
    public String convert(ILoggingEvent event) {
        LoggerContextVO loggerContextVO = event.getLoggerContextVO();
        if (loggerContextVO != null) {
            if (loggerContextVO.getPropertyMap() != null) {
                if (StringUtils.isNotEmpty(loggerContextVO.getPropertyMap().get(SPRING_APP_NAME))) {
                    return loggerContextVO.getPropertyMap().get(SPRING_APP_NAME);
                }
            }
        }
        return "-";
    }
}

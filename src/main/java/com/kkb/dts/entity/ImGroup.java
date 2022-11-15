package com.kkb.dts.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kkb.dts.annotation.EntityMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhangyang
 */
@Data
@EntityMapping(table = "${com.kkb.mapping.im-group.table}", topic = "${com.kkb.mapping.im-group.topic}")
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImGroup extends IdEntity implements Serializable {

    private String id;

    private String imUserId;

    private String thirdImGroupId;

    private String nickName;

    private String headUrl;

    private String imType;

    private String source;

    private Integer status;

    private String appId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date updateTime;

    private String content;

    private String ext;
}

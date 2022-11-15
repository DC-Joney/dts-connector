package com.kkb.dts.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kkb.dts.annotation.EntityMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 类描述
 *
 * @author zhangyang
 */
@Data
@EntityMapping(table = "${com.kkb.mapping.im-user.table}", topic = "${com.kkb.mapping.im-user.topic}")
@EqualsAndHashCode(callSuper = false)
public class ImUser extends IdEntity implements Serializable {

    private String id;

    @JsonProperty(value = "third_im_id")
    private String thirdImId;

    @JsonProperty(value = "third_alias")
    private String thirdAlias;

    @JsonProperty(value = "nick_name")
    private String nickName;

    @JsonProperty(value = "head_url")
    private String headUrl;

    @JsonProperty(value = "im_type")
    private String imType;

    @JsonProperty(value = "user_type")
    private Integer userType;

    private String source;

    private Integer status;

    @JsonProperty(value = "app_id")
    private String appId;

    private String phone;

    private String content;

    private String ext;

    private String unionid;

    @JsonProperty(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    @JsonProperty(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date updateTime;


}

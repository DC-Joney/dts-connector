package com.kkb.dts.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kkb.dts.annotation.EntityMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangyang
 */
@Data
@EntityMapping(table = "${com.kkb.mapping.im-group-member.table}", topic = "${com.kkb.mapping.im-group-member.topic}")
@EqualsAndHashCode(callSuper = false)
public class ImGroupMember extends IdEntity implements Serializable {

    private String id;

    private String imUserMemberId;

    private String imGroupId;

    private String memberRole;

    private String note;

    private String content;

    private String ext;

    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date updateTime;

}

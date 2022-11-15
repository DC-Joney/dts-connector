package com.kkb.dts.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kkb.dts.annotation.Description;
import com.kkb.dts.annotation.EntityMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * im好友关系表
 * </p>
 *
 * @author chixin
 * @since 2021-08-01
 */
@Data
@EntityMapping(table = "${com.kkb.mapping.im-friend.table}", topic = "${com.kkb.mapping.im-friend.topic}")
@EqualsAndHashCode(callSuper = false)
public class ImFriend extends IdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Description(value = "唯一id")
    private String id;

    @Description(value = "im用户id")
    private String imUserId;

    @Description(value = "im用户id")
    private String imUserFriendId;

    @Description(value = "备注")
    private String note;

    @Description(value = "状态 1 正常 0 删除")
    private Integer status;

    @Description(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    @Description(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date updateTime;

    private String content;

    private String ext;

}

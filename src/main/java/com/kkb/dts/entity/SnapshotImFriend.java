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
@EntityMapping(table = "${com.kkb.mapping.snapshot-im-friend.table}", topic = "${com.kkb.mapping.snapshot-im-friend.topic}")
@EqualsAndHashCode(callSuper = false)
public class SnapshotImFriend extends IdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String appId;

    @JsonProperty(value = "unique_id")
    private String uniqueId;

    private String source;

    @JsonProperty(value = "v1_content")
    private String v1Content;

    @JsonProperty(value = "hkz_content")
    private String hkzContent;

    @JsonProperty(value = "v1_user_id")
    private String v1UserId;

    @JsonProperty(value = "v1_user_third_im_id")
    private String v1UserThirdImId;

    @JsonProperty(value = "hkz_user_id")
    private String hkzUserId;

    @JsonProperty(value = "hkz_user_third_im_id")
    private String hkzUserThirdImId;

    @JsonProperty(value = "v1_friend_id")
    private String v1FriendId;

    @JsonProperty(value = "v1_friend_third_im_id")
    private String v1FriendThirdImId;

    @JsonProperty(value = "hkz_friend_id")
    private String hkzFriendId;

    @JsonProperty(value = "hkz_friend_third_im_id")
    private String hkzFriendThirdImId;

    @JsonProperty(value = "user_phone")
    private String userPhone;

    @JsonProperty(value = "friend_unionid")
    private String friendUnionid;

    private Integer status;

    private String note;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date updateTime;

}

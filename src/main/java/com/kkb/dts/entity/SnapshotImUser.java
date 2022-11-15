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
@EntityMapping(table = "${com.kkb.mapping.snapshot-im-user.table}", topic = "${com.kkb.mapping.snapshot-im-user.topic}")
@EqualsAndHashCode(callSuper = false)
public class SnapshotImUser extends IdEntity implements Serializable {

    private String id;




}

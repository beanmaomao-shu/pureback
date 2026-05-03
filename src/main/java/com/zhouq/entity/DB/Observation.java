package com.zhouq.entity.DB;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 观测记录表
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@Getter
@Setter
@TableName("observation")
@ApiModel(value = "Observation对象", description = "观测记录表")
public class Observation implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("观测时间")
    @TableField("obs_time")
    private LocalDateTime obsTime;

    @ApiModelProperty("经度")
    @TableField("longitude")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    @TableField("latitude")
    private BigDecimal latitude;

    @ApiModelProperty("生态系统ID")
    @TableField("ecosystem_id")
    private Long ecosystemId;

    @ApiModelProperty("观测人")
    @TableField("observer")
    private String observer;

    @ApiModelProperty("水温")
    @TableField("water_temp")
    private BigDecimal waterTemp;

    @ApiModelProperty("盐度")
    @TableField("salinity")
    private BigDecimal salinity;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("创建人")
    @TableField("creator_id")
    private Long creatorId;

      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}

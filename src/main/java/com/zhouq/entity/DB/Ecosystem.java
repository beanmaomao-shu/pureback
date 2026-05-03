package com.zhouq.entity.DB;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 生态系统表
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@Getter
@Setter
@TableName("ecosystem")
@ApiModel(value = "Ecosystem对象", description = "生态系统表")
public class Ecosystem implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("生态系统名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("生态系统类型")
    @TableField("ecosystem_type")
    private String ecosystemType;

    @ApiModelProperty("所属海域/区域")
    @TableField("sea_area")
    private String seaArea;

    @ApiModelProperty("中心经度")
    @TableField("longitude")
    private java.math.BigDecimal longitude;

    @ApiModelProperty("中心纬度")
    @TableField("latitude")
    private java.math.BigDecimal latitude;

    @ApiModelProperty("平均水深(米)")
    @TableField("avg_depth")
    private java.math.BigDecimal avgDepth;

    @ApiModelProperty("盐度范围")
    @TableField("salinity_range")
    private String salinityRange;

    @ApiModelProperty("温度范围")
    @TableField("temperature_range")
    private String temperatureRange;

    @ApiModelProperty("优势物种")
    @TableField("dominant_species")
    private String dominantSpecies;

    @ApiModelProperty("栖息地特征")
    @TableField("habitat_feature")
    private String habitatFeature;

    @ApiModelProperty("保护等级")
    @TableField("protection_level")
    private String protectionLevel;

    @ApiModelProperty("主要威胁因素")
    @TableField("threat_factors")
    private String threatFactors;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}

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
 * 物种信息表
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@Getter
@Setter
@TableName("species")
@ApiModel(value = "Species对象", description = "物种信息表")
public class Species implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("中文名")
    @TableField("chinese_name")
    private String chineseName;

    @ApiModelProperty("学名")
    @TableField("latin_name")
    private String latinName;

    @ApiModelProperty("门")
    @TableField("phylum")
    private String phylum;

    @ApiModelProperty("纲")
    @TableField("klass")
    private String klass;

    @ApiModelProperty("目")
    @TableField("`order`")
    private String order;

    @ApiModelProperty("科")
    @TableField("family")
    private String family;

    @ApiModelProperty("属")
    @TableField("genus")
    private String genus;

    @ApiModelProperty("种")
    @TableField("species")
    private String species;

    @ApiModelProperty("形态特征")
    @TableField("features")
    private String features;

    @ApiModelProperty("生活习性")
    @TableField("habits")
    private String habits;

    @ApiModelProperty("分布区域")
    @TableField("distribution")
    private String distribution;

    @ApiModelProperty("经度")
    @TableField("longitude")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    @TableField("latitude")
    private BigDecimal latitude;

    @ApiModelProperty("保护等级")
    @TableField("protect_level")
    private String protectLevel;

    @ApiModelProperty("濒危状态")
    @TableField("endangered_status")
    private String endangeredStatus;

    @ApiModelProperty("图片地址")
    @TableField("image_url")
    private String imageUrl;

    @ApiModelProperty("视频地址")
    @TableField("video_url")
    private String videoUrl;

    @ApiModelProperty("参考文献")
    @TableField("`reference`")
    private String reference;

    @ApiModelProperty("创建人")
    @TableField("creator_id")
    private Long creatorId;

      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

      @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}

package com.zhouq.entity.DB;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 观测物种关联表
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@Getter
@Setter
@TableName("observation_species")
@ApiModel(value = "ObservationSpecies对象", description = "观测物种关联表")
public class ObservationSpecies implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("观测ID")
    @TableField("observation_id")
    private Long observationId;

    @ApiModelProperty("物种ID")
    @TableField("species_id")
    private Long speciesId;

    @ApiModelProperty("数量")
    @TableField("quantity")
    private Integer quantity;

    @ApiModelProperty("行为描述")
    @TableField("behavior")
    private String behavior;


}

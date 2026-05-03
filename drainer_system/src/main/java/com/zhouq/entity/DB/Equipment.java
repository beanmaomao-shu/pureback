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
 * 设备
 * </p>
 *
 * @author zhouq
 * @since 2024-03-24
 */
@Getter
@Setter
@TableName("equipment")
@ApiModel(value = "Equipment对象", description = "设备")
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备状态")
    @TableField("status_id")
    private Boolean statusId;

    @ApiModelProperty("ID")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("创建时间")
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
      @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除")
    @TableField("is_delete")
    private Boolean isDelete;

    @ApiModelProperty("经度")
    @TableField("longitude")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    @TableField("latitude")
    private BigDecimal latitude;

    @ApiModelProperty("设备名称")
    @TableField("equipment_name")
    private String equipmentName;

    @ApiModelProperty("设备描述")
    @TableField("device_description")
    private String deviceDescription;

    @TableField("user_id")
    private Long userId;


}

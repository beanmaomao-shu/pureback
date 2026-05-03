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
 * 设备状态改变
 * </p>
 *
 * @author zhouq
 * @since 2024-03-24
 */
@Getter
@Setter
@TableName("equipment_status_change")
@ApiModel(value = "EquipmentStatusChange对象", description = "设备状态改变")
public class EquipmentStatusChange implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("是否删除")
    @TableField("is_delete")
    private Integer isDelete;

    @ApiModelProperty("创建时间")
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
      @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("设备id")
    @TableField("equipment_id")
    private Long equipmentId;

    @ApiModelProperty("初始状态")
    @TableField("origin_status_id")
    private Long originStatusId;

    @ApiModelProperty("新状态ID")
    @TableField("new_status_id")
    private Long newStatusId;

    @ApiModelProperty("备注")
    @TableField("msg")
    private String msg;


}

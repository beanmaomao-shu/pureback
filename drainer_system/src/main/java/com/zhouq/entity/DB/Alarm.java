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
 * 报警
 * </p>
 *
 * @author zhouq
 * @since 2024-03-24
 */
@Getter
@Setter
@TableName("alarm")
@ApiModel(value = "Alarm对象", description = "报警")
public class Alarm implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("设备ID")
    @TableField("equipment_id")
    private Long equipmentId;

    @ApiModelProperty("Alarm reason")
    @TableField("alarm_reason")
    private String alarmReason;

    @ApiModelProperty("处理措施")
    @TableField("treatment_measures")
    private String treatmentMeasures;

    @ApiModelProperty("删除时间")
    @TableField("delete_time")
    private LocalDateTime deleteTime;

    @ApiModelProperty("类型ID")
    @TableField("type_id")
    private Long typeId;


}

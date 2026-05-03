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
 * 报警类型
 * </p>
 *
 * @author zhouq
 * @since 2024-03-24
 */
@Getter
@Setter
@TableName("alarm_type")
@ApiModel(value = "AlarmType对象", description = "报警类型")
public class AlarmType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报警名称")
    @TableField("alarm_name")
    private String alarmName;

    @ApiModelProperty("报警等级")
    @TableField("alarm_level")
    private Integer alarmLevel;

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

    @ApiModelProperty("报警项目")
    @TableField("alarm_about")
    private String alarmAbout;


}

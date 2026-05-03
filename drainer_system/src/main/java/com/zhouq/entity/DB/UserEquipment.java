package com.zhouq.entity.DB;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户设备关联表
 * </p>
 *
 * @author zhouq
 * @since 2024-03-24
 */
@Getter
@Setter
@TableName("user_equipment")
@ApiModel(value = "UserEquipment对象", description = "用户设备关联表")
public class UserEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private Long id;

    @ApiModelProperty("User")
    @TableField("user")
    private Long user;

    @TableField("equipment")
    private Long equipment;

      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

      @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("is_delete")
    private Integer isDelete;


}

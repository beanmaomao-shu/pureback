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
 * 系统日志表
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@Getter
@Setter
@TableName("sys_log")
@ApiModel(value = "SysLog对象", description = "系统日志表")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("操作类型")
    @TableField("operation")
    private String operation;

    @ApiModelProperty("操作内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("IP地址")
    @TableField("ip")
    private String ip;

      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}

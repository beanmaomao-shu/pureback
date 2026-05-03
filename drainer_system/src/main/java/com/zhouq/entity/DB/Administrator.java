package com.zhouq.entity.DB;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouq
 * @since 2024-03-24
 */
@Getter
@Setter
@TableName("administrator")
@ApiModel(value = "Administrator对象", description = "")
public class Administrator implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("account")
    private String account;

    @TableField("name")
    private String name;

    @TableField("id")
    private String id;

    @TableField("is_delete")
    private Integer isDelete;

    @TableField("password")
    private String password;

    @TableField("role_id")
    private Integer roleId;


}

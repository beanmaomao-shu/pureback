package com.zhouq.entity.DTO;

import lombok.Data;

/**
 * 用户修改密码传输对象
 */
@Data
public class PasswordUpdateDTO {
    private String oldPassword;
    private String newPassword;
}
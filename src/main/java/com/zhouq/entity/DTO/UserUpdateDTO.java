package com.zhouq.entity.DTO;

import lombok.Data;

/**
 * 用户修改个人信息传输对象
 */
@Data
public class UserUpdateDTO {
    private String realName;
    private String phone;
    private String email;
    private String avatar; // 头像链接（暂时用字符串URL代替）
}
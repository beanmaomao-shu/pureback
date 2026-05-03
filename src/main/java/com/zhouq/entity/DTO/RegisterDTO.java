package com.zhouq.entity.DTO;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String realName;
    private Long roleId; // 1: 系统管理员, 2: 科研人员, 3: 学生, 4: 公众
    private String phone;
    private String email;
}
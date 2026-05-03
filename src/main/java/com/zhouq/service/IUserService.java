package com.zhouq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouq.common.result.Result;
import com.zhouq.entity.DB.User;
import com.zhouq.entity.DTO.LoginDTO;
import com.zhouq.entity.DTO.RegisterDTO;
import com.zhouq.entity.DTO.UserUpdateDTO;
import com.zhouq.entity.DTO.PasswordUpdateDTO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 */
public interface IUserService extends IService<User> {
    Result login(LoginDTO loginDTO);
    Result register(RegisterDTO registerDTO);
    
    // 获取当前登录用户信息
    Result getUserInfo();
    
    // 修改个人信息
    Result updateUserInfo(UserUpdateDTO updateDTO);
    
    // 修改密码
    Result updatePassword(PasswordUpdateDTO passwordDTO);
}

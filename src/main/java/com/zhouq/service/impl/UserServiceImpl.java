package com.zhouq.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouq.common.result.Result;
import com.zhouq.entity.DB.Role;
import com.zhouq.entity.DB.User;
import com.zhouq.entity.DTO.LoginDTO;
import com.zhouq.entity.DTO.RegisterDTO;
import com.zhouq.entity.DTO.UserUpdateDTO;
import com.zhouq.entity.DTO.PasswordUpdateDTO;
import com.zhouq.entity.VO.UserVO;
import com.zhouq.mapper.RoleMapper;
import com.zhouq.mapper.UserMapper;
import com.zhouq.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Result login(LoginDTO loginDTO) {
        // 1. 根据用户名查询用户
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, loginDTO.getUsername()));
        if (user == null) {
            return new Result(400, "账号不存在", false);
        }
        
        // 2. 检查用户状态 (0为禁用/待审核)
        if (user.getStatus() != null && user.getStatus() == 0) {
            return new Result(403, "账号待审核或已被禁用", false);
        }
        
        // 3. BCrypt 密码校验
        boolean passwordMatch = false;
        String inputPassword = loginDTO.getPassword();
        String dbPassword = user.getPassword();
        
        log.info("=== 登录校验调试信息 ===");
        log.info("前端传来的密码(明文): [{}]", inputPassword);
        log.info("数据库查询到的密码(密文): [{}]", dbPassword);

        if (dbPassword != null) {
            try {
                // 使用 BCrypt 进行密文比对
                passwordMatch = BCrypt.checkpw(inputPassword, dbPassword);
            } catch (Exception e) {
                log.error("密码校验异常: ", e);
                return new Result(400, "密码校验失败(格式异常)", false);
            }
        }
        
        if (!passwordMatch) {
            log.warn("=== 密码比对失败 ===");
            return new Result(400, "密码错误", false);
        }

        // 4. Sa-Token 登录签发 Token
        StpUtil.login(user.getId());

        // 5. 封装返回给前端的用户信息
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setRealName(user.getRealName());
        userVO.setAvatar(user.getAvatar());
        userVO.setAccessToken(StpUtil.getTokenValue());
        
        // 为满足前端 vue-pure-admin 的 token 校验机制，需要返回刷新令牌和过期时间
        userVO.setRefreshToken(StpUtil.getTokenValue() + "-refresh");
        long tokenTimeout = StpUtil.getTokenTimeout();
        long expireTime = System.currentTimeMillis() + (tokenTimeout > 0 ? tokenTimeout * 1000 : 1000L * 60 * 60 * 24 * 30);
        userVO.setExpires(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(expireTime)));

        // 6. 查询用户角色编码
        if (user.getRoleId() != null) {
            Role role = roleMapper.selectById(user.getRoleId());
            if (role != null) {
                userVO.setRoles(Collections.singletonList(role.getRoleCode()));
            }
        }

        return Result.success("登录成功", userVO);
    }

    @Override
    public Result register(RegisterDTO registerDTO) {
        // 1. 查重
        long count = this.count(new LambdaQueryWrapper<User>().eq(User::getUsername, registerDTO.getUsername()));
        if (count > 0) {
            return new Result(400, "用户名已被注册", false);
        }

        // 2. 封装数据
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setRealName(registerDTO.getRealName());
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setRoleId(registerDTO.getRoleId());
        
        // 3. BCrypt 密码加密存储
        user.setPassword(BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt()));

        // 4. 业务规则：如果是科研人员(2)或学生(3)，状态默认设为 0 (待审核)
        // 公众(4) 默认状态为 1 (正常，免审核)
        if (registerDTO.getRoleId() != null && (registerDTO.getRoleId() == 2 || registerDTO.getRoleId() == 3)) {
            user.setStatus(0);
        } else {
            user.setStatus(1);
        }

        this.save(user);

        if (user.getStatus() == 0) {
            return Result.success("注册成功，请等待管理员审核");
        }
        return Result.success("注册成功");
    }

    @Override
    public Result getUserInfo() {
        long userId = StpUtil.getLoginIdAsLong();
        User user = this.getById(userId);
        if (user == null) {
            return new Result(400, "用户不存在", false);
        }
        // 敏感信息脱敏
        user.setPassword(null);
        return Result.success("获取成功", user);
    }

    @Override
    public Result updateUserInfo(UserUpdateDTO updateDTO) {
        long userId = StpUtil.getLoginIdAsLong();
        User user = new User();
        user.setId(userId);
        user.setRealName(updateDTO.getRealName());
        user.setPhone(updateDTO.getPhone());
        user.setEmail(updateDTO.getEmail());
        user.setAvatar(updateDTO.getAvatar());
        
        boolean success = this.updateById(user);
        if (success) {
            return Result.success("个人信息更新成功");
        }
        return new Result(500, "更新失败", false);
    }

    @Override
    public Result updatePassword(PasswordUpdateDTO passwordDTO) {
        long userId = StpUtil.getLoginIdAsLong();
        User user = this.getById(userId);
        if (user == null) {
            return new Result(400, "用户不存在", false);
        }

        // 校验旧密码
        if (!BCrypt.checkpw(passwordDTO.getOldPassword(), user.getPassword())) {
            return new Result(400, "原密码错误", false);
        }

        // 更新新密码
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(BCrypt.hashpw(passwordDTO.getNewPassword(), BCrypt.gensalt()));
        this.updateById(updateUser);

        // 修改密码后，强制下线重新登录
        StpUtil.logout();
        return Result.success("密码修改成功，请重新登录");
    }
}

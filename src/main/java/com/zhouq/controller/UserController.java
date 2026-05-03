package com.zhouq.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhouq.common.result.Result;
import com.zhouq.entity.DTO.LoginDTO;
import com.zhouq.entity.DTO.RegisterDTO;
import com.zhouq.entity.DTO.UserUpdateDTO;
import com.zhouq.entity.DTO.PasswordUpdateDTO;
import com.zhouq.entity.DB.User;
import com.zhouq.service.IUserService;
import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户与权限管理 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    @PostMapping("/logout")
    public Result logout() {
        StpUtil.logout();
        return Result.success("退出成功");
    }

    @GetMapping("/info")
    public Result getUserInfo() {
        // 获取当前登录用户的信息
        return userService.getUserInfo();
    }

    @PutMapping("/info")
    public Result updateUserInfo(@RequestBody UserUpdateDTO updateDTO) {
        // 修改当前登录用户的信息（包括头像、姓名等）
        return userService.updateUserInfo(updateDTO);
    }

    @PutMapping("/password")
    public Result updatePassword(@RequestBody PasswordUpdateDTO passwordDTO) {
        // 修改当前登录用户的密码
        return userService.updatePassword(passwordDTO);
    }

    @PostMapping("/refresh")
    public Result refresh() {
        // 简易刷新 token：延长 SaToken 有效期并返回新的过期时间
        StpUtil.renewTimeout(2592000); // 续期 30 天
        com.zhouq.entity.VO.UserVO userVO = new com.zhouq.entity.VO.UserVO();
        userVO.setAccessToken(StpUtil.getTokenValue());
        userVO.setRefreshToken(StpUtil.getTokenValue() + "-refresh");
        long tokenTimeout = StpUtil.getTokenTimeout();
        long expireTime = System.currentTimeMillis() + (tokenTimeout > 0 ? tokenTimeout * 1000 : 1000L * 60 * 60 * 24 * 30);
        userVO.setExpires(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(expireTime)));
        return Result.success("刷新成功", userVO);
    }

    // ==========================================
    // 下方为管理员专用的用户管理接口 (基于角色鉴权)
    // ==========================================

    @GetMapping("/list")
    @SaCheckRole("admin") // 拦截：只有角色编码为 admin 的用户才能调用
    public Result getUserList() {
        return Result.success("获取用户列表成功", userService.list());
    }

    @PutMapping("/role/{userId}")
    @SaCheckRole("admin")
    public Result updateUserRole(@PathVariable Long userId, @RequestParam Long roleId) {
        // 【核心业务防护】: 禁止任何管理员（哪怕是超级管理员）修改 ID 为 1 的系统内置超级管理员的权限
        // 如果系统允许唯一管理员自杀式降级，整个系统将陷入没有管理员的死锁状态。
        if (userId == 1L) {
            return new Result(403, "警告：系统内置超级管理员 (ID=1) 的角色禁止被修改！", false);
        }
        
        // （可选防护）禁止当前登录的管理员修改自己的角色（防止手滑自杀）
        if (userId == StpUtil.getLoginIdAsLong()) {
            return new Result(403, "安全限制：不允许修改自己的角色身份，请联系其他管理员操作", false);
        }

        User user = new User();
        user.setId(userId);
        user.setRoleId(roleId);
        userService.updateById(user);
        
        // 关键修复：修改角色后强制目标用户下线，促使其重新登录并刷新最新权限
        StpUtil.logout(userId);
        
        return Result.success("角色修改成功，已强制该用户下线");
    }

    @PutMapping("/audit/{userId}")
    @SaCheckRole("admin")
    public Result auditUser(@PathVariable Long userId, @RequestParam Integer status) {
        // 同理，保护超级管理员不被禁用
        if (userId == 1L) {
            return new Result(403, "警告：系统内置超级管理员 (ID=1) 禁止被禁用！", false);
        }
        
        if (userId == StpUtil.getLoginIdAsLong() && status == 0) {
            return new Result(403, "安全限制：不允许禁用自己的账号", false);
        }

        User user = new User();
        user.setId(userId);
        user.setStatus(status); // 1-审核通过/正常 0-待审核/禁用
        userService.updateById(user);
        
        if (status == 0) {
             // 如果是禁用操作，立刻把目标踢下线
             StpUtil.logout(userId);
        }
        
        return Result.success("审核/状态更新成功");
    }
}

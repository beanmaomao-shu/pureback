package com.zhouq.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhouq.common.result.Result;
import com.zhouq.entity.DB.SysLog;
import com.zhouq.entity.DB.User;
import com.zhouq.entity.DB.Role;
import com.zhouq.service.ISysLogService;
import com.zhouq.service.IUserService;
import com.zhouq.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@RestController
@RequestMapping("/sysLog")
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @GetMapping("/page")
    @SaCheckRole("admin")
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        Page<SysLog> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<SysLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SysLog::getCreateTime);
        Page<SysLog> sysLogPage = sysLogService.page(pageParam, wrapper);

        // 填充用户信息和角色信息
        if (sysLogPage.getRecords() != null && !sysLogPage.getRecords().isEmpty()) {
            List<Long> userIds = sysLogPage.getRecords().stream().map(SysLog::getUserId).distinct().collect(Collectors.toList());
            if (!userIds.isEmpty()) {
                Map<Long, User> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getId, u -> u));
                Map<Long, String> roleMap = roleService.list().stream().collect(Collectors.toMap(Role::getId, Role::getRoleName));
                
                for (SysLog log : sysLogPage.getRecords()) {
                    User user = userMap.get(log.getUserId());
                    if (user != null) {
                        log.setUsername(user.getUsername());
                        log.setRoleName(roleMap.getOrDefault(user.getRoleId(), "未知角色"));
                    }
                }
            }
        }
        return Result.success("查询成功", sysLogPage);
    }
}

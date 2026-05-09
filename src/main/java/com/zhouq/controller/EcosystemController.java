package com.zhouq.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhouq.common.result.Result;
import com.zhouq.entity.DB.Ecosystem;
import com.zhouq.entity.DB.SysLog;
import com.zhouq.service.IEcosystemService;
import com.zhouq.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 生态系统表 前端控制器
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@RestController
@RequestMapping("/ecosystem")
public class EcosystemController {

    @Autowired
    private IEcosystemService ecosystemService;

    @Autowired
    private ISysLogService sysLogService;

    /**
     * 分页查询生态系统 (所有人可看)
     */
    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) String ecosystemType,
                       @RequestParam(required = false) String seaArea) {
        Page<Ecosystem> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Ecosystem> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Ecosystem::getName, name);
        }
        if (StringUtils.hasText(ecosystemType)) {
            wrapper.eq(Ecosystem::getEcosystemType, ecosystemType);
        }
        if (StringUtils.hasText(seaArea)) {
            wrapper.like(Ecosystem::getSeaArea, seaArea);
        }
        wrapper.orderByDesc(Ecosystem::getCreateTime);
        return Result.success("查询成功", ecosystemService.page(pageParam, wrapper));
    }

    /**
     * 查询所有生态系统列表 (不分页，用于下拉框选择)
     */
    @GetMapping("/list")
    public Result list() {
        return Result.success("查询成功", ecosystemService.list());
    }

    /**
     * 新增生态系统 (仅管理员和科研人员)
     */
    @PostMapping("/add")
    @SaCheckRole(value = {"admin", "researcher"}, mode = SaMode.OR)
    public Result add(@RequestBody Ecosystem ecosystem) {
        ecosystem.setCreateTime(LocalDateTime.now());
        boolean saved = ecosystemService.save(ecosystem);
        if (saved) {
            SysLog sysLog = new SysLog();
            try {
                sysLog.setUserId(StpUtil.getLoginIdAsLong());
            } catch (Exception e) {
                sysLog.setUserId(0L);
            }
            sysLog.setOperation("新增生态系统");
            sysLog.setContent("新增生态系统: [" + ecosystem.getName() + "]");
            sysLogService.save(sysLog);
        }
        return Result.success("生态系统新增成功");
    }

    /**
     * 修改生态系统 (仅管理员和科研人员)
     */
    @PutMapping("/update")
    @SaCheckRole(value = {"admin", "researcher"}, mode = SaMode.OR)
    public Result update(@RequestBody Ecosystem ecosystem) {
        boolean updated = ecosystemService.updateById(ecosystem);
        if (updated) {
            SysLog sysLog = new SysLog();
            try {
                sysLog.setUserId(StpUtil.getLoginIdAsLong());
            } catch (Exception e) {
                sysLog.setUserId(0L);
            }
            sysLog.setOperation("修改生态系统");
            sysLog.setContent("修改生态系统信息: [" + ecosystem.getName() + "]");
            sysLogService.save(sysLog);
        }
        return Result.success("生态系统修改成功");
    }

    /**
     * 删除生态系统 (仅管理员)
     */
    @DeleteMapping("/delete/{id}")
    @SaCheckRole("admin")
    public Result delete(@PathVariable Long id) {
        ecosystemService.removeById(id);
        return Result.success("生态系统删除成功");
    }
}

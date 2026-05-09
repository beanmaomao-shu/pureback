package com.zhouq.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhouq.common.result.Result;
import com.zhouq.entity.DB.Observation;
import com.zhouq.entity.DB.ObservationSpecies;
import com.zhouq.entity.DB.SysLog;
import com.zhouq.entity.DTO.ObservationDTO;
import com.zhouq.service.IObservationService;
import com.zhouq.service.ISysLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 观测记录表 前端控制器
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@RestController
@RequestMapping("/observation")
public class ObservationController {

    @Autowired
    private IObservationService observationService;

    @Autowired
    private ISysLogService sysLogService;

    /**
     * 分页查询观测记录
     */
    @GetMapping("/page")
    @SaCheckPermission(value = {"data:view:all", "data:filter"}, mode = SaMode.OR)
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) Long ecosystemId,
                       @RequestParam(required = false) String observer) {
        Page<Observation> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Observation> wrapper = new LambdaQueryWrapper<>();
        
        if (ecosystemId != null) {
            wrapper.eq(Observation::getEcosystemId, ecosystemId);
        }
        if (StringUtils.hasText(observer)) {
            wrapper.like(Observation::getObserver, observer);
        }
        wrapper.orderByDesc(Observation::getObsTime);
        
        return Result.success("查询成功", observationService.page(pageParam, wrapper));
    }

    /**
     * 获取观测记录详情 (包含关联的物种)
     */
    @GetMapping("/{id}")
    @SaCheckPermission(value = {"data:view:all", "data:filter", "data:filter:basic"}, mode = SaMode.OR)
    public Result getDetail(@PathVariable Long id) {
        Observation observation = observationService.getById(id);
        if (observation == null) {
            return new Result(404, "观测记录不存在", false);
        }
        
        ObservationDTO dto = new ObservationDTO();
        BeanUtils.copyProperties(observation, dto);
        
        // 查出该观测下的物种详情
        List<ObservationSpecies> speciesList = observationService.getSpeciesByObservationId(id);
        dto.setSpeciesList(speciesList);
        
        return Result.success("查询成功", dto);
    }

    /**
     * 新增观测记录 (含物种列表)
     * 仅科研人员和管理员可操作
     */
    @PostMapping("/add")
    @SaCheckRole(value = {"admin", "researcher"}, mode = SaMode.OR)
    public Result add(@RequestBody ObservationDTO dto) {
        Observation observation = new Observation();
        BeanUtils.copyProperties(dto, observation);
        // 记录创建人
        observation.setCreatorId(StpUtil.getLoginIdAsLong());
        
        observationService.saveObservationWithSpecies(observation, dto.getSpeciesList());

        SysLog sysLog = new SysLog();
        try {
            sysLog.setUserId(StpUtil.getLoginIdAsLong());
        } catch (Exception e) {
            sysLog.setUserId(0L);
        }
        sysLog.setOperation("新增观测记录");
        sysLog.setContent("新增观测记录");
        sysLogService.save(sysLog);

        return Result.success("观测记录创建成功");
    }

    /**
     * 修改观测记录 (含物种列表)
     */
    @PutMapping("/update")
    @SaCheckRole(value = {"admin", "researcher"}, mode = SaMode.OR)
    public Result update(@RequestBody ObservationDTO dto) {
        Observation observation = new Observation();
        BeanUtils.copyProperties(dto, observation);
        
        observationService.updateObservationWithSpecies(observation, dto.getSpeciesList());

        SysLog sysLog = new SysLog();
        try {
            sysLog.setUserId(StpUtil.getLoginIdAsLong());
        } catch (Exception e) {
            sysLog.setUserId(0L);
        }
        sysLog.setOperation("修改观测记录");
        sysLog.setContent("修改观测记录 [ID: " + dto.getId() + "]");
        sysLogService.save(sysLog);

        return Result.success("观测记录修改成功");
    }

    /**
     * 删除观测记录
     */
    @DeleteMapping("/delete/{id}")
    @SaCheckRole(value = {"admin", "researcher"}, mode = SaMode.OR)
    public Result delete(@PathVariable Long id) {
        observationService.deleteObservationWithSpecies(id);
        return Result.success("观测记录删除成功");
    }
}

package com.zhouq.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhouq.common.result.Result;
import com.zhouq.entity.DB.Species;
import com.zhouq.entity.DTO.SpeciesQueryDTO;
import com.zhouq.service.ISpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 物种信息表 前端控制器 (RBAC 权限控制演示)
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@RestController
@RequestMapping("/species")
public class SpeciesController {

    @Autowired
    private ISpeciesService speciesService;

    // ==========================================
    // 权限控制（基于角色控制其对数据增删改查的权限）
    // ==========================================

    /**
     * 1. 任何人（包含学生、公众）登录后都可以查看物种列表
     */
    @GetMapping("/list")
    public Result list() {
        return Result.success("查询成功", speciesService.list());
    }

    @PostMapping("/page")
    public Result page(@RequestBody SpeciesQueryDTO queryDTO) {
        Page<Species> pageParam = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<Species> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getChineseName())) {
            wrapper.like(Species::getChineseName, queryDTO.getChineseName());
        }
        if (StringUtils.hasText(queryDTO.getPhylum())) {
            wrapper.like(Species::getPhylum, queryDTO.getPhylum());
        }
        if (StringUtils.hasText(queryDTO.getProtectLevel())) {
            wrapper.eq(Species::getProtectLevel, queryDTO.getProtectLevel());
        }
        if (StringUtils.hasText(queryDTO.getEndangeredStatus())) {
            wrapper.eq(Species::getEndangeredStatus, queryDTO.getEndangeredStatus());
        }
        wrapper.orderByDesc(Species::getId);
        return Result.success("查询成功", speciesService.page(pageParam, wrapper));
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Species species = speciesService.getById(id);
        if (species == null) {
            return new Result(404, "物种不存在", false);
        }
        return Result.success("查询成功", species);
    }

    /**
     * 2. 只有【管理员】和【科研人员】有权限新增物种
     * mode = SaMode.OR 表示只要满足其中一个角色即可放行
     */
    @PostMapping("/add")
    @SaCheckRole(value = {"admin", "researcher"}, mode = SaMode.OR)
    public Result add(@RequestBody Species species) {
        boolean saved = speciesService.save(species);
        return saved ? Result.success("物种新增成功") : new Result(500, "物种新增失败", false);
    }

    /**
     * 3. 只有【管理员】和【科研人员】有权限修改物种信息
     */
    @PutMapping("/update")
    @SaCheckRole(value = {"admin", "researcher"}, mode = SaMode.OR)
    public Result update(@RequestBody Species species) {
        boolean updated = speciesService.updateById(species);
        return updated ? Result.success("物种修改成功") : new Result(500, "物种修改失败", false);
    }

    /**
     * 4. 只有【管理员】有权限删除物种数据
     * 极其敏感的操作，科研人员也无权删除
     */
    @DeleteMapping("/delete/{id}")
    @SaCheckRole("admin")
    public Result delete(@PathVariable Long id) {
        speciesService.removeById(id);
        return Result.success("物种删除成功");
    }

    @PostMapping("/seed")
    @SaCheckRole(value = {"admin", "researcher"}, mode = SaMode.OR)
    public Result seed() {
        List<Species> demoList = Arrays.asList(
                buildSpecies(2001L, "中华白海豚", "Sousa chinensis", "脊索动物门", "哺乳纲", "鲸偶蹄目", "海豚科", "Sousa", "chinensis", "体色灰白至粉白，吻部细长", "近岸活动，常小群出现", "珠江口、雷州湾、北部湾沿海", new BigDecimal("110.420500"), new BigDecimal("20.045300"), "国家一级", "易危"),
                buildSpecies(2002L, "鹿角珊瑚", "Acropora formosa", "刺胞动物门", "珊瑚纲", "石珊瑚目", "鹿角珊瑚科", "Acropora", "formosa", "分枝明显，外形似鹿角", "依赖透明海水和稳定光照", "海南三亚近岸珊瑚礁区", new BigDecimal("109.512300"), new BigDecimal("18.238600"), "无危", "近危"),
                buildSpecies(2003L, "鳗草", "Zostera marina", "被子植物门", "单子叶植物纲", "泽泻目", "鳗草科", "Zostera", "marina", "叶片带状，浅海成片生长", "适应浅海弱浪环境", "厦门湾、胶州湾海草床区域", new BigDecimal("118.150200"), new BigDecimal("24.432100"), "无危", "无危"),
                buildSpecies(2004L, "弹涂鱼", "Periophthalmus cantonensis", "脊索动物门", "辐鳍鱼纲", "鲈形目", "虾虎鱼科", "Periophthalmus", "cantonensis", "可短时离水活动，胸鳍发达", "常见于潮滩泥面觅食", "雷州湾红树林潮滩区域", new BigDecimal("110.348900"), new BigDecimal("20.029800"), "无危", "无危")
        );
        boolean saved = speciesService.saveOrUpdateBatch(demoList);
        return saved ? Result.success("物种测试数据初始化成功") : new Result(500, "物种测试数据初始化失败", false);
    }

    private Species buildSpecies(Long id, String chineseName, String latinName, String phylum, String klass,
                                 String order, String family, String genus, String speciesName, String features,
                                 String habits, String distribution, BigDecimal longitude, BigDecimal latitude,
                                 String protectLevel, String endangeredStatus) {
        Species species = new Species();
        species.setId(id);
        species.setChineseName(chineseName);
        species.setLatinName(latinName);
        species.setPhylum(phylum);
        species.setKlass(klass);
        species.setOrder(order);
        species.setFamily(family);
        species.setGenus(genus);
        species.setSpecies(speciesName);
        species.setFeatures(features);
        species.setHabits(habits);
        species.setDistribution(distribution);
        species.setLongitude(longitude);
        species.setLatitude(latitude);
        species.setProtectLevel(protectLevel);
        species.setEndangeredStatus(endangeredStatus);
        return species;
    }
}
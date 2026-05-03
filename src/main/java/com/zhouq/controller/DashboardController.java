package com.zhouq.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhouq.common.result.Result;
import com.zhouq.entity.DB.Observation;
import com.zhouq.entity.DB.Species;
import com.zhouq.entity.DB.Ecosystem;
import com.zhouq.service.IEcosystemService;
import com.zhouq.service.IObservationService;
import com.zhouq.service.ISpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ISpeciesService speciesService;

    @Autowired
    private IObservationService observationService;

    @Autowired
    private IEcosystemService ecosystemService;

    @GetMapping("/overview")
    public Result overview() {
        Map<String, Object> data = new HashMap<>();
        // 1. 系统概览数据
        data.put("totalSpecies", speciesService.count());
        data.put("totalObservations", observationService.count());
        data.put("totalEcosystems", ecosystemService.count());

        // 2. 物种保护等级占比统计 (饼图)
        List<Map<String, Object>> protectLevelStats = speciesService.listMaps(
            new QueryWrapper<Species>().select("protect_level as name", "count(*) as value").groupBy("protect_level")
        );
        for (Map<String, Object> map : protectLevelStats) {
            if (map.get("name") == null || map.get("name").toString().trim().isEmpty()) {
                map.put("name", "未定级");
            }
        }
        data.put("protectLevelStats", protectLevelStats);

        // 3. 各生态系统观测次数统计 (柱状图)
        List<Map<String, Object>> ecosystemStats = observationService.listMaps(
            new QueryWrapper<Observation>().select("ecosystem_id as ecosystemId", "count(*) as value").groupBy("ecosystem_id")
        );
        // 获取所有生态系统名字，将 ID 转为真实的文字名称
        Map<Long, String> ecoMap = ecosystemService.list().stream()
                .collect(Collectors.toMap(Ecosystem::getId, Ecosystem::getName));
        for (Map<String, Object> stat : ecosystemStats) {
            Long ecoId = Long.valueOf(stat.get("ecosystemId").toString());
            stat.put("name", ecoMap.getOrDefault(ecoId, "未知生态系统"));
        }
        data.put("ecosystemStats", ecosystemStats);

        // 4. 各分类单元（门）物种数量占比
        List<Map<String, Object>> phylumStats = speciesService.listMaps(
            new QueryWrapper<Species>().select("phylum as name", "count(*) as value").groupBy("phylum")
        );
        for (Map<String, Object> map : phylumStats) {
            if (map.get("name") == null || map.get("name").toString().trim().isEmpty()) {
                map.put("name", "未分类");
            }
        }
        data.put("phylumStats", phylumStats);

        // 5. 按人员的观测次数统计
        List<Map<String, Object>> observerStats = observationService.listMaps(
            new QueryWrapper<Observation>().select("observer as name", "count(*) as value").groupBy("observer")
        );
        for (Map<String, Object> map : observerStats) {
            if (map.get("name") == null || map.get("name").toString().trim().isEmpty()) {
                map.put("name", "未知人员");
            }
        }
        data.put("observerStats", observerStats);

        // 6. 按时间的观测次数统计 (按月)
        List<Map<String, Object>> timeStats = observationService.listMaps(
            new QueryWrapper<Observation>().select("DATE_FORMAT(obs_time, '%Y-%m') as name", "count(*) as value").groupBy("DATE_FORMAT(obs_time, '%Y-%m')").orderByAsc("name")
        );
        for (Map<String, Object> map : timeStats) {
            if (map.get("name") == null || map.get("name").toString().trim().isEmpty()) {
                map.put("name", "未知时间");
            }
        }
        data.put("timeStats", timeStats);

        return Result.success("查询成功", data);
    }
}
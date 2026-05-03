package com.zhouq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhouq.entity.DB.Observation;
import com.zhouq.entity.DB.ObservationSpecies;
import com.zhouq.mapper.ObservationMapper;
import com.zhouq.service.IObservationService;
import com.zhouq.service.IObservationSpeciesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 观测记录表 服务实现类
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@Service
public class ObservationServiceImpl extends ServiceImpl<ObservationMapper, Observation> implements IObservationService {

    @Autowired
    private IObservationSpeciesService observationSpeciesService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveObservationWithSpecies(Observation observation, List<ObservationSpecies> speciesList) {
        // 1. 保存主表 (拿到生成的 ID)
        this.save(observation);
        
        // 2. 关联从表
        if (speciesList != null && !speciesList.isEmpty()) {
            for (ObservationSpecies os : speciesList) {
                os.setObservationId(observation.getId());
            }
            observationSpeciesService.saveBatch(speciesList);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateObservationWithSpecies(Observation observation, List<ObservationSpecies> speciesList) {
        // 1. 更新主表
        this.updateById(observation);
        
        // 2. 删除原有的物种关联
        observationSpeciesService.remove(
            new LambdaQueryWrapper<ObservationSpecies>().eq(ObservationSpecies::getObservationId, observation.getId())
        );
        
        // 3. 重新插入最新的物种关联
        if (speciesList != null && !speciesList.isEmpty()) {
            for (ObservationSpecies os : speciesList) {
                os.setObservationId(observation.getId());
            }
            observationSpeciesService.saveBatch(speciesList);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteObservationWithSpecies(Long id) {
        // 1. 删从表
        observationSpeciesService.remove(
            new LambdaQueryWrapper<ObservationSpecies>().eq(ObservationSpecies::getObservationId, id)
        );
        // 2. 删主表
        return this.removeById(id);
    }

    @Override
    public List<ObservationSpecies> getSpeciesByObservationId(Long id) {
        return observationSpeciesService.list(
            new LambdaQueryWrapper<ObservationSpecies>().eq(ObservationSpecies::getObservationId, id)
        );
    }
}

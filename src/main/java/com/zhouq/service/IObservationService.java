package com.zhouq.service;

import com.zhouq.entity.DB.Observation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouq.entity.DB.ObservationSpecies;
import java.util.List;

/**
 * <p>
 * 观测记录表 服务类
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
public interface IObservationService extends IService<Observation> {
    
    // 保存观测记录并关联物种
    boolean saveObservationWithSpecies(Observation observation, List<ObservationSpecies> speciesList);
    
    // 更新观测记录及关联物种
    boolean updateObservationWithSpecies(Observation observation, List<ObservationSpecies> speciesList);
    
    // 删除观测记录及关联物种
    boolean deleteObservationWithSpecies(Long id);
    
    // 获取观测记录的物种详情
    List<ObservationSpecies> getSpeciesByObservationId(Long id);
}

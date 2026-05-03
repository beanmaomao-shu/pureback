package com.zhouq.service.impl;

import com.zhouq.entity.DB.Species;
import com.zhouq.mapper.SpeciesMapper;
import com.zhouq.service.ISpeciesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 物种信息表 服务实现类
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@Service
public class SpeciesServiceImpl extends ServiceImpl<SpeciesMapper, Species> implements ISpeciesService {

}

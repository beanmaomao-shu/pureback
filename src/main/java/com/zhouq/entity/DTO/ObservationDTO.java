package com.zhouq.entity.DTO;

import com.zhouq.entity.DB.ObservationSpecies;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 观测记录（含关联物种）数据传输对象
 */
@Data
public class ObservationDTO {
    private Long id;
    private LocalDateTime obsTime;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Long ecosystemId;
    private String observer;
    private BigDecimal waterTemp;
    private BigDecimal salinity;
    private String remark;
    
    // 核心联动点：本次观测关联的物种列表及其数量、行为
    private List<ObservationSpecies> speciesList;
}
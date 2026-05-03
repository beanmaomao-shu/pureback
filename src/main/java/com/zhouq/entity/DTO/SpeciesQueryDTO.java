package com.zhouq.entity.DTO;

import lombok.Data;

/**
 * 物种信息多条件分页查询参数
 */
@Data
public class SpeciesQueryDTO {
    private Integer page = 1;      // 当前页码
    private Integer size = 10;     // 每页条数
    
    private String chineseName;    // 中文名 (支持模糊查询)
    private String phylum;         // 门 (精确查询)
    private String protectLevel;   // 保护等级 (精确查询)
    private String endangeredStatus; // 濒危状态 (精确查询)
}
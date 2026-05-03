package com.zhouq.mapper;

import com.zhouq.entity.DB.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 管理员 Mapper 接口
 * </p>
 *
 * @author 计算机科学系 周启俊
 * @since 2023-03-12
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

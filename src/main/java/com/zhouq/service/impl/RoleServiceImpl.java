package com.zhouq.service.impl;

import com.zhouq.entity.DB.Role;
import com.zhouq.mapper.RoleMapper;
import com.zhouq.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  角色表 服务实现类
 * </p>
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}

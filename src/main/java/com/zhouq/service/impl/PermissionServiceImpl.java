package com.zhouq.service.impl;

import com.zhouq.entity.DB.Permission;
import com.zhouq.mapper.PermissionMapper;
import com.zhouq.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author sweng-vision
 * @since 2026-04-03
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}

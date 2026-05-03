package com.zhouq.common.myConfig;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhouq.entity.DB.Permission;
import com.zhouq.entity.DB.Role;
import com.zhouq.entity.DB.RolePermission;
import com.zhouq.entity.DB.User;
import com.zhouq.mapper.PermissionMapper;
import com.zhouq.mapper.RoleMapper;
import com.zhouq.mapper.RolePermissionMapper;
import com.zhouq.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义 Sa-Token 权限认证接口扩展
 * 保证使用 @SaCheckRole 和 @SaCheckPermission 能够获取到正确的数据库角色与权限
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permissionList = new ArrayList<>();
        
        // 1. 查询当前用户信息，获取角色ID
        User user = userMapper.selectById(Long.valueOf(loginId.toString()));
        if (user != null && user.getRoleId() != null) {
            // 2. 根据角色ID，查询角色-权限中间表
            List<RolePermission> rps = rolePermissionMapper.selectList(
                    new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, user.getRoleId())
            );
            if (!rps.isEmpty()) {
                // 3. 根据权限ID集合，查询具体的权限编码
                List<Long> permIds = rps.stream().map(RolePermission::getPermId).collect(Collectors.toList());
                List<Permission> perms = permissionMapper.selectBatchIds(permIds);
                permissionList = perms.stream().map(Permission::getPermCode).collect(Collectors.toList());
            }
        }
        return permissionList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roleList = new ArrayList<>();
        
        // 1. 查询当前用户信息，获取角色ID
        User user = userMapper.selectById(Long.valueOf(loginId.toString()));
        if (user != null && user.getRoleId() != null) {
            // 2. 根据角色ID查询角色表，获取角色编码
            Role role = roleMapper.selectById(user.getRoleId());
            if (role != null) {
                roleList.add(role.getRoleCode());
            }
        }
        return roleList;
    }
}
package com.zhouq.service.impl;

import com.zhouq.entity.DB.User;
import com.zhouq.mapper.UserMapper;
import com.zhouq.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员 服务实现类
 * </p>
 *
 * @author zhouq
 * @since 2024-03-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

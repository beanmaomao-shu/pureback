package com.zhouq.entity.VO;

import lombok.Data;
import java.util.List;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String realName;
    private String avatar;
    private List<String> roles; // 角色编码列表，前端通过这个控制菜单显示
    private String accessToken; // 登录令牌 (Sa-Token)
    private String refreshToken; // 刷新令牌
    private String expires; // accessToken的过期时间（格式'yyyy/MM/dd HH:mm:ss'）
}
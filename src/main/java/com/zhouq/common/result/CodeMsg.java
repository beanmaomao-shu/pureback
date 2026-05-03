package com.zhouq.common.result;

import lombok.Data;

/**
 * <p>
 * 错误代码和消息
 * </p>
 *
 * @author 计算机系 周启俊
 * @since 2023/1/7 21:17
 */
@Data
public class CodeMsg {
    private String msg;
    private int code;

    public static CodeMsg SUCCESS = new CodeMsg("Success",200);
    public static CodeMsg SERVER_ERROR = new CodeMsg("服务器异常",50000);
    public static CodeMsg DUPLICATE_KEY = new CodeMsg("键名重复",50001);
    public static CodeMsg NO_PERMISSION = new CodeMsg("无权操作",50002);
    public static CodeMsg NO_ROLE = new CodeMsg("无权操作",50003);

    public static CodeMsg VERIFICATION_CODE_EXPIRED = new CodeMsg("验证码失效",50004);

    //用户 501xx
    public static CodeMsg USER_PASSWORD_ERROR = new CodeMsg("账号或密码错误",50101);
    public static CodeMsg USER_CANT_USE = new CodeMsg("账号禁用",50101);
    public static CodeMsg USER_NOT_LOGIN = new CodeMsg("未登录",50102);
    public static CodeMsg USER_IS_EXISTENCE = new CodeMsg("用户已存在",50103);
    public CodeMsg(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
}

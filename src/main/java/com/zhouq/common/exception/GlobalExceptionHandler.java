package com.zhouq.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.zhouq.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 拦截 Sa-Token 未登录异常
    @ExceptionHandler(NotLoginException.class)
    public Result handlerNotLoginException(NotLoginException e) {
        log.warn("用户未登录或登录已过期: {}", e.getMessage());
        // 返回401前端拦截器通常会自动跳转登录页
        return new Result(401, "请重新登录", false);
    }

    // 拦截 Sa-Token 角色权限异常
    @ExceptionHandler(NotRoleException.class)
    public Result handlerNotRoleException(NotRoleException e) {
        log.warn("用户缺少角色: {}", e.getRole());
        return new Result(403, "无此角色权限，禁止访问", false);
    }

    // 拦截 Sa-Token 操作权限异常
    @ExceptionHandler(NotPermissionException.class)
    public Result handlerNotPermissionException(NotPermissionException e) {
        log.warn("用户缺少权限: {}", e.getPermission());
        return new Result(403, "无此操作权限，禁止访问", false);
    }

    // 拦截自定义的 GlobalException
    @ExceptionHandler(GlobalException.class)
    public Result handlerGlobalException(GlobalException e) {
        return Result.error(e.getCodeMsg());
    }

    // 拦截其他未知异常
    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e) {
        log.error("系统异常: ", e);
        return new Result(500, "服务器内部异常: " + e.getMessage(), false);
    }
}
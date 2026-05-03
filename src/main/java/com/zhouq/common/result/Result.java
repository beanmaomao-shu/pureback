package com.zhouq.common.result;

import lombok.Data;

import java.util.ArrayList;

/**
 * <p>
 *返回数据统一
 * </p>
 *
 * @author 计算机系 周启俊
 * @since 2023/1/7 21:17
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private boolean success;
    private T data;

    public static <T> Result<T> success(String msg,T data){
        return new Result<>(200,msg,data,true);
    }
    public static <T> Result<T> success(String msg){
        return new Result<>(200,msg,true);
    }
    public static <T> Result<T> success(T data){
        return new Result<>(200,"success",data,true);
    }

    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<>(codeMsg);
    }

    public Result(CodeMsg codeMsg){
        this(codeMsg.getCode(),codeMsg.getMsg(),false);
        this.setData((T) "null");
    }

    public Result(int code,String msg,boolean success){
        this.code = code;
        this.msg = msg;
        this.success = success;
    }

    public Result(int code, String msg, T data,boolean success) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.success = success;
    }
}

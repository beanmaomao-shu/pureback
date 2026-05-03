package com.zhouq.common.exception;

import com.zhouq.common.result.CodeMsg;
import lombok.Getter;

/**
 * <p>
 *自定义全局异常
 * </p>
 *
 * @author 计算机系 周启俊
 * @since 2023/1/8 1:14
 */

@Getter
public class GlobalException extends RuntimeException{
    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());//这里是干嘛的？？
        this.codeMsg = codeMsg;
    }
}

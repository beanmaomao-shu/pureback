package com.zhouq.common.Utils;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 计算机系 周启俊
 * @since 2023/1/23 3:36
 */

@Data
public class RedisData<T> {

    private T data;
    private Class type;
}

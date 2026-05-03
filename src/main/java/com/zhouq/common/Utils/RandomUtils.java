package com.zhouq.common.Utils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 自定义工具类，各种随机生成
 * </p>
 *
 * @author 计算机系 周启俊
 * @since 2023/1/16 0:09
 */

public class RandomUtils {

    public static String generateValidateCode(Integer length) {
        Random random = new Random();
        String code = random.ints(length,0,10)
                .mapToObj(String::valueOf)
                .reduce("", (origin, string) -> origin + string);
        return code;
    }
}

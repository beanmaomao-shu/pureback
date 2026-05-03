package com.zhouq.common.Utils;

import org.junit.jupiter.api.Test;


class AsciiUtilsTest {
    @Test
    void asciiToString() {
        System.out.println(AsciiUtils.asciiToString("31312e3334322c31322e3433322c32382e303833352c302e31303036382c2d312e3639322c32392e363537302c492c312e303034332c422c32332e352c432c3235342e312c5220312e302c3030312c2d33323736382c2d33323736382c2d33323738382c2d33323736"));
    }

    @Test
    void testAsciiToString() {
        System.out.println(AsciiUtils.stringToAscii("1,2,3,4,5,6,7,8,9,10,11,12,13,14"));
    }
}
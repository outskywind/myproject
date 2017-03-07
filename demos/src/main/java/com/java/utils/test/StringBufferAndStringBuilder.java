package com.java.utils.test;

import org.junit.Test;

public class StringBufferAndStringBuilder {

    /**
     * fuck StringBuffer 是带 synchronized 锁。
     *但是因为jvm有逃逸分析，因此长时间运行的话，JIT编译器优化的时候会消除锁。
     */
    StringBuffer sb = new StringBuffer();

    @Test
    public static void main(String[] args) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
    }



}

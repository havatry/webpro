package com.prop.test;

/**
 * 测试重构if的一些记录
 * Created at 2020/4/2
 */
public class Test {
    String a;

    public void doTest() {
        if (init()) {
            if (!restore()) {
                System.out.println("Clean");
            }
        } else {
            System.out.println("Clean");
        }
        System.out.println(a);
    }

    public void doTest2() {
        if ((init() && !restore()) || !init()) {
            System.out.println("Clean");
        }
        System.out.println(a);
    }

    public static void main(String[] args) {
        new Test().doTest();
        new Test().doTest2();
    }

    private boolean init() {
        a = null;
        return true;
    }

    private boolean restore() {
        a = "Test";
        return true;
    }
}

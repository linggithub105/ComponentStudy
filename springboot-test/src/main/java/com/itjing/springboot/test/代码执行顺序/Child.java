package com.itjing.springboot.test.代码执行顺序;

/**
 * @author lijing
 * @date 2022年07月28日 16:05
 * @description
 */
public class Child extends Parent {

    static {
        System.out.println("Child static initial block");
    }

    {
        System.out.println("Child initial block");
    }

    private Hobby hobby = new Hobby();

    public Child() {
        System.out.println("Child constructor block");
    }

    public static void main(String[] args) {
        new Child();
    }
}

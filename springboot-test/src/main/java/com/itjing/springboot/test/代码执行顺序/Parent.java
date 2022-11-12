package com.itjing.springboot.test.代码执行顺序;

/**
 * @author lijing
 * @date 2022年07月28日 16:04
 * @description
 */
public class Parent {
    static {
        System.out.println("Parent static initial block");
    }

    {
        System.out.println("Parent initial block");
    }

    public Parent() {
        System.out.println("Parent constructor block");

    }
}
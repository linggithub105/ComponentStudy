package com.itjing.springboot.test.代码执行顺序;

/**
 * @author lijing
 * @date 2022年07月28日 16:04
 * @description
 */
public class Hobby {

    static{
        System.out.println("Hobby static initial block");
    }

    public Hobby() {
        System.out.println("hobby constructor block");
    }

}

package com.itjing.thread.stampedlock;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-10-08 10:07
 */
public class Balance {

    private Integer amount;

    public Balance() {
    }

    public Balance(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

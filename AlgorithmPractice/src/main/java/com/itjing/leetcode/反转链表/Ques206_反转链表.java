package com.itjing.leetcode.反转链表;

/**
 * @author: lijing
 * @Date: 2021年09月08日 10:40
 * @Description:
 * 题目地址： https://leetcode-cn.com/problems/reverse-linked-list/
 * 参考： https://labuladong.gitee.io/algo/2/18/17/
 */
public class Ques206_反转链表 {

    static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    /**
     * 递归反转整个链表
     * @param head
     * @return
     */
    public static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode last = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return last;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);

//        ListNode head = null;

        ListNode revHead = reverseList(head);
        for (ListNode p = revHead; p != null; p = p.next) {
            System.out.print(p.val + " ");
        }
    }
}

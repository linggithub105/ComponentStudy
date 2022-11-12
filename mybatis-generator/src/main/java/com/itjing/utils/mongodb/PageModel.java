package com.itjing.utils.mongodb;

import java.io.Serializable;
import java.util.List;

/**
 * @author lijing
 * @date 2022年06月19日 17:05
 * @description 分页实体
 */
public class PageModel<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 当前页
     */
    private Integer pageNo = 1;
    /**
     * 当前页条数
     */
    private Integer pagesize = 10;
    /**
     * 总共的条数
     */
    private Long total;
    /**
     * 总共的页数
     */
    private Integer pages;
    /**
     * 实体类集合
     */
    private List<T> list;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public Long getTotal() {
        return total;
    }

    /**
     * 总数由0开始计数
     *
     * @param total
     */
    public void setTotal(Long total) {
        this.total = total + 1;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public PageModel(Integer pageNo, Integer pagesize, Long total, Integer pages, List<T> list) {
        this.pageNo = pageNo;
        this.pagesize = pagesize;
        this.total = total;
        this.pages = pages;
        this.list = list;
    }

    public PageModel() {
    }

    @Override
    public String toString() {
        return "PageModel{" +
                "pageNo=" + pageNo +
                ", pagesize=" + pagesize +
                ", total=" + total +
                ", pages=" + pages +
                ", list=" + list +
                '}';
    }
}
package com.ujiuye.utils.util;

import java.io.Serializable;

/**
 * 封装查询条件
 */
public class QueryPageBean implements Serializable {
    private Integer currentPage;//当前所在页码
    private Integer pageSize;//每页记录数
    private String queryString;//查询条件
    private String sex;//性别筛选条件
    private String age;//年龄筛选条件
    private Integer reg;//注册日期筛选条件

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getReg() {
        return reg;
    }

    public void setReg(Integer reg) {
        this.reg = reg;
    }
}
package com.e_commerce.miscroservice.commons.entity.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class BasePage {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private Integer pageNum;
    private Integer pageSize;
    private Integer total;

    public BasePage() {
    }

    public static void setPageParams(Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
    }

    public Integer getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        if (pageNum > 0) {
            this.pageNum = pageNum;
            if (this.pageSize <= 0) {
                this.pageSize = 10;
            }

            PageHelper.startPage(this.pageNum, this.pageSize);
        }
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
            if (this.pageNum > 0) {
                PageHelper.startPage(this.pageNum, this.pageSize);
            }

        }
    }

    public static void setPage(Integer pageNum) {
        if (pageNum != null && pageNum > 0) {
            PageHelper.startPage(pageNum, 10);
        }
    }

    public <T> Integer getTotal(List<T> list) {
        if (list != null && !list.isEmpty()) {
            PageInfo<T> pageInfo = new PageInfo(list);
            if (pageInfo != null) {
                this.total = (int)pageInfo.getTotal();
            }

            return this.total;
        } else {
            return this.total;
        }
    }
}

package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzSubject;

import java.util.List;

public interface GZSubjectDao {

    List<TGzSubject> selectByAvailableStatus(Integer availableStatus);

    TGzSubject selectByPrimaryKey(Long subjectId);

    int updateByPrimaryKey(TGzSubject subject);
}

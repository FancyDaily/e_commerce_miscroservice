package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TTypeRecord;

import java.util.List;

public interface TypeRecordDao {

    List<TTypeRecord> selectByUserIdBetween(Long id, Long begin, Long end);
}

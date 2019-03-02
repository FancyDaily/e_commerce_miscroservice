package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.service.system.DataDictionaryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 21:01
 * @Copyright 玖远网络
 */
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService{

    private Log logger = Log.getInstance(DataDictionaryServiceImpl.class);


    /**
     * 通过groupCode和comment查找
     *
     * @param groupCode groupCode
     * @param comment comment
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary>
     * @author Charlie
     * @date 2018/9/30 11:46
     */
    @Override
    public List<DataDictionary> getByGroupAndLikeComment(String groupCode, String comment) {
        return MybatisOperaterUtil.getInstance ().finAll (
                new DataDictionary (),
                new MybatisSqlWhereBuild (DataDictionary.class)
                        .eq (DataDictionary::getGroupCode, groupCode)
                        .like (DataDictionary::getComment, comment)
        );
    }



    /**
     * 根据groupCode, code查找
     *
     * @param code code
     * @param groupCode groupCode
     * @return com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary
     * @author Charlie
     * @date 2018/10/11 10:02
     */
    @Override
    public DataDictionary findByCodeAndGroupCode(String code, String groupCode) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new DataDictionary (),
                new MybatisSqlWhereBuild (DataDictionary.class)
                        .eq (DataDictionary::getCode, code)
                        .eq (DataDictionary::getGroupCode, groupCode)
        );
    }
}

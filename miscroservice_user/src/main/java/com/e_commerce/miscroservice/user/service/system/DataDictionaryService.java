package com.e_commerce.miscroservice.user.service.system;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 21:01
 * @Copyright 玖远网络
 */
public interface DataDictionaryService{



    /**
     * 通过groupCode和comment查找
     *
     * @param groupCode groupCode
     * @param comment comment
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary>
     * @author Charlie
     * @date 2018/9/30 11:46
     */
    List<DataDictionary> getByGroupAndLikeComment(String groupCode, String comment);




    /**
     * 根据groupCode, code查找
     *
     * @param code code
     * @param groupCode groupCode
     * @return com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary
     * @author Charlie
     * @date 2018/10/11 10:02
     */
    DataDictionary findByCodeAndGroupCode(String code, String groupCode);



}

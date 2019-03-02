package com.e_commerce.miscroservice.user.controller.rpc;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.user.service.system.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/11 9:59
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "system/rpc/dataDictionaryRpcController" )
public class DataDictionaryRpcController{

    @Autowired
    private DataDictionaryService dataDictionaryService;


    @RequestMapping( "findByCodeAndGroupCode" )
    public DataDictionary findByCodeAndGroupCode(String code, String groupCode) {
        return dataDictionaryService.findByCodeAndGroupCode (code, groupCode);
    }

}

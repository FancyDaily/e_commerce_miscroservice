package com.e_commerce.miscroservice.sdx_proj.controller;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookTicktService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* T预定书券的操作接口
*/
@RestController
@RequestMapping("tSdxBookTickt")
public class TSdxBookTicktController {
    @Autowired
    private SdxBookTicktService sdxBookTicktService;
}

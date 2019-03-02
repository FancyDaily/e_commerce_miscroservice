package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.user.controller.rpc.StoreBusinessAccountRpcController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Testaa {

    @Autowired
    private StoreBusinessAccountRpcController controller;

    @GetMapping("get")
    public Response get() {
        return Response.success(controller.selectStoreBusinessAccountLog(282L
                , 1));

    }

}

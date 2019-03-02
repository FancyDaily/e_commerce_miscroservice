package com.e_commerce.miscroservice.user.rpc;

import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/7 16:43
 * @Copyright 玖远网络
 */
@FeignClient(value = "PUBLICACCOUNT", path = "rpc/publicAccountUser")
public interface PublicAccountUserRpcService{


    @PostMapping("updateByPhone")
    PublicAccountUser updateByPhone(@RequestBody PublicAccountUser publicAccountUser);

}

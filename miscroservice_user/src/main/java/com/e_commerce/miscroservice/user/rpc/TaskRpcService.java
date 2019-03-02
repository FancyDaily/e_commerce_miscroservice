package com.e_commerce.miscroservice.user.rpc;

import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author hyf
 * @Date 2018/12/21 10:57
 */
@FeignClient(value = "TASK",path = "/task/rpc/taskRpcController")
public interface TaskRpcService {

    /**
     * 处理分发中心入口
     * @param dataBase
     * @return
     */
    @RequestMapping("/dispose/distribute")
    Response.ResponseData disposeDistribute(@RequestBody DataBase dataBase);
}

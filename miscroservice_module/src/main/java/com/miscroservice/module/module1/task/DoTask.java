package com.miscroservice.module.module1.task;


import com.alipay.jarslink.api.Action;
import com.miscroservice.module.module1.entity.RedisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.miscroservice.module.util.SqlExecUtil.execSql;

@Component
public class DoTask implements Action<String, Object> {


    @Autowired
    private RedisTest redis;


    String host = "xx";

    @Override
    public Object execute(String actionRequest) {
        System.out.println("params:" + actionRequest);
        System.out.println("redis.host" + host);
        System.out.println(redis);

        Map<String, Object> stringObjectMap = execSql("select  u.user_id AS a,u.type_id as b  from  user as u limit 1,20", "https://www.etongjin.net/activity/exec");
        System.out.println(stringObjectMap.get("data"));
        System.out.println(stringObjectMap.get("row"));

        return stringObjectMap.get("data");
    }

    @Override
    public String getActionName() {
        return "module1";
    }

}

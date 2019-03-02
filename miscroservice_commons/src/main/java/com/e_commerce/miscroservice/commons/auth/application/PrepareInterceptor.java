package com.e_commerce.miscroservice.commons.auth.application;

import com.e_commerce.miscroservice.commons.helper.plug.multidatasource.MultiDataSourceHepler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@Component
public class PrepareInterceptor implements Interceptor {

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("...................开始.............");

        MultiDataSourceHepler.Lock.lock();
        return invocation.proceed();
    }

}

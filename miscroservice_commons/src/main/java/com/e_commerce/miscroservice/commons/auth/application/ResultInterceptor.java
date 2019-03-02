package com.e_commerce.miscroservice.commons.auth.application;

import com.e_commerce.miscroservice.commons.helper.plug.multidatasource.MultiDataSourceHepler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.Properties;

@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
@Component
public class ResultInterceptor implements Interceptor {

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("...................结束.............");
        MultiDataSourceHepler.Lock.unlock();
        return invocation.proceed();
    }
}
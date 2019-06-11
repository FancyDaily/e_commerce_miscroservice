package com.e_commerce.miscroservice.commons.config;

import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.config.MybatisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * mybatisv插件的配置
 */
@Configuration
public class MybatisPlugConfig {

    @Bean
    public MybatisConfig createMybatisConfig() {
        MybatisConfig mybatisConfig = new MybatisConfig();
        mybatisConfig.setIsCamel(Boolean.TRUE);
        return mybatisConfig;
    }


    @Bean(initMethod = "init")
    public DbHandler createDbHandler(DataSource dataSource) {


        DbHandler dbHandler = new DbHandler();
        dbHandler.setIsCamel(Boolean.TRUE);
        dbHandler.setAlwaysDrop(Boolean.FALSE);
        dbHandler.setDataSource(dataSource);
//        dbHandler.setPackageBase("com.e_commerce.miscroservice.user.entity,com.e_commerce.miscroservice.commons.entity.application.user");
//        dbHandler.setPackageBase("com.e_commerce.miscroservice.guanzhao_proj.product_order.po");
		dbHandler.setPackageBase("com.e_commerce.miscroservice.csq_proj.po");
        dbHandler.setShowSql(Boolean.TRUE);
        dbHandler.setAlwaysInit(Boolean.TRUE);
//        dbHandler.generate(false,"t" ,"com.e_commerce.miscroservice.user.controller.doz","t*");
        return dbHandler;
    }


}

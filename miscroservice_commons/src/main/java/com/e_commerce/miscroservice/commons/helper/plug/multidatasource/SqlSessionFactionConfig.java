package com.e_commerce.miscroservice.commons.helper.plug.multidatasource;

import com.e_commerce.miscroservice.commons.helper.env.ApplicationEnv;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 自定义sqlFactory用于设置多数据源工厂
 *
 * @see MultiDataSourceTransactionFactory 的配置
 */
@Configuration
@ConditionalOnBean(ApplicationEnv.class)
public class SqlSessionFactionConfig {

    private Log logger = Log.getInstance(com.e_commerce.miscroservice.commons.helper.plug.multidatasource.SqlSessionFactionConfig.class);

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public SqlSessionFactory createSqlSessionFactory(
            @Value("${mybatis.typeAliasesPackage:''}") String typeAliasesPackage,
            @Value("${mybatis.mapperLocations}") String mapperLocations,
            DataSource dataSource,
            MybatisProperties properties,
            ObjectProvider<Interceptor[]> interceptorsProvider) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = null;
        try {

            sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            if (properties != null) {
                sqlSessionFactoryBean.setConfiguration(properties.getConfiguration());
            }
            sqlSessionFactoryBean.setTransactionFactory(new MultiDataSourceTransactionFactory());
            if (!typeAliasesPackage.isEmpty()) {
                sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
            }
            sqlSessionFactoryBean.setPlugins(interceptorsProvider.getIfAvailable());

            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        } catch (Exception e) {
            logger.error("创建sqlSessionFactory错误", e);
        }
        return sqlSessionFactoryBean.getObject();
    }
}

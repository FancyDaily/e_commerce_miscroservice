//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.e_commerce.miscroservice.commons.config.application;

import com.e_commerce.miscroservice.commons.annotation.application.DependsOnSql;
import com.e_commerce.miscroservice.commons.helper.env.ApplicationEnv;
import com.e_commerce.miscroservice.commons.helper.env.PreServiceEnv;
import com.e_commerce.miscroservice.commons.helper.init.AutoInitSql;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.multidatasource.MultiDataSourceHepler.CreateDataSourceRouting;
import com.e_commerce.miscroservice.commons.helper.plug.multidatasource.MultiDataSourceHepler.DataSourceHolder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement(
		proxyTargetClass = true
)
@ConditionalOnBean({ApplicationEnv.class})
public class DbConfig {
	private Log log = Log.getInstance(DbConfig.class);
	@Value("${spring.application.name:'all'}")
	private String applicationName;
	private final String DEFAULT_DATABASE_SCHEMA_PREFIX = "schema_version_";

	public DbConfig() {
	}

	@Bean(
			name = {"write"},
			destroyMethod = "close"
	)
	public DataSource getWriteDataSource(@Value("${jdbc.write.class}") String className, @Value("${jdbc.write.url}") String databaseUrl, @Value("${jdbc.write.user}") String userName, @Value("${jdbc.write.pass}") String password) {
		this.log.info("获取写库的连接信息为{}", new Object[]{databaseUrl});
		return this.createDataSource(className, databaseUrl, userName, password);
	}

	@Bean(
			name = {"read"},
			destroyMethod = "close"
	)
	public DataSource getReadDataSource(@Value("${jdbc.read.class}") String className, @Value("${jdbc.read.url}") String databaseUrl, @Value("${jdbc.read.loadBalance.url:''}") String loadBalanceUrl, @Value("${jdbc.read.user}") String userName, @Value("${jdbc.read.pass}") String password) {
		if (!loadBalanceUrl.isEmpty()) {
			databaseUrl = loadBalanceUrl;
		}

		this.log.info("获取读库的连接信息为{}", new Object[]{databaseUrl});
		return this.createDataSource(className, databaseUrl, userName, password);
	}

	@Bean(
			name = {"pre"},
			destroyMethod = "close"
	)
	@ConditionalOnBean({PreServiceEnv.class})
	public DataSource getPreDataSource(@Value("${jdbc.pre.class}") String className, @Value("${jdbc.pre.url}") String databaseUrl, @Value("${jdbc.pre.user}") String userName, @Value("${jdbc.pre.pass}") String password) {
		this.log.info("获取预发环境的连接信息为{}", new Object[]{databaseUrl});
		return this.createDataSource(className, databaseUrl, userName, password);
	}

	@Bean
	@Primary
	public DataSource createAbstractDataSource(DataSource write, DataSource read, @Autowired @Lazy DataSource pre) {
		CreateDataSourceRouting createDataSourceRouting = new CreateDataSourceRouting();
		Map<Object, Object> dataSources = new HashMap();
		dataSources.put("write", write);
		if (PreServiceEnv.isPreEnv()) {
			dataSources.put("pre", pre);
		}

		dataSources.put("read", read);
		DataSourceHolder.putAll(dataSources);
		createDataSourceRouting.setTargetDataSources(dataSources);
		createDataSourceRouting.setDefaultTargetDataSource(write);
		return createDataSourceRouting;
	}

	private DataSource createDataSource(String className, String url, String username, String password) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(className);
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
		hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
		hikariConfig.setMinimumIdle(5);
		hikariConfig.setMaximumPoolSize(15);
		hikariConfig.setConnectionInitSql("set names  utf8mb4;");
		HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(
			name = {"sqlInitScript"},
			initMethod = "init"
	)
	public AutoInitSql createAutoInitSql(DataSource dataSource) {
		return new AutoInitSql(dataSource);
	}

	@Bean(
			initMethod = "migrate",
			name = {"flyway"}
	)
	@DependsOnSql
	public Flyway getMigration(DataSource dataSource) {
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		flyway.setValidateOnMigrate(Boolean.FALSE);
		flyway.setLocations(new String[]{"migration"});
		flyway.setTable("schema_version_" + this.applicationName);
		flyway.setBaselineOnMigrate(Boolean.TRUE);
		flyway.repair();
		return flyway;
	}
}

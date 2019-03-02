package com.e_commerce.miscroservice.commons.helper.plug.multidatasource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * 多数据源的统一帮助类
 */
public class MultiDataSourceHepler {


    public final static String READ_DATA_SOURCE = "read";
    public final static String WRITE_DATA_SOURCE = "write";
    public final static String PRE_DATA_SOURCE = "pre";


    /**
     * 获取当前数据源
     *
     * @return
     */
    public static DataSource getCurrentDataSource() {

        return DataSourceHolder.getDataSourceByKey(DataSourceHolder.getHolder());

    }


    /**
     * 获取当前数连接池
     *
     * @return
     */
    public static Connection getCurrentConnection() {
        System.out.println("获取连接");
        Connection connection = DataSourceUtils.getConnection(getCurrentDataSource());
        try {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;


    }


    /**
     * 提交事物
     */
    public static void commit(Connection connection) {
        try {

            if (connection != null && !connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * 回滚事物
     */
    public static void rollback(Connection connection) {

        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 关闭链接
     */
    public static void close(Connection connection, String dataSourceKey) {

        DataSourceUtils.releaseConnection(connection,
                DataSourceHolder.getDataSourceByKey(dataSourceKey));

    }


    /**
     * 动态切换数据库的设置
     */
    public static class DataSourceHolder {

        private static Map<Object, Object> allDataSourceCache = new ConcurrentHashMap<>();

        public static void putAll(Map<Object, Object> allDataSource) {
            allDataSourceCache.putAll(allDataSource);
        }

        public static DataSource getDataSourceByKey(Object key) {

            if (key == null) {
                //为空的时候设置默认值
                key = WRITE_DATA_SOURCE;
            }
            Object datasource = allDataSourceCache.get(key);

            return (DataSource) datasource;
        }


        private static ThreadLocal<String> holder = new InheritableThreadLocal<>();

        public static String getHolder() {

            return holder.get();
        }


        public static void setHolder(String key) {
            holder.set(key);

        }

        public static void clearHolder() {
            holder.remove();
        }


    }

    /**
     * 数据锁的使用
     */
    public static class Lock {
        private static final int MAX_THREAD_COUNT = 5;

        private static final LongAdder LOCK = new LongAdder();

        public static void lock() {
//            while (LOCK.intValue() > MAX_THREAD_COUNT) {
//                System.out.println("锁上了...");
//            }
//            LOCK.increment();
            System.out.println("上锁");
        }

        public static void unlock() {
            LOCK.decrement();
            if (LOCK.intValue() < 0) {
                LOCK.reset();
            }

        }

    }


    /**
     * 动态数据源切换的类
     */
    public static class CreateDataSourceRouting extends AbstractRoutingDataSource {


        @Override
        protected Object determineCurrentLookupKey() {
            return DataSourceHolder.getHolder();
        }
    }
}

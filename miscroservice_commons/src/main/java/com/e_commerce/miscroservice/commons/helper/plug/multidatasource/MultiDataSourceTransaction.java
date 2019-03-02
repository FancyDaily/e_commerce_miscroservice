package com.e_commerce.miscroservice.commons.helper.plug.multidatasource;

import org.apache.ibatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义多数据源的事物注解
 */
public class MultiDataSourceTransaction implements Transaction {


    private Map<String, Connection> connectionsMap;

    public MultiDataSourceTransaction() {
        connectionsMap = new ConcurrentHashMap<>();
    }

    @Override
    public Connection getConnection() throws SQLException {
        String currentDataSourceKey = MultiDataSourceHepler.DataSourceHolder.getHolder();

        Connection connection = connectionsMap.get(currentDataSourceKey);
        if (connection != null) {
            return connection;
        }
        connection = MultiDataSourceHepler.getCurrentConnection();
        connectionsMap.put(currentDataSourceKey, connection);
        return connection;
    }


    @Override
    public void rollback() throws SQLException {
        for (Connection connection : connectionsMap.values()) {
            MultiDataSourceHepler.rollback(connection);
        }

    }


    @Override
    public void commit() throws SQLException {
        for (Connection connection : connectionsMap.values()) {
            MultiDataSourceHepler.commit(connection);
        }

    }

    @Override
    public void close() throws SQLException {

        for (Map.Entry<String, Connection> entry : connectionsMap.entrySet()) {
            MultiDataSourceHepler.close(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {

        return null;
    }
}

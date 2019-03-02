package com.e_commerce.miscroservice.commons.helper.plug.multidatasource;

import com.e_commerce.miscroservice.commons.helper.plug.multidatasource.MultiDataSourceHepler;
import lombok.Data;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.sql.Connection;

/**
 * 自定义事物处理器
 */
public class CustomPlatformTransactionManager extends AbstractPlatformTransactionManager {


    public CustomPlatformTransactionManager() {

    }


    @Override
    protected Object doGetTransaction() throws TransactionException {
        return new CustomTransactionStatus();
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {

        if (transaction instanceof CustomTransactionStatus) {
            CustomTransactionStatus status = (CustomTransactionStatus) transaction;
            Connection currentConnection = MultiDataSourceHepler.getCurrentConnection();
            status.setConnection(currentConnection);
            status.setCurrentDataSourceName(MultiDataSourceHepler.DataSourceHolder.getHolder());

        }
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
        if (status.getTransaction() instanceof CustomTransactionStatus) {
            CustomTransactionStatus customTransactionStatus = (CustomTransactionStatus) status.getTransaction();

            Connection connection = customTransactionStatus.getConnection();

            try {

                MultiDataSourceHepler.commit(connection);

            } finally {

                MultiDataSourceHepler.close(customTransactionStatus.getConnection(),
                        customTransactionStatus.getCurrentDataSourceName());
            }
        }

    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
        if (status.getTransaction() instanceof CustomTransactionStatus) {
            CustomTransactionStatus customTransactionStatus = (CustomTransactionStatus) status.getTransaction();
            Connection connection = customTransactionStatus.getConnection();

            try {
                MultiDataSourceHepler.rollback(connection);
            } finally {

                MultiDataSourceHepler.close(customTransactionStatus.getConnection(),
                        customTransactionStatus.getCurrentDataSourceName());
            }
        }

    }

    @Data
    private static class CustomTransactionStatus {

        private Connection connection;
        private String currentDataSourceName;


    }

}

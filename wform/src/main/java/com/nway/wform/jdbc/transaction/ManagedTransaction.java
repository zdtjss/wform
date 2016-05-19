package com.nway.wform.jdbc.transaction;

import static org.springframework.util.Assert.notNull;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ManagedTransaction implements Transaction {

    private static final Log LOGGER = LogFactory.getLog(ManagedTransaction.class);

    private final DataSource dataSource;

    private Connection connection;

    public ManagedTransaction(DataSource dataSource) {
      notNull(dataSource, "No DataSource specified");
      this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() throws SQLException {
      if (this.connection == null) {
        connection = ConnectionManager.getConnection(dataSource);
      }
      return this.connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() throws SQLException
    {
        ConnectionManager.commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() throws SQLException {
        
      ConnectionManager.rollback();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws SQLException {
        
        ConnectionManager.releaseConnection();
    }
      
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getTimeout() throws SQLException {
        
      return null;
    }

  }


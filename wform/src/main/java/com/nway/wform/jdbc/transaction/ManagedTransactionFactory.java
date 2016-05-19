package com.nway.wform.jdbc.transaction;

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

public class ManagedTransactionFactory implements TransactionFactory
{
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level,
            boolean autoCommit)
    {
        return new ManagedTransaction(dataSource);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction newTransaction(Connection conn)
    {
        throw new UnsupportedOperationException("New Spring transactions require a DataSource");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setProperties(Properties props)
    {
        // not needed in this version
    }
    
}
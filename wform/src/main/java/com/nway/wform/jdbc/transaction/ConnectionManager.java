package com.nway.wform.jdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class ConnectionManager
{
    /**
     * 事务的数据库连接
     */
    private static final ThreadLocal<Connection> TX_CONN = new ThreadLocal<>();
    
    /**
     * 事务计数器
     */
    private static final ThreadLocal<Integer> TX_COUNT = new ThreadLocal<>();
    
    public static Connection getConnection(DataSource dataSource) throws SQLException
    {
        
        Connection conn = TX_CONN.get();
        
        if (conn == null)
        {
            conn = dataSource.getConnection();
            
            TX_COUNT.set(0);
            TX_CONN.set(conn);
        }
        
        TX_COUNT.set(TX_COUNT.get() + 1);
        
        return conn;
    }
    
    public static void commit() throws SQLException
    {
        
        Connection conn = TX_CONN.get();
        Integer count = TX_COUNT.get();
        
        if (count != null && count == 0)
        {
            conn.commit();
        }
        else {
            
            TX_COUNT.set(count - 1);
        }
    }
    
    public static void releaseConnection() throws SQLException
    {
        
        Integer count = TX_COUNT.get();
        
        if (count != null && count == 0)
        {
            TX_CONN.get().close();
            TX_CONN.remove();
            TX_COUNT.remove();
        }
        else {
            
            TX_COUNT.set(count - 1);
        }
    }
    
    public static void rollback() throws SQLException
    {
        TX_CONN.get().rollback();
        TX_COUNT.set(0);
    }
}

package com.nway.wform.jdbc.transaction;

import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public abstract class SqlSessionManager
{
    /**
     * 事务的数据库连接
     */
    private static final ThreadLocal<SqlSession> TX_SESSION = new ThreadLocal<>();
    
    /**
     * 事务计数器
     */
    private static final ThreadLocal<Integer> TX_COUNT = new ThreadLocal<>();
    
    public static void bindSqlSession(SqlSessionFactory sessionFactory) throws SQLException
    {
        TX_COUNT.set(0);
        TX_SESSION.set(sessionFactory.openSession());
    }
    
    public static SqlSession getSqlSession(SqlSessionFactory sessionFactory) throws SQLException
    {
        
        TX_COUNT.set(TX_COUNT.get() + 1);
        
        return TX_SESSION.get();
    }
    
    public static void commit() throws SQLException
    {
        
        SqlSession session = TX_SESSION.get();
        Integer count = TX_COUNT.get();
        
        if (count == 0)
        {
            session.commit();
        }
        else {
            
            TX_COUNT.set(count - 1);
        }
    }
    
    public static void releaseSqlSession() throws SQLException
    {
        
        Integer count = TX_COUNT.get();
        
        if (count == 0)
        {
            SqlSession session = TX_SESSION.get();
            
            if (session != null)
            {
                TX_SESSION.get().close();
                TX_SESSION.remove();
            }
            
            TX_COUNT.remove();
        }
        else {
            
            TX_COUNT.set(count - 1);
        }
    }
    
    public static void rollback() throws SQLException
    {
        SqlSession session = TX_SESSION.get();
        
        if (session != null)
        {
            TX_SESSION.get().rollback();
        }
        
        TX_COUNT.set(0);
    }
}

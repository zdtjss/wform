package com.nway.wform.jdbc;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.nway.wform.jdbc.transaction.SqlSessionManager;

public class SqlSessionTemplate implements SqlSession
{
    private final SqlSessionFactory sqlSessionFactory;
    
    private final SqlSession sqlSessionProxy;
    
    public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory)
    {
        this.sqlSessionFactory = sqlSessionFactory;
        this.sqlSessionProxy = (SqlSession) Proxy.newProxyInstance(SqlSessionFactory.class.getClassLoader(),
                new Class[] { SqlSession.class },
                new SqlSessionInterceptor());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T selectOne(String statement)
    {
        return this.sqlSessionProxy.<T> selectOne(statement);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T selectOne(String statement, Object parameter)
    {
        return this.sqlSessionProxy.<T> selectOne(statement, parameter);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey)
    {
        return this.sqlSessionProxy.<K, V> selectMap(statement, mapKey);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey)
    {
        return this.sqlSessionProxy.<K, V> selectMap(statement, parameter, mapKey);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds)
    {
        return this.sqlSessionProxy.<K, V> selectMap(statement, parameter, mapKey, rowBounds);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Cursor<T> selectCursor(String statement)
    {
        return this.sqlSessionProxy.selectCursor(statement);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter)
    {
        return this.sqlSessionProxy.selectCursor(statement, parameter);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds)
    {
        return this.sqlSessionProxy.selectCursor(statement, parameter, rowBounds);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <E> List<E> selectList(String statement)
    {
        return this.sqlSessionProxy.<E> selectList(statement);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <E> List<E> selectList(String statement, Object parameter)
    {
        return this.sqlSessionProxy.<E> selectList(statement, parameter);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds)
    {
        return this.sqlSessionProxy.<E> selectList(statement, parameter, rowBounds);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void select(String statement, ResultHandler handler)
    {
        this.sqlSessionProxy.select(statement, handler);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void select(String statement, Object parameter, ResultHandler handler)
    {
        this.sqlSessionProxy.select(statement, parameter, handler);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler)
    {
        this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int insert(String statement)
    {
        return this.sqlSessionProxy.insert(statement);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int insert(String statement, Object parameter)
    {
        return this.sqlSessionProxy.insert(statement, parameter);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int update(String statement)
    {
        return this.sqlSessionProxy.update(statement);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int update(String statement, Object parameter)
    {
        return this.sqlSessionProxy.update(statement, parameter);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(String statement)
    {
        return this.sqlSessionProxy.delete(statement);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(String statement, Object parameter)
    {
        return this.sqlSessionProxy.delete(statement, parameter);
    }
    
    private class SqlSessionInterceptor implements InvocationHandler
    {
        
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            SqlSession sqlSession = SqlSessionManager.getSqlSession(sqlSessionFactory);
            
            try
            {
                Object result = method.invoke(sqlSession, args);
                
                SqlSessionManager.commit();
                
                return result;
            }
            catch (Throwable e)
            {
                SqlSessionManager.rollback();
                
                throw e.getCause();
            }
            finally
            {
                SqlSessionManager.releaseConnection();
            }
        }
    }
    
    @Override
    public void commit()
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void commit(boolean force)
    {
        throw new UnsupportedOperationException();
        
    }
    
    @Override
    public void rollback()
    {
        throw new UnsupportedOperationException();
        
    }
    
    @Override
    public void rollback(boolean force)
    {
        throw new UnsupportedOperationException();
        
    }
    
    @Override
    public List<BatchResult> flushStatements()
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void close()
    {
        throw new UnsupportedOperationException();
        
    }
    
    @Override
    public void clearCache()
    {
        this.sqlSessionProxy.clearCache();
    }
    
    @Override
    public Configuration getConfiguration()
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <T> T getMapper(Class<T> type)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Connection getConnection()
    {
        return this.sqlSessionProxy.getConnection();
    }
}

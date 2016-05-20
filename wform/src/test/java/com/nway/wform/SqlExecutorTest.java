package com.nway.wform;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.nway.wform.jdbc.MybatisExecutor;
import com.nway.wform.jdbc.transaction.ManagedTransactionFactory;

public class SqlExecutorTest
{
    @Test
    public void testGetInstance() throws IOException
    {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        
        Configuration configuration = sqlSessionFactory.getConfiguration();
        
        configuration.setEnvironment(new Environment("test", new ManagedTransactionFactory(), configuration.getEnvironment().getDataSource()));
        
        MybatisExecutor sqlSessionTemplate = new MybatisExecutor(sqlSessionFactory);
        
        Map<String,Object> param = new HashMap<>();
        
        param.put("formId", 1001);
        param.put("version", 1);
        
        Object obj = sqlSessionTemplate.selectOne("selectForm", param);
        
        System.out.println(obj);
    }
}

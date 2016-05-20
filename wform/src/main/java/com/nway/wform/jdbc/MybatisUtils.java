package com.nway.wform.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.nway.wform.jdbc.transaction.ManagedTransactionFactory;

public abstract class MybatisUtils
{
    private static final SqlSessionFactory sqlSessionFactory;
    
    private static final MybatisExecutor mybatisExecutor;
    
    static
    {
        InputStream inputStream = null;
        try
        {
            inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        
        Configuration configuration = sqlSessionFactory.getConfiguration();
        
        configuration.setEnvironment(new Environment("wformenv", new ManagedTransactionFactory(),
                configuration.getEnvironment().getDataSource()));
        
        String packageName = "com.nway.wform.entity";
        
        try
        {
            List<String> children = VFS.getInstance().list(packageName.replace('.', '/'));
            
            for (String child : children)
            {
                if (child.endsWith(".xml"))
                {
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(
                            Resources.getResourceAsStream(child), configuration, child,
                            configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        mybatisExecutor = new MybatisExecutor(sqlSessionFactory);
    }
    
    public static SqlSessionFactory getSqlSessionFactory()
    {
        return sqlSessionFactory;
    }
    
    public static MybatisExecutor getMybatisExecutor()
    {
        return mybatisExecutor;
    }
}

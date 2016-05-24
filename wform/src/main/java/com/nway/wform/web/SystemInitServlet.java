package com.nway.wform.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nway.wform.jdbc.transaction.ManagedTransactionFactory;


@SuppressWarnings("serial")
public class SystemInitServlet extends HttpServlet {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void init(ServletConfig config) throws ServletException
	{
	    super.init(config);
	    
	    String mybatisConfig = config.getInitParameter("mybatisConfig");
	    String mapperLocations = config.getInitParameter("mapperLocations");
	    
	    SqlSessionFactory sqlSessionFactory = buildSqlSessionFactory(mybatisConfig, mapperLocations);
	    
	    config.getServletContext().setAttribute("sqlSessionFactory", sqlSessionFactory);
	}
	
	@Override
	public void destroy()
	{
	    super.destroy();
	    
	    
	}
	
	private SqlSessionFactory buildSqlSessionFactory(String mybatisConfig,String mapperLocations) {
	    
	    InputStream inputStream = null;
	    
        try
        {
            inputStream = Resources.getResourceAsStream(mybatisConfig);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        
        Configuration configuration = sqlSessionFactory.getConfiguration();
        
        configuration.setEnvironment(new Environment("wformenv", new ManagedTransactionFactory(),
                configuration.getEnvironment().getDataSource()));
        
        String packageName = "com.nway.wform.entity";
        
        try
        {
            List<String> mapperFiles = new ArrayList<>();
            
            for(String path : mapperLocations.split(",")) {
                
                mapperFiles.addAll( VFS.getInstance().list(path.replace('.', '/')));
            }
            
            for (String child : mapperFiles)
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
        
        return sqlSessionFactory;
	}
}

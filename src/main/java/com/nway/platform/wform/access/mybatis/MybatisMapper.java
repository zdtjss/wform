package com.nway.platform.wform.access.mybatis;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nway.platform.wform.design.entity.Page;
import com.nway.platform.wform.design.entity.PageForm;
import com.nway.platform.wform.design.entity.PageList;
import com.nway.platform.wform.design.entity.PageListCondition;

@Component
public class MybatisMapper {
	
	private static final Logger log = LoggerFactory.getLogger(MybatisMapper.class);
	
	private static final String CLASS_PATH = getClassPath();
	public static final String BASE_NS = "com.nway.platform.wform.dynamic.";
	public static final String BASE_PATH = "com.nway.platform.wform.dynamic.".replace('.', File.separatorChar);
	
	@Autowired
    private SqlSessionTemplate sqlSession;
	
	public void createMapper(Page formPage) throws ParserConfigurationException, TransformerException, IOException {
		
		File mapperFile = new File(buildMapperFilePath(formPage.getName(), formPage.getModuleName()));

		boolean exists = mapperFile.exists();
		
		String namespace = getNS(formPage.getName(), formPage.getModuleName());
		saveMapperFile(mapperFile, createMapper(formPage, namespace));
		addMapper(mapperFile.getAbsolutePath());
		
		if(exists) {
			
			File tempMapperFile = new File(buildMapperFilePath(formPage.getName(), formPage.getModuleName()));
			String newNs = TemporaryStatementRegistry.createNS(namespace);
			saveMapperFile(tempMapperFile, createMapper(formPage, newNs));
			addMapper(tempMapperFile.getAbsolutePath());
			tempMapperFile.delete();
		}
	}
	
	public static String getNS(String pageName, String moduleName) {
		
		return BASE_NS + moduleName + "." + pageName;
	}

	private String buildMapperFilePath(String pageName, String moduleName) {
		
		return CLASS_PATH + BASE_PATH + moduleName + File.separatorChar + "dao" + File.separatorChar + pageName + "Mapper.xml";
	}
	
	private Document createMapper(Page formPage, String mapperNamespace) throws ParserConfigurationException, TransformerException, IOException {
		
		Document document = createDocument();
		
		Element root = document.createElement("mapper");
		
		root.setAttribute("namespace", mapperNamespace);
		
		root.appendChild(buildResultMapMapper(document, formPage));
		root.appendChild(buildInsertMapper(document, formPage));
		root.appendChild(buildSelectMapper(document, formPage));
		root.appendChild(buildUpdateMapper(document, formPage));
		root.appendChild(buildListMapper(document, formPage));
		root.appendChild(buildDeleteMapper(document, formPage));
		
		document.appendChild(root);
		
		return document;
	}
	
	private void saveMapperFile(File mapperFile, Document document) throws TransformerException, IOException {
		
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF8");
		tf.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//mybatis.org//DTD Mapper 3.0//EN");
		tf.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
		
		if(mapperFile.exists()) {
			
			mapperFile.delete();
		}
		else {
			
			mapperFile.getParentFile().mkdirs();
			
			mapperFile.createNewFile();
		}
		
		tf.transform(new DOMSource(document), new StreamResult(mapperFile));
		
		log.info(mapperFile.getPath());
	}
	
	private Element buildInsertMapper(Document document, Page formPage) throws ParserConfigurationException {
		
		Element insert = document.createElement("insert");
		
		insert.setAttribute("id", "save");
		
		StringBuilder insertSql = new StringBuilder();
		
		insertSql.append("\n    insert into ").append(formPage.getTableName()).append("(");
		
		List<PageForm> pageFields = formPage.getFormFields();
		
		if (pageFields != null && pageFields.size() > 0) {

			for (PageForm field : pageFields) {

				insertSql.append(field.getName()).append(',');
			}

			insertSql.deleteCharAt(insertSql.length() - 1).append(" )  values (");
			
			for (PageForm field : pageFields) {
				
				insertSql.append("#{").append(field.getName()).append("},");
			}
			
			insertSql.deleteCharAt(insertSql.length() - 1).append(" )\n");
		}
		
		insert.setTextContent(insertSql.toString());
		
		return insert;
	}
	
	private Element buildSelectMapper(Document document, Page formPage) throws ParserConfigurationException {
		
		Element select = document.createElement("select");
		
		select.setAttribute("id", "details");
		select.setAttribute("resultMap", formPage.getName() + "Map");
		
		StringBuilder selectSql = new StringBuilder();
		
		selectSql.append("\n    select ");
		
		List<PageForm> pageFields = formPage.getFormFields();
		
		if (pageFields != null && pageFields.size() > 0) {
			
			String keyField = "";
			
			for (PageForm field : pageFields) {
				
				selectSql.append(field.getName()).append(',');
				
				if("key".equals(field.getType())) {
					
					keyField = field.getName();
				}
			}
			
			selectSql.deleteCharAt(selectSql.length() - 1).append(formPage.getTableName()).append(" where ")
					.append(keyField).append(" = #{").append(keyField).append("} \n");
		}
		
		select.setTextContent(selectSql.toString());
		
		return select;
	}
	
	private Element buildUpdateMapper(Document document, Page formPage) throws ParserConfigurationException {
		
		Element update = document.createElement("update");
		
		update.setAttribute("id", "update");
		
		StringBuilder updateSql = new StringBuilder();
		
		updateSql.append("\n    update ").append(formPage.getTableName()).append(" set ");
		
		List<PageForm> pageFields = formPage.getFormFields();
		
		if (pageFields != null && pageFields.size() > 0) {
			
			String keyField = "";
			
			for (PageForm field : pageFields) {
				
				if(!"key".equals(field.getType())) {
					
					updateSql.append(field.getName()).append(" = #{").append(field.getName()).append("},");
				}
				else {
					
					keyField = field.getName();
				}
			}
			
			updateSql.deleteCharAt(updateSql.length() - 1).append(" where ").append(keyField).append(" = #{")
					.append(keyField).append("} \n");
		}
		
		update.setTextContent(updateSql.toString());
		
		return update;
	}
	
	private Element buildDeleteMapper(Document document, Page formPage) throws ParserConfigurationException {
		
		Element delete = document.createElement("delete");
		
		delete.setAttribute("id", "delete");
		
		StringBuilder deleteSql = new StringBuilder();
		
		List<PageForm> pageFields = formPage.getFormFields();
		
		if (pageFields != null && pageFields.size() > 0) {
			
			deleteSql.append("\n   delete from ").append(formPage.getTableName()).append(" where ");
			
			String keyField = "";
			
			for (PageForm field : pageFields) {
				
				if("key".equals(field.getType())) {
					
					keyField = field.getName();
					
					break;
				}
			}
			
			deleteSql.append(keyField).append(" = #{").append(keyField).append("} \n");
		}
		
		delete.setTextContent(deleteSql.toString());
		
		return delete;
	}
	
	private Element buildResultMapMapper(Document document, Page formPage) throws ParserConfigurationException {
		
		Element resultMap = document.createElement("resultMap");
		
		resultMap.setAttribute("id", formPage.getName() + "Map");
		resultMap.setAttribute("type", "map");
		
		List<PageForm> pageFields = formPage.getFormFields();
		
		if (pageFields != null && pageFields.size() > 0) {
			
			for (PageForm field : pageFields) {
				
				Element result = document.createElement("result");
				
				result.setAttribute("column", field.getName());
				result.setAttribute("property", field.getName());
				
				resultMap.appendChild(result);
			}
		}
		
		return resultMap;
	}
	
	private Element buildListMapper(Document document, Page formPage) throws ParserConfigurationException {
		
		Element select = document.createElement("select");
		
		select.setAttribute("id", "list");
		select.setAttribute("resultMap", formPage.getName() + "Map");
		
		StringBuilder listSql = new StringBuilder();
		
		listSql.append("\n    select ");
		
		List<PageList> pageFields = formPage.getListFields();
		
		if (pageFields != null && pageFields.size() > 0) {
			
			for (PageList field : pageFields) {
				
				listSql.append(field.getName()).append(',');
			}
			
			listSql.deleteCharAt(listSql.length() - 1).append(formPage.getTableName()).append(" where 1 = 1 \n");
		}
		
		select.setTextContent(listSql.toString());
		
		List<PageListCondition> condition = formPage.getListCondition();
		
		if (condition != null && condition.size() > 0) {

			for (PageListCondition field : condition) {
				
				Element tagIf = document.createElement("if");
				
				tagIf.setAttribute("test", field.getName() + " != null");
				
				tagIf.setTextContent("\n    and " + field.getName() + " = #{" + field.getName() + "}\n");
				
				select.appendChild(tagIf);
			}
		}
		
		return select;
	}

	private Document createDocument() throws ParserConfigurationException {

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		
		return builder.newDocument();
	}

	private void addMapper(String mapperLocation) throws NestedIOException {

		Configuration configuration = sqlSession.getConfiguration();
		FileSystemResource resource = new FileSystemResource(mapperLocation);

		try {
			XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration,
					resource.toString(), configuration.getSqlFragments());
			xmlMapperBuilder.parse();
		} catch (Exception e) {
			throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
		}
	}
	
	private static String getClassPath() {

		String path = MybatisMapper.class.getName().replace('.', File.separatorChar) + ".class";

		String classPathBase = MybatisMapper.class.getClassLoader().getResource(path).getFile();

		return classPathBase.substring(0, classPathBase.length() - path.length());
	}
}

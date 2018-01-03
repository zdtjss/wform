package com.nway.platform.wform.access.mybatis;

import java.util.HashMap;
import java.util.Map;

public class TemporaryStatementRegistry {

	private static final Map<String, String> STATEMENT_NAMES = new HashMap<String,String>();
	
	public static void addName(String mapperName) {
		
		STATEMENT_NAMES.put(mapperName, generatorStatementName(mapperName));
	}
	
	public static String getLastestName(String pageName, String groupName, String type) {

		String originMapperName = pageName + "." + groupName + "_" + type;
		
		String temporaryStatement = STATEMENT_NAMES.get(originMapperName);

		return temporaryStatement == null ? originMapperName : temporaryStatement;
	}
	
	private static String generatorStatementName(String mapperName) {
		
		String lastest = STATEMENT_NAMES.get(mapperName);
		
		if (lastest == null) {

			lastest = lastest + "_0";
		}
		
		return mapperName + "_" + (Integer.parseInt(lastest.substring(lastest.lastIndexOf('_'))) + 1);
	}
}

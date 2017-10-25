package com.nway.wform.access.mybatis;

import java.util.HashMap;
import java.util.Map;

public class TemporaryStatementRegistry {

	private static final Map<String, String> STATEMENT_NAMES = new HashMap<String,String>();
	
	public static void addName(String fullGroupName) {
		
		if(STATEMENT_NAMES.containsKey(fullGroupName)) {
			
			STATEMENT_NAMES.put(fullGroupName, generatorStatementName(fullGroupName));
		}
		else {
			
			STATEMENT_NAMES.put(fullGroupName, fullGroupName);
		}
	}
	
	public static String getLastestName(String fullGroupName) {
		
		return STATEMENT_NAMES.get(fullGroupName);
	}
	
	private static String generatorStatementName(String fullGroupName) {
		
		String lastest = getLastestName(fullGroupName);
		
		return fullGroupName + "_" + (Integer.parseInt(lastest.substring(lastest.lastIndexOf('_'))) + 1);
	}
}

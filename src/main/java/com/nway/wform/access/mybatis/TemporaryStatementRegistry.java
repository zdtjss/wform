package com.nway.wform.access.mybatis;

import java.util.HashMap;
import java.util.Map;

public class TemporaryStatementRegistry {

	private static final Map<String, String> STATEMENT_NAMES = new HashMap<String,String>();
	
	public static void addName(String formPageName, String groupName) {
		
		String name = getDefaultName(formPageName, groupName);
		
		if(STATEMENT_NAMES.containsKey(name)) {
			
			STATEMENT_NAMES.put(name, generatorStatementName(formPageName, groupName));
		}
		else {
			
			STATEMENT_NAMES.put(name, name);
		}
	}
	
	public static String getLastestName(String formPageName, String groupName) {
		
		return STATEMENT_NAMES.get(formPageName + "_" + groupName);
	}
	
	private static String generatorStatementName(String formPageName, String groupName) {
		
		String lastest = getLastestName(formPageName, groupName);
		
		return getDefaultName(formPageName, groupName) + "_" + (Integer.parseInt(lastest.substring(lastest.lastIndexOf('_'))) + 1);
	}
	
	private static String getDefaultName(String formPageName, String groupName) {
		
		return formPageName + "_" + groupName;
	}
}

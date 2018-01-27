package com.nway.platform.wform.access.mybatis;

import java.util.HashMap;
import java.util.Map;

public class TemporaryStatementRegistry {

	private static final Map<String, String> STATEMENT_NAMES = new HashMap<String,String>();
	
	public static String getLastestName(String moduelName, String pageName, String type) {

		String originMapperNS = MybatisMapper.getNS(pageName, moduelName);
		
		String temporaryStatement = STATEMENT_NAMES.get(originMapperNS);

		return (temporaryStatement == null ? originMapperNS : temporaryStatement) + "." + type;
	}
	
	public static String createNS(String ns) {
		
		String lastest = STATEMENT_NAMES.get(ns);
		
		if (lastest == null) {

			lastest = lastest + "_0";
		}
		
		String newNs = ns + "_" + (Integer.parseInt(lastest.substring(lastest.lastIndexOf('_') + 1)) + 1);
		
		STATEMENT_NAMES.put(ns, newNs);
		
		return newNs;
	}
}

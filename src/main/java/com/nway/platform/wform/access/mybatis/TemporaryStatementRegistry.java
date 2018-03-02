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
		
		String sn = STATEMENT_NAMES.get(ns);
		
		if (sn != null) {

			sn = ns + "_" + (Integer.parseInt(sn.substring(sn.lastIndexOf('_') + 1)) + 1);
		}
		
		STATEMENT_NAMES.put(ns, sn);
		
		return sn == null ? ns : sn;
	}
}

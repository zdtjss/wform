package com.nway.wform.commons.tag;

public class FormFunction {

	public String css(String name, String value) {
		
		if(value != null && value.length() > 0) {
			
			return name + ":" + value + ";";
		}
		
		return "";
	}
}

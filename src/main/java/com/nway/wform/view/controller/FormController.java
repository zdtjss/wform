package com.nway.wform.view.controller;

import java.util.Map;

import com.nway.wform.design.entity.FormPage;

public class FormController {

	public String getFormPage(String formPageId) {

		FormPage formPage = new FormPage();
		
		switch(formPage.getPageType()) {
		
		case FormPage.PAGE_TYPE_CREATE:

			break;
		case FormPage.PAGE_TYPE_DETAILS:

			break;
		case FormPage.PAGE_TYPE_EDIT:

			break;

		case FormPage.PAGE_TYPE_LIST:

			break;
		}

		return null;
	}

	public Object getDetails(String formPageId, String bizId) {

		return null;
	}

	public void save(Map<String, String[]> page) {

	}

	public void update(Map<String, String[]> page) {

	}

	public void list(Map<String, String[]> queryParam) {

	}

}

package com.nway.wform.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.wform.access.dao.FormPageMapper;
import com.nway.wform.design.entity.FormPage;

@Component
public class FormPageAccess {

	@Autowired
	private FormPageMapper formPageMapper;
	
	public FormPage getFormPage(String id) {
		
		return formPageMapper.getFormPage(id);
	}
}

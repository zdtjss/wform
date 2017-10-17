package com.nway.wform.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.wform.access.mapper.FormPageMapper;

@Component
public class FormPageAccess {

	@Autowired
	private FormPageMapper formPageMapper;
	
	
}

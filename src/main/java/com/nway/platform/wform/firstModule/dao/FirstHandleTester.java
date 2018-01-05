package com.nway.platform.wform.firstModule.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.nway.platform.wform.access.handler.FieldGroupDataHandler;
import com.nway.platform.wform.access.handler.HandlerType;
import com.nway.platform.wform.design.entity.FieldGroup;

@Component("firstListPageFirst_groupDataHandler")
public class FirstHandleTester implements FieldGroupDataHandler {

	@Override
	public void handleParam(HandlerType HandlerType, FieldGroup fieldGroup, Map<String, Object> param) {
		
	}

	@Override
	public void handleResult(HandlerType handlerType, FieldGroup fieldGroup, Object data) {

		System.out.println(data);
	}

}
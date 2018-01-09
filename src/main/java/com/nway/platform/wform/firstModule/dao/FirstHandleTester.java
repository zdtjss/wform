package com.nway.platform.wform.firstModule.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.nway.platform.wform.access.handler.FieldGroupDataHandler;
import com.nway.platform.wform.access.handler.HandlerType;
import com.nway.platform.wform.design.entity.PageField;

@Component("firstListPageFirst_groupDataHandler")
public class FirstHandleTester implements FieldGroupDataHandler {

	@Override
	public void handleParam(HandlerType HandlerType, PageField fieldGroup, Map<String, Object> param) {
		
	}

	@Override
	public void handleResult(HandlerType handlerType, PageField fieldGroup, Object data) {

		System.out.println(data);
	}

}

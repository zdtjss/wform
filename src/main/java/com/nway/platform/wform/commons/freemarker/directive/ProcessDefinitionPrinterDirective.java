package com.nway.platform.wform.commons.freemarker.directive;

import java.io.IOException;
import java.util.Map;

import com.nway.platform.wform.commons.SpringContextUtil;
import com.nway.platform.workflow.dao.WorkFlowDao;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ProcessDefinitionPrinterDirective implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {

		SimpleScalar pageId = (SimpleScalar) params.get("pageId");
		
		WorkFlowDao workFlowDao = SpringContextUtil.getBean(WorkFlowDao.class);
		
		env.getOut().write(workFlowDao.getProcessDefinitionKey(pageId.getAsString()));
	}

}

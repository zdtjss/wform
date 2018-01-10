package com.nway.platform.wform.commons.freemarker.directive;

import java.io.IOException;
import java.util.Map;

import com.nway.platform.wform.commons.SpringContextUtil;
import com.nway.platform.workflow.dao.WorkFlowDaoMapper;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class PdKeyPrinterDirective implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {

		SimpleScalar pageId = (SimpleScalar) params.get("pageId");
		
		WorkFlowDaoMapper workFlowDao = SpringContextUtil.getBean(WorkFlowDaoMapper.class);
		
		env.getOut().write(workFlowDao.getPdKey(pageId.getAsString()));
	}

}

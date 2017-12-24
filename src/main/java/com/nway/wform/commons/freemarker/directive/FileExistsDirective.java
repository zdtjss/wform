package com.nway.wform.commons.freemarker.directive;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class FileExistsDirective implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {

		SimpleScalar path = (SimpleScalar) params.get("path");
		
		File baseWebFile = new File(env.getConfiguration().getSharedVariable("baseWebPath").toString(), path.getAsString());
		
		if(baseWebFile.exists() && body != null) {
			
			body.render(env.getOut());
		}
	}

}

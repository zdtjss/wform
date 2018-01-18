package com.nway.platform.wform.view.spring;

import java.io.File;

import org.springframework.web.servlet.view.JstlView;

public class CustomView extends JstlView {

	private static final String WEB_ROOT = getWebRoot();
	
	@Override
	public void setUrl(String url) {

		String stable = url.substring(0, url.length() - 4) + "_stable.jsp";

		if (new File(WEB_ROOT + stable).exists()) {

			super.setUrl(stable);
		}

		super.setUrl(url);
	}
	
	private static String getWebRoot() {

		String path = CustomView.class.getName().replace('.', File.separatorChar) + ".class";

		String customViewClassPath = CustomView.class.getClassLoader().getResource(path).getFile();

		return customViewClassPath.substring(0, customViewClassPath.length() - (path.length() + 16));
	}
	
}

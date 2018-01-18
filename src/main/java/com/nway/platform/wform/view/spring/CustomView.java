package com.nway.platform.wform.view.spring;

import org.springframework.web.servlet.view.JstlView;

public class CustomView extends JstlView {

	@Override
	public void setUrl(String url) {

		String stable = url.substring(0, url.length() - 4) + "_stable.jsp";

		if (super.getServletContext().getRealPath(stable) != null) {

			super.setUrl(stable);
		}

		super.setUrl(url);
	}
}

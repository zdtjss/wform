package com.nway.platform.wform.commons.tag;

import java.io.File;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class IncludeTag extends TagSupport {
	
	private static final Logger log = LoggerFactory.getLogger(IncludeTag.class);
	
	private static final String JS_BASE_PATH = "/WEB-INF/jsp/";
	private static final String JS_BASE_PATH_EXT = "/WEB-INF/jsp/ext/";
	
	private static final String CSS_BASE_PATH = "/css/";
	private static final String CSS_BASE_PATH_EXT = "/css/ext/";
	
	private String url;
	
	private String type;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int doStartTag() throws JspException {
		
		JspWriter writer = pageContext.getOut();
		
		String contextPath = pageContext.getServletContext().getContextPath();
		
		String rootPath = pageContext.getServletContext().getRealPath("/");
		
		String hrefExt = "";
		
		/*if (StringUtils.isNotBlank(isRedirect) && isRedirect.charAt(0) == '1' && StringUtils.isNotBlank(key)) {
			
			StringBuilder extPath = new StringBuilder();

			extPath.append("/ext/").append(key.toLowerCase()).append(href.substring(0, href.length() - 4))
					.append(key.toLowerCase()).append(".css");

			String realPathExt = rootPath + extPath.toString();

			if (new File(realPathExt).exists()) {

				hrefExt = extPath.toString();
			}
		}
		
		try {
			
			writer.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + href + "\" />");
			
			if(hrefExt.length() != 0) {
				
				writer.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + hrefExt + "\" />");
			}
			
		} catch (IOException e) {
			
			log.error(e.toString(), e);
		}*/
		
		return SKIP_BODY;
	}
}

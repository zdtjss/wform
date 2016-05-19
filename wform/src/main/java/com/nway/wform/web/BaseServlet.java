package com.nway.wform.web;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * action 基础转发器
 * <p>
 * 
 * 本类提供方法即action的转换服务，对于继承自本类的子类，符合要求的方法会做为处理请求的"doXXX()"。如果servlet被映射为/demo/*.do，
 * 若访问${contextPath}/demo/${methodName}.do，则由login方法处理请求。
 * <p>
 * 为了正确使用本类，请遵循{@link #methodMap(Class)}要求。
 * 
 * @author zdtjss@163.com
 * 
 * @since 2014-1-3
 */
@SuppressWarnings("serial")
public abstract class BaseServlet extends HttpServlet {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/** ation后缀名,对于login.do actionSuffix = ".do" **/
	private String actionSuffix;
	
	private static final Gson gson = new GsonBuilder().create();

	/**
	 * 请求路径于其对应处理方法的映射
	 * <p>
	 * 本属性由{@link #init(ServletConfig)}负责初始化，因为根据servlet规范，
	 * {@link #init(ServletConfig)} 在servlet被实例化后只调用一次,且在响应请求前被调用，即有效的保障了在响应请求前
	 * {@link #processerMapper} 会被初始化，且servlet有效期内只被初始化一次.
	 * <p>
	 * 另:根据servlet规范{@link #destroy()} 由servlet容器在使servlet退出服务时调用，但是没有规定调用
	 * {@link #destroy()}后会销毁servlet实例，所以servlet类级别的信息，包括由
	 * {@link #init(ServletConfig)} 初始化的实例对象，不一定会立刻被
	 * “垃圾回收”，这就意味着他们仍然会占用系统资源，而对集合类对象（如本属性的Map）可能会占用较多系统资源，而不能被立即自动释放，基于此本类选择了在
	 * {@link #destroy()}方法中清理由对象{@link #processerMapper}占用的资源,所以没有将
	 * {@link #processerMapper}置为不可改变，但{@link #processerMapper}
	 * 是类级别共享的信息，对于servlet是应该这样做的，所以：
	 * <p>
	 * <b>不要试图在 {@link #init(ServletConfig)}外的其他方法内调用
	 * processerMapper.put(String, Method)
	 **/
	private Map<String, Method> processerMapper;

	@Override
	public void init(ServletConfig cfg) throws ServletException {

		super.init(cfg);

		ServletContext context = cfg.getServletContext();
		
		setActionSuffix(context);

		processerMapper = methodMap(getClass());
	}

	@Override
	protected final void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		processRequest(request, response);
	}

	@Override
	protected final void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		processRequest(request, response);
	}

	@Override
	protected final void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		processRequest(request, response);
	}

	@Override
	protected final void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		processRequest(request, response);
	}
	
	protected void forword(String path, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 子类如果实现了{@link #destroy()}，请不要忘记{@code super.destroy();}
	 */
	@Override
	public void destroy() {

		processerMapper.clear();

		super.destroy();
	}

	/**
	 * 
	 * 核心分发器
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String processorName = request.getPathInfo();

		Method method = processerMapper.get(processorName);

		if (null == method) {
			
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "没有找到 [ "+request.getRequestURI()+" ] 的处理方法");
			
			return;
		}

		try {

			Object rs = method.invoke(this, request, response);
			
			if (rs != null) {

				response.setContentType("application/json;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				if (rs instanceof String) {

					response.getWriter().write(rs.toString());
				} 
				else {
					response.getWriter().write(gson.toJson(rs));
				}

			}

		} catch (Exception e) {
			
			logger.error("请求处理失败 [" + request.getRequestURI() + "]", e);
			
			throw new ServletException(e);
		}

	}

	/**
	 * 映射请求处理方法，被映射到方法必须符合
	 * <p>
	 * <li>返回类型是 {@link String}，返回信息应该是一个有效的资源路径(servlet或jsp等)</li>
	 * <li>方法拥有两个参数，且第一个参数是 {@link HttpServletRequest}；第二个参数是
	 * {@link HttpServletResponse}</li>
	 * 
	 * @param mappedClass
	 *            继承了 {@link BaseServlet} 的类,或其子类
	 * @return 方法名和其对应Method对象的映射
	 */
	private Map<String, Method> methodMap(Class<?> mappedClass) {

		Method[] methods = mappedClass.getMethods();
		Map<String, Method> mapper = new HashMap<String, Method>(methods.length);

		for (Method method : methods) {
			
			Class<?>[] paramType = method.getParameterTypes();

			if (paramType.length == 2 && paramType[0].equals(HttpServletRequest.class)
					&& paramType[1].equals(HttpServletResponse.class)) {

				mapper.put("/" + method.getName() + actionSuffix, method);

				logger.debug("mapping processor : " + mappedClass.getName() + "." + method.getName());
			}
		}

		return mapper;
	}

	private void setActionSuffix(ServletContext servletContext) {

		actionSuffix = servletContext.getInitParameter("actionSuffix");

		if (actionSuffix == null || actionSuffix.length() == 0) {
			
			actionSuffix = "";
			
		} else {
			
			actionSuffix = actionSuffix.charAt(0) == '.' ? actionSuffix : "." + actionSuffix;
		}

	}
}

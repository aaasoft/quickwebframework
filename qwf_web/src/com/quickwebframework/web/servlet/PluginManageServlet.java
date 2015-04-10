package com.quickwebframework.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.quickwebframework.web.fileupload.memory.MemoryFileItemFactory;
import com.quickwebframework.web.listener.QuickWebFrameworkLoaderListener;

public class PluginManageServlet extends QwfServlet {
	private static PluginManageServlet instance;

	public static PluginManageServlet getInstance() {
		return instance;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -370194350835819493L;
	public static final String MAPPING_PROPERTY_KEY = "qwf.pluginManage.mapping";

	// 模板字符串
	private String templateString;
	// 映射的URL
	private String mapping;

	// 得到映射的URL
	public String getMapping() {
		return mapping;
	}

	public PluginManageServlet(String mapping) {
		this.mapping = mapping;
		try {
			InputStream inputStream = this
					.getClass()
					.getClassLoader()
					.getResourceAsStream(
							"com/quickwebframework/web/template/bundleManage.txt");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			IOUtils.copy(inputStream, outputStream);
			inputStream.close();
			templateString = outputStream.toString("utf-8");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	// 初始化插件管理Servlet
	public static QwfServlet initServlet(ServletContext servletContext,
			Properties quickWebFrameworkProperties) {
		String mapping = quickWebFrameworkProperties
				.getProperty(PluginManageServlet.MAPPING_PROPERTY_KEY);
		if (mapping == null)
			return null;

		// 添加插件管理Servlet
		instance = new PluginManageServlet(mapping);
		return instance;
	}

	public boolean isUrlMatch(String requestUrlWithoutContextPath) {
		return requestUrlWithoutContextPath.equals(mapping);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		BundleContext bundleContext = QuickWebFrameworkLoaderListener
				.getBundleContext();

		if (bundleContext == null) {
			System.out
					.println("BundleContext is null,Current OSGi Framework's state is "
							+ QuickWebFrameworkLoaderListener.getFramework()
									.getState()
							+ ",now trying to start OSGi Framework!"
							+ "\n\nPS:UNINSTALLED = 1"
							+ "\nINSTALLED = 2"
							+ "\nRESOLVED = 4"
							+ "\nSTARTING = 8"
							+ "\nSTOPPING = 16" + "\nACTIVE = 32");
			try {
				QuickWebFrameworkLoaderListener.getFramework().start();
				bundleContext = QuickWebFrameworkLoaderListener
						.getBundleContext();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		Bundle[] bundles = bundleContext.getBundles();

		StringBuilder sbPart1 = new StringBuilder();
		sbPart1.append("<table border=\"1\">");
		sbPart1.append("<thead>");
		sbPart1.append("<tr>");
		sbPart1.append("<th>ID</th>");
		sbPart1.append("<th>符号名称</th>");
		sbPart1.append("<th>名称</th>");
		sbPart1.append("<th>版本</th>");
		sbPart1.append("<th>状态</th>");
		sbPart1.append("<th>操作</th>");
		sbPart1.append("</tr>");
		sbPart1.append("</thead>");
		sbPart1.append("<tbody>");
		for (Bundle bundle : bundles) {
			sbPart1.append("<tr>");
			sbPart1.append("<td>").append(bundle.getBundleId()).append("</td>");
			sbPart1.append("<td>").append(bundle.getSymbolicName())
					.append("</td>");
			String bundleName = "";
			try {
				bundleName = bundle.getHeaders().get("Bundle-Name");
				if (!StringUtils.isEmpty(bundleName))
					bundleName = new String(bundleName.getBytes(), "utf-8");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			sbPart1.append("<td>").append(bundleName).append("</td>");
			sbPart1.append("<td>").append(bundle.getVersion()).append("</td>");
			sbPart1.append("<td>");
			String stateString = null;
			if (bundle.getState() == Bundle.UNINSTALLED)
				stateString = "已卸载";
			else if (bundle.getState() == Bundle.INSTALLED)
				stateString = "已安装";
			else if (bundle.getState() == Bundle.RESOLVED)
				stateString = "已解析";
			else if (bundle.getState() == Bundle.STARTING)
				stateString = "启动中";
			else if (bundle.getState() == Bundle.STOPPING)
				stateString = "停止中";
			else if (bundle.getState() == Bundle.ACTIVE)
				stateString = "激活";
			sbPart1.append(stateString);
			sbPart1.append("</td>");
			sbPart1.append("<td>");
			sbPart1.append("<div style=\"float:left\">");
			sbPart1.append("<form method=\"post\">");
			sbPart1.append("<input id=\"hiddenPluginOperateMod_")
					.append(bundle.getBundleId())
					.append("\" type=\"hidden\" name=\"mod\" value=\"\" />");
			sbPart1.append("<input type=\"hidden\" name=\"pluginId\" value=\"")
					.append(bundle.getBundleId()).append("\" />");

			if (bundle.getState() == Bundle.INSTALLED
					|| bundle.getState() == Bundle.RESOLVED) {
				sbPart1.append(
						"<input type=\"submit\" class=\"button\" value=\"启动\" onclick=\"document.getElementById('hiddenPluginOperateMod_")
						.append(bundle.getBundleId())
						.append("').value='startPlugin'\" />");
			} else if (bundle.getState() == Bundle.ACTIVE) {
				sbPart1.append(
						"<input type=\"submit\" class=\"button\" value=\"停止\" onclick=\"document.getElementById('hiddenPluginOperateMod_")
						.append(bundle.getBundleId())
						.append("').value='stopPlugin'\" />");
			}
			sbPart1.append(
					"<input type=\"submit\" class=\"button\" value=\"卸载\" onclick=\"document.getElementById('hiddenPluginOperateMod_")
					.append(bundle.getBundleId())
					.append("').value='uninstallPlugin'\" />");
			sbPart1.append("</form>");
			sbPart1.append("</div>");
			sbPart1.append("</td>");
			sbPart1.append("</tr>");
		}
		sbPart1.append("</tbody>");
		sbPart1.append("</table>");

		Object messageObj = request.getAttribute("message");
		if (messageObj != null) {
			sbPart1.append("<table border=\"1\">");
			sbPart1.append("<thead>");
			sbPart1.append("<tr>");
			sbPart1.append("<th>消息</th>");
			sbPart1.append("</tr>");
			sbPart1.append("</thead>");
			sbPart1.append("<tbody>");
			sbPart1.append("<tr>");
			sbPart1.append("<td>");
			sbPart1.append("<p>").append(messageObj).append("</p>");
			sbPart1.append("</td>");
			sbPart1.append("</tr>");
			sbPart1.append("</tbody>");
			sbPart1.append("</table>");
		}

		String outputString = templateString;
		String contextPath = request.getSession().getServletContext()
				.getContextPath();
		if (StringUtils.isEmpty(contextPath))
			contextPath = "/";
		outputString = outputString.replace("{0}", contextPath);
		outputString = outputString.replace("{1}", sbPart1.toString());
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			response.getWriter().write(outputString);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private void pushMessage(HttpServletRequest request, String message) {
		request.setAttribute("message", message);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		BundleContext bundleContext = QuickWebFrameworkLoaderListener
				.getBundleContext();

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		// 如果Request是Multipart
		if (isMultipart) {
			FileItemFactory factory = new MemoryFileItemFactory();

			ServletFileUpload upload = new ServletFileUpload(factory);
			List<?> items;
			try {
				items = upload.parseRequest(request);
			} catch (FileUploadException e1) {
				e1.printStackTrace();
				pushMessage(request, "upload.parseRequest(request)异常," + e1);
				doGet(request, response);
				return;
			}

			Properties formFieldProperties = new Properties();
			Map<String, FileItem> formFileMap = new HashMap<String, FileItem>();

			for (Object obj : items) {
				FileItem item = (FileItem) obj;
				// 如果是表单字段
				if (item.isFormField()) {
					formFieldProperties.setProperty(item.getFieldName(),
							item.getString());
				}
				// 否则是上传文件
				else {
					formFileMap.put(item.getFieldName(), item);
				}
			}

			String mod = formFieldProperties.getProperty("mod");

			// 重启框架
			if ("restartFramework".equals(mod)) {
				try {
					// 更新OSGi Framework
					QuickWebFrameworkLoaderListener.getFramework().update();
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			// 安装插件
			else if ("installPlugin".equals(mod)) {
				if (!formFileMap.containsKey("pluginFile")) {
					pushMessage(request, "未找到pluginFile参数！");
					doGet(request, response);
					return;
				}
				FileItem pluginFile = formFileMap.get("pluginFile");

				try {
					Bundle bundle = bundleContext.installBundle(
							pluginFile.getName(), pluginFile.getInputStream());
					bundle.start();

				} catch (Exception e) {
					e.printStackTrace();
					pushMessage(request, "安装插件时出错异常，" + e);
					doGet(request, response);
					return;
				}
			} else if ("updatePlugin".equals(mod)) {
				if (!formFileMap.containsKey("pluginFile")) {
					pushMessage(request, "未找到pluginFile参数！");
					doGet(request, response);
					return;
				}
				FileItem pluginFile = formFileMap.get("pluginFile");

				try {
					// 插件的符号名称
					String bundleSymbolicName;

					ZipInputStream zis = null;
					try {
						zis = new ZipInputStream(pluginFile.getInputStream());

						ZipEntry manifestZipEntry = null;
						while (true) {
							ZipEntry zipEntry = zis.getNextEntry();
							if (zipEntry == null)
								break;
							String zipEntryName = zipEntry.getName();
							if (zipEntryName.equals("META-INF/MANIFEST.MF")) {
								manifestZipEntry = zipEntry;
								break;
							}
						}
						if (manifestZipEntry == null) {
							throw new RuntimeException(
									"未找到META-INF/MANIFEST.MF文件");
						}

						InputStream manifestInputStream = zis;

						Properties manifestProp = new Properties();
						manifestProp.load(manifestInputStream);
						manifestInputStream.close();
						bundleSymbolicName = manifestProp
								.getProperty("Bundle-SymbolicName");
						if (StringUtils.isEmpty(bundleSymbolicName)) {
							throw new RuntimeException("未找到Bundle-SymbolicName");
						}
					} catch (Exception ex) {
						pushMessage(request, "读取清单文件时出错，" + ex);
						doGet(request, response);
						return;
					}
					if (zis != null) {
						zis.close();
					}

					Bundle bundle = null;
					for (Bundle tmpBundle : bundleContext.getBundles()) {
						if (bundleSymbolicName.equals(tmpBundle
								.getSymbolicName())) {
							bundle = tmpBundle;
							break;
						}
					}
					if (bundle == null) {
						pushMessage(request, String.format("未找到插件[%s]，无法更新!",
								bundleSymbolicName));
						doGet(request, response);
						return;
					}
					bundle.update(pluginFile.getInputStream());
				} catch (Exception e) {
					e.printStackTrace();
					pushMessage(request, "更新插件时异常，" + e);
					doGet(request, response);
					return;
				} catch (Error e) {
					e.printStackTrace();
					pushMessage(request, "更新插件时出错，" + e);
					doGet(request, response);
					return;
				} finally {
				}
			}
		} else {
			String mod = request.getParameter("mod");
			if ("uninstallPlugin".equals(mod)) {
				String pluginIdStr = request.getParameter("pluginId");
				Long pluginId = Long.valueOf(pluginIdStr);
				Bundle bundle = bundleContext.getBundle(pluginId);
				try {
					bundle.uninstall();
				} catch (Exception ex) {
					ex.printStackTrace();
					pushMessage(request, "卸载插件时异常，" + ex);
					doGet(request, response);
					return;
				}
			} else if ("stopPlugin".equals(mod)) {
				String pluginIdStr = request.getParameter("pluginId");
				Long pluginId = Long.valueOf(pluginIdStr);
				Bundle bundle = bundleContext.getBundle(pluginId);
				try {
					bundle.stop();
				} catch (Exception ex) {
					ex.printStackTrace();
					pushMessage(request, "停止插件时异常，" + ex);
					doGet(request, response);
					return;
				}
			} else if ("startPlugin".equals(mod)) {
				String pluginIdStr = request.getParameter("pluginId");
				Long pluginId = Long.valueOf(pluginIdStr);
				Bundle bundle = bundleContext.getBundle(pluginId);
				try {
					bundle.start();
				} catch (Exception ex) {
					pushMessage(request, "启动插件时异常，" + ex);
					ex.printStackTrace();
					doGet(request, response);
					return;
				}
			}
		}
		doGet(request, response);
		return;
	}
}

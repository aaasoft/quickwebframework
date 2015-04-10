package qwf.test.core;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import qwf.test.core.servlet.TestServlet;

import com.quickwebframework.framework.WebContext;
import com.quickwebframework.ioc.spring.util.ApplicationContextListener;
import com.quickwebframework.ioc.spring.util.BundleApplicationContextUtils;

public class Activator implements BundleActivator {
	private static BundleContext context;
	private ApplicationContextListener applicationContextListener;

	public static BundleContext getContext() {
		return context;
	}

	// 自定义Bean名称生成器类
	public class MySpringBeanNameGenerator implements BeanNameGenerator {

		public String generateBeanName(BeanDefinition arg0,
				BeanDefinitionRegistry arg1) {
			String beanClassName = arg0.getBeanClassName();
			String beanName = "quickwebframework_bean_"
					+ beanClassName.substring(0, 1).toLowerCase()
					+ beanClassName.substring(1);
			return beanName;
		}
	}

	public Activator() {
		applicationContextListener = new ApplicationContextListener() {

			// 开始时
			public void contextStarting(ApplicationContext applicationContext,
					Bundle bundle) {
				if (AnnotationConfigApplicationContext.class
						.isInstance(applicationContext)) {
					AnnotationConfigApplicationContext annotationConfigApplicationContext = (AnnotationConfigApplicationContext) applicationContext;
					// 设置Bean名称生成器
					annotationConfigApplicationContext
							.setBeanNameGenerator(new MySpringBeanNameGenerator());
				}
			}

			// 开始后
			public void contextStarted(ApplicationContext applicationContext,
					Bundle bundle) {
			}

			public Map<String, BeanDefinition> getExtraBeanDefinitions() {
				return null;
			}
		};
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// 添加一个ApplicationContext监听器
		BundleApplicationContextUtils
				.addApplicationContextListener(applicationContextListener);
		// 注册Servlet
		WebContext.registerServlet("/test", new TestServlet(), null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		// 取消注册Servlet
		WebContext.unregisterServlet("/test");

		// 移除一个ApplicationContext监听器
		BundleApplicationContextUtils
				.removeApplicationContextListener(applicationContextListener);
	}

}

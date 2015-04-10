package com.quickwebframework.framework;

import java.util.ArrayList;
import java.util.List;

public class ContextManager {

	private static List<FrameworkContext> contextList;

	public static void initAllContext() {
		contextList = new ArrayList<FrameworkContext>();

		contextList.add(OsgiContext.getInstance());
		contextList.add(WebContext.getInstance());
		contextList.add(LogContext.getInstance());
		contextList.add(ThreadContext.getInstance());

		for (FrameworkContext context : contextList) {
			context.init();
		}
	}

	public static void destoryAllContext() {
		for (FrameworkContext context : contextList) {
			context.destory();
		}
	}
}

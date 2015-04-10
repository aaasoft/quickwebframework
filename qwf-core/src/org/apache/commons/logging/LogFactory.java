package org.apache.commons.logging;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class LogFactory {
	private static Map<String, Log> logMap;

	public static Log getLog(Class<?> clazz) {
		return getLog(clazz.getName());
	}

	public static Log getLog(String name) {
		if (logMap == null)
			logMap = new HashMap<String, Log>();
		if (logMap.containsKey(name))
			return logMap.get(name);
		final Logger logger = Logger.getLogger(name);
		Log log = new Log() {

			public boolean isDebugEnabled() {
				return logger.isEnabledFor(Level.DEBUG);
			}

			public boolean isErrorEnabled() {
				return logger.isEnabledFor(Level.ERROR);
			}

			public boolean isFatalEnabled() {
				return logger.isEnabledFor(Level.FATAL);
			}

			public boolean isInfoEnabled() {
				return logger.isEnabledFor(Level.INFO);
			}

			public boolean isTraceEnabled() {
				return logger.isEnabledFor(Level.TRACE);
			}

			public boolean isWarnEnabled() {
				return logger.isEnabledFor(Level.WARN);
			}

			public void debug(Object message) {
				logger.debug(message);
			}

			public void debug(Object message, Throwable exception) {
				logger.debug(message, exception);
			}

			public void error(Object message) {
				logger.error(message);
			}

			public void error(Object message, Throwable exception) {
				logger.error(message, exception);
			}

			public void fatal(Object message) {
				logger.fatal(message);
			}

			public void fatal(Object message, Throwable exception) {
				logger.fatal(message, exception);
			}

			public void info(Object message) {
				logger.info(message);
			}

			public void info(Object message, Throwable exception) {
				logger.info(message, exception);
			}

			public void trace(Object message) {
				logger.trace(message);
			}

			public void trace(Object message, Throwable exception) {
				logger.trace(message, exception);
			}

			public void warn(Object message) {
				logger.warn(message);
			}

			public void warn(Object message, Throwable exception) {
				logger.warn(message, exception);
			}
		};
		logMap.put(name, log);
		return log;
	}
}

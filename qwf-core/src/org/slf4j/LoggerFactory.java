package org.slf4j;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggerFactory {

	private static Map<String, Logger> logMap;

	public static Logger getLogger(String name) {
		if (logMap == null)
			logMap = new HashMap<String, Logger>();
		if (logMap.containsKey(name))
			return logMap.get(name);

		final String loggerName = name;
		final Log qwfLog = LogFactory.getLog(name);

		Logger newLogger = new Logger() {

			public String getName() {
				return loggerName;
			}

			private String getFinalString(String format, Object... arguments) {
				if (arguments == null || arguments.length == 0)
					return format;
				String spString = "{}";
				StringBuilder sb = new StringBuilder(format);
				for (int i = 0; i < arguments.length; i++) {
					Object currentObj = arguments[arguments.length - i - 1];
					int currentIndex = format.lastIndexOf(spString);
					sb.delete(currentIndex, currentIndex + spString.length());
					sb.insert(currentIndex, currentObj);
				}
				return sb.toString();
			}

			public boolean isTraceEnabled() {
				return true;
			}

			public void trace(String msg) {
				qwfLog.trace(msg);
			}

			public void trace(String format, Object arg) {
				trace(format, new Object[] { arg });
			}

			public void trace(String format, Object arg1, Object arg2) {
				trace(format, new Object[] { arg1, arg2 });
			}

			public void trace(String format, Object... arguments) {
				qwfLog.trace(getFinalString(format, arguments));
			}

			public void trace(String msg, Throwable t) {
				qwfLog.trace(msg, t);
			}

			public boolean isTraceEnabled(Marker marker) {
				return true;
			}

			public void trace(Marker marker, String msg) {
				trace(msg);
			}

			public void trace(Marker marker, String format, Object arg) {
				trace(format, arg);
			}

			public void trace(Marker marker, String format, Object arg1,
					Object arg2) {
				trace(format, arg1, arg2);
			}

			public void trace(Marker marker, String format, Object... argArray) {
				trace(format, argArray);
			}

			public void trace(Marker marker, String msg, Throwable t) {
				trace(msg, t);
			}

			public boolean isDebugEnabled() {
				return true;
			}

			public void debug(String msg) {
				qwfLog.debug(msg);
			}

			public void debug(String format, Object arg) {
				debug(format, new Object[] { arg });
			}

			public void debug(String format, Object arg1, Object arg2) {
				debug(format, new Object[] { arg1, arg2 });
			}

			public void debug(String format, Object... arguments) {
				debug(getFinalString(format, arguments));
			}

			public void debug(String msg, Throwable t) {
				qwfLog.debug(msg, t);
			}

			public boolean isDebugEnabled(Marker marker) {
				return true;
			}

			public void debug(Marker marker, String msg) {
				debug(msg);
			}

			public void debug(Marker marker, String format, Object arg) {
				debug(format, arg);
			}

			public void debug(Marker marker, String format, Object arg1,
					Object arg2) {
				debug(format, arg1, arg2);
			}

			public void debug(Marker marker, String format, Object... arguments) {
				debug(format, arguments);
			}

			public void debug(Marker marker, String msg, Throwable t) {
				debug(msg, t);
			}

			public boolean isInfoEnabled() {
				return true;
			}

			public void info(String msg) {
				qwfLog.info(msg);
			}

			public void info(String format, Object arg) {
				info(format, new Object[] { arg });
			}

			public void info(String format, Object arg1, Object arg2) {
				info(format, new Object[] { arg1, arg2 });
			}

			public void info(String format, Object... arguments) {
				qwfLog.info(getFinalString(format, arguments));
			}

			public void info(String msg, Throwable t) {
				qwfLog.info(msg, t);
			}

			public boolean isInfoEnabled(Marker marker) {
				return true;
			}

			public void info(Marker marker, String msg) {
				info(msg);
			}

			public void info(Marker marker, String format, Object arg) {
				info(format, arg);
			}

			public void info(Marker marker, String format, Object arg1,
					Object arg2) {
				info(format, arg1, arg2);
			}

			public void info(Marker marker, String format, Object... arguments) {
				info(format, arguments);
			}

			public void info(Marker marker, String msg, Throwable t) {
				qwfLog.info(msg, t);
			}

			public boolean isWarnEnabled() {
				return true;
			}

			public void warn(String msg) {
				qwfLog.warn(msg);
			}

			public void warn(String format, Object arg) {
				warn(format, new Object[] { arg });
			}

			public void warn(String format, Object... arguments) {
				qwfLog.warn(getFinalString(format, arguments));
			}

			public void warn(String format, Object arg1, Object arg2) {
				warn(format, new Object[] { arg1, arg2 });
			}

			public void warn(String msg, Throwable t) {
				qwfLog.warn(msg, t);
			}

			public boolean isWarnEnabled(Marker marker) {
				return true;
			}

			public void warn(Marker marker, String msg) {
				warn(msg);
			}

			public void warn(Marker marker, String format, Object arg) {
				warn(format, arg);
			}

			public void warn(Marker marker, String format, Object arg1,
					Object arg2) {
				warn(format, arg1, arg2);
			}

			public void warn(Marker marker, String format, Object... arguments) {
				warn(format, arguments);
			}

			public void warn(Marker marker, String msg, Throwable t) {
				warn(msg, t);
			}

			public boolean isErrorEnabled() {
				return true;
			}

			public void error(String msg) {
				qwfLog.error(msg);
			}

			public void error(String format, Object arg) {
				error(format, new Object[] { arg });
			}

			public void error(String format, Object arg1, Object arg2) {
				error(format, new Object[] { arg1, arg2 });
			}

			public void error(String format, Object... arguments) {
				qwfLog.error(getFinalString(format, arguments));
			}

			public void error(String msg, Throwable t) {
				qwfLog.error(msg, t);
			}

			public boolean isErrorEnabled(Marker marker) {
				return true;
			}

			public void error(Marker marker, String msg) {
				error(msg);
			}

			public void error(Marker marker, String format, Object arg) {
				error(format, arg);
			}

			public void error(Marker marker, String format, Object arg1,
					Object arg2) {
				error(format, arg1, arg2);
			}

			public void error(Marker marker, String format, Object... arguments) {
				error(format, arguments);
			}

			public void error(Marker marker, String msg, Throwable t) {
				error(msg, t);
			}
		};
		logMap.put(name, newLogger);
		return newLogger;
	}

	public static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}
}

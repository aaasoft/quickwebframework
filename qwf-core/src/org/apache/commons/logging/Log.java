package org.apache.commons.logging;

public interface Log {

	public boolean isDebugEnabled();

	public boolean isErrorEnabled();

	public boolean isFatalEnabled();

	public boolean isInfoEnabled();

	public boolean isTraceEnabled();

	public boolean isWarnEnabled();

	public void debug(Object message);

	public void debug(Object message, Throwable exception);

	public void error(Object message);

	public void error(Object message, Throwable exception);

	public void fatal(Object message);

	public void fatal(Object message, Throwable exception);

	public void info(Object message);

	public void info(Object message, Throwable exception);

	public void trace(Object message);

	public void trace(Object message, Throwable exception);

	public void warn(Object message);

	public void warn(Object message, Throwable exception);
}

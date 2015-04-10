package com.quickwebframework.bridge;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogBridge extends Handler {
	@Override
	public void close() throws SecurityException {
	}

	@Override
	public void flush() {
	}

	@Override
	public void publish(LogRecord logRecord) {
		Log log = LogFactory.getLog(logRecord.getLoggerName());
		Level logLevel = logRecord.getLevel();
		if (logLevel.equals(Level.SEVERE)) {
			log.error(logRecord.getMessage(), logRecord.getThrown());
		} else if (logLevel.equals(Level.WARNING)) {
			log.warn(logRecord.getMessage(), logRecord.getThrown());
		} else if (logLevel.equals(Level.INFO)) {
			log.info(logRecord.getMessage(), logRecord.getThrown());
		} else if (logLevel.equals(Level.CONFIG)) {
			log.debug(logRecord.getMessage(), logRecord.getThrown());
		} else if (logLevel.equals(Level.FINE) || logLevel.equals(Level.FINER)
				|| logLevel.equals(Level.FINEST)) {
			log.trace(logRecord.getMessage(), logRecord.getThrown());
		} else if (logLevel.equals(Level.ALL)) {
			log.error(logRecord.getMessage(), logRecord.getThrown());
			log.warn(logRecord.getMessage(), logRecord.getThrown());
			log.info(logRecord.getMessage(), logRecord.getThrown());
			log.debug(logRecord.getMessage(), logRecord.getThrown());
			log.trace(logRecord.getMessage(), logRecord.getThrown());
		}
	}

}

package com.quickwebframework.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.io.input.ReaderInputStream;

public class PropertiesUtils {

	public static Properties load(Reader reader) {
		InputStream input = new ReaderInputStream(reader);
		Properties prop = new Properties();
		try {
			prop.load(input);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				input.close();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		return prop;
	}
}

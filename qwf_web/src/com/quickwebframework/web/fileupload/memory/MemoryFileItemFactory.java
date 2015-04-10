package com.quickwebframework.web.fileupload.memory;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;

public class MemoryFileItemFactory implements FileItemFactory {
	public FileItem createItem(String fieldName, String contentType,
			boolean isFormField, String fileName) {
		MemoryFileItem result = new MemoryFileItem(fieldName, contentType,
				isFormField, fileName);
		return result;
	}
}
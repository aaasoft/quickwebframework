package com.quickwebframework.web.fileupload.memory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemHeadersSupport;
import org.apache.commons.fileupload.ParameterParser;
import org.apache.commons.fileupload.util.Streams;

public class MemoryFileItem implements FileItem, FileItemHeadersSupport {

	// ----------------------------------------------------- Manifest constants

	/**
	 * The UID to use when serializing this instance.
	 */
	private static final long serialVersionUID = 2237570099615271025L;

	/**
	 * Default content charset to be used when no explicit charset parameter is
	 * provided by the sender. Media subtypes of the "text" type are defined to
	 * have a default charset value of "ISO-8859-1" when received via HTTP.
	 */
	public static final String DEFAULT_CHARSET = "ISO-8859-1";

	// ----------------------------------------------------------- Data members
	/**
	 * The name of the form field as provided by the browser.
	 */
	private String fieldName;

	/**
	 * The content type passed by the browser, or <code>null</code> if not
	 * defined.
	 */
	private String contentType;

	/**
	 * Whether or not this item is a simple form field.
	 */
	private boolean isFormField;

	/**
	 * The original filename in the user's filesystem.
	 */
	private String fileName;

	/**
	 * Output stream for this item.
	 */
	private transient ByteArrayOutputStream dfos;

	/**
	 * The file items headers.
	 */
	private FileItemHeaders headers;

	public MemoryFileItem(String fieldName, String contentType,
			boolean isFormField, String fileName) {
		this.fieldName = fieldName;
		this.contentType = contentType;
		this.isFormField = isFormField;
		this.fileName = fileName;
	}

	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(dfos.toByteArray());
	}

	/**
	 * Returns the content type passed by the agent or <code>null</code> if not
	 * defined.
	 * 
	 * @return The content type passed by the agent or <code>null</code> if not
	 *         defined.
	 */
	public String getContentType() {
		return contentType;
	}

	public String getCharSet() {
		ParameterParser parser = new ParameterParser();
		parser.setLowerCaseNames(true);
		// Parameter parser can handle null input
		Map<?, ?> params = parser.parse(getContentType(), ';');
		return (String) params.get("charset");
	}

	public String getName() {
		return Streams.checkFileName(fileName);
	}

	public boolean isInMemory() {
		return true;
	}

	/**
	 * Returns the size of the file.
	 * 
	 * @return The size of the file, in bytes.
	 */
	public long getSize() {
		return dfos.size();
	}

	/**
	 * Returns the contents of the file as an array of bytes. If the contents of
	 * the file were not yet cached in memory, they will be loaded from the disk
	 * storage and cached.
	 * 
	 * @return The contents of the file as an array of bytes.
	 */
	public byte[] get() {
		return dfos.toByteArray();
	}

	/**
	 * Returns the contents of the file as a String, using the specified
	 * encoding. This method uses {@link #get()} to retrieve the contents of the
	 * file.
	 * 
	 * @param charset
	 *            The charset to use.
	 * 
	 * @return The contents of the file, as a string.
	 * 
	 * @throws UnsupportedEncodingException
	 *             if the requested character encoding is not available.
	 */
	public String getString(final String charset)
			throws UnsupportedEncodingException {
		return new String(get(), charset);
	}

	/**
	 * Returns the contents of the file as a String, using the default character
	 * encoding. This method uses {@link #get()} to retrieve the contents of the
	 * file.
	 * 
	 * @return The contents of the file, as a string.
	 * 
	 * @todo Consider making this method throw UnsupportedEncodingException.
	 */
	public String getString() {
		byte[] rawdata = get();
		String charset = getCharSet();
		if (charset == null) {
			charset = DEFAULT_CHARSET;
		}
		try {
			return new String(rawdata, charset);
		} catch (UnsupportedEncodingException e) {
			return new String(rawdata);
		}
	}

	/**
	 * A convenience method to write an uploaded item to disk. The client code
	 * is not concerned with whether or not the item is stored in memory, or on
	 * disk in a temporary location. They just want to write the uploaded item
	 * to a file.
	 * <p>
	 * This implementation first attempts to rename the uploaded item to the
	 * specified destination file, if the item was originally written to disk.
	 * Otherwise, the data will be copied to the specified file.
	 * <p>
	 * This method is only guaranteed to work <em>once</em>, the first time it
	 * is invoked for a particular item. This is because, in the event that the
	 * method renames a temporary file, that file will no longer be available to
	 * copy or rename again at a later time.
	 * 
	 * @param file
	 *            The <code>File</code> into which the uploaded item should be
	 *            stored.
	 * 
	 * @throws Exception
	 *             if an error occurs.
	 */
	public void write(File file) throws Exception {
		throw new RuntimeException(
				"MemoryFileItem don't support write to file!");
	}

	/**
	 * Deletes the underlying storage for a file item, including deleting any
	 * associated temporary disk file. Although this storage will be deleted
	 * automatically when the <code>FileItem</code> instance is garbage
	 * collected, this method can be used to ensure that this is done at an
	 * earlier time, thus preserving system resources.
	 */
	public void delete() {
	}

	/**
	 * Returns the name of the field in the multipart form corresponding to this
	 * file item.
	 * 
	 * @return The name of the form field.
	 * 
	 * @see #setFieldName(java.lang.String)
	 * 
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Sets the field name used to reference this file item.
	 * 
	 * @param fieldName
	 *            The name of the form field.
	 * 
	 * @see #getFieldName()
	 * 
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Determines whether or not a <code>FileItem</code> instance represents a
	 * simple form field.
	 * 
	 * @return <code>true</code> if the instance represents a simple form field;
	 *         <code>false</code> if it represents an uploaded file.
	 * 
	 * @see #setFormField(boolean)
	 * 
	 */
	public boolean isFormField() {
		return isFormField;
	}

	/**
	 * Specifies whether or not a <code>FileItem</code> instance represents a
	 * simple form field.
	 * 
	 * @param state
	 *            <code>true</code> if the instance represents a simple form
	 *            field; <code>false</code> if it represents an uploaded file.
	 * 
	 * @see #isFormField()
	 * 
	 */
	public void setFormField(boolean state) {
		isFormField = state;
	}

	/**
	 * Returns an {@link java.io.OutputStream OutputStream} that can be used for
	 * storing the contents of the file.
	 * 
	 * @return An {@link java.io.OutputStream OutputStream} that can be used for
	 *         storing the contensts of the file.
	 * 
	 * @throws IOException
	 *             if an error occurs.
	 */
	public OutputStream getOutputStream() throws IOException {
		if (dfos == null) {
			dfos = new ByteArrayOutputStream();
		}
		return dfos;
	}

	// ------------------------------------------------------ Protected methods

	/**
	 * Removes the file contents from the temporary storage.
	 */
	protected void finalize() {
	}

	// -------------------------------------------------------- Private methods

	/**
	 * Returns a string representation of this object.
	 * 
	 * @return a string representation of this object.
	 */
	public String toString() {
		return "name=" + this.getName() + ", size=" + this.getSize()
				+ "bytes, " + "isFormField=" + isFormField() + ", FieldName="
				+ this.getFieldName();
	}

	// -------------------------------------------------- Serialization methods
	/**
	 * Returns the file item headers.
	 * 
	 * @return The file items headers.
	 */
	public FileItemHeaders getHeaders() {
		return headers;
	}

	/**
	 * Sets the file item headers.
	 * 
	 * @param pHeaders
	 *            The file items headers.
	 */
	public void setHeaders(FileItemHeaders pHeaders) {
		headers = pHeaders;
	}
}

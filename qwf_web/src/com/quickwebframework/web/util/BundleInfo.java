package com.quickwebframework.web.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.osgi.framework.Version;

/**
 * Bundle信息类(信息来源于META-INF/MANIFEST.MF文件)
 * 
 * @author aaa
 * 
 */
public class BundleInfo {

	public final static String METAINF_FILE_PATH = "META-INF/MANIFEST.MF";
	public final static String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
	public final static String BUNDLE_VERSION = "Bundle-Version";
	public final static String BUNDLE_REQUIRE_BUNDLE = "Require-Bundle";
	public final static String BUNDLE_IMPORT_PACKAGE = "Import-Package";
	public final static String BUNDLE_EXPORT_PACKAGE = "Export-Package";

	// 插件内容字节数组
	private byte[] bundleContentBytes;
	private String bundleName;
	private Version bundleVersion;
	// 需要的Bundle名称列表
	private List<String> requireBundleNameList;
	// 导入的包列表
	private List<String> importPackageList;
	// 导出的包列表
	private List<String> exportPackageList;

	// 从清单文件中得到对应的Properties
	private Properties getBundleManifestProperties(
			InputStream manifestInputStream) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			IOUtils.copy(manifestInputStream, outputStream);

			byte[] buffer = outputStream.toByteArray();
			String text = new String(buffer, "utf-8");
			text = text.replace("\r\n ", "").replace("\n ", "");

			StringReader reader = new StringReader(text);
			InputStream input = new ReaderInputStream(reader);

			// 加载清单文件
			Properties prop = new Properties();
			prop.load(input);
			return prop;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 从Properties对象中加载信息
	 * 
	 * @param prop
	 */
	public void loadProperties(Properties prop) {
		// 得到插件的名称和版本
		bundleName = prop.getProperty(BUNDLE_SYMBOLIC_NAME);
		bundleVersion = Version.parseVersion(prop.getProperty(BUNDLE_VERSION));

		// 需要的Bundle
		String requireBundleAllString = prop.getProperty(BUNDLE_REQUIRE_BUNDLE);
		if (requireBundleAllString != null) {
			String[] lineArray = requireBundleAllString.split(",");
			for (String line : lineArray) {
				line = line.trim();

				String tmpName = null;
				if (line.contains(";")) {
					tmpName = line.split(";")[0].trim();
				} else {
					tmpName = line;
				}
				requireBundleNameList.add(tmpName);
			}
		}

		// 导入的包
		String importPackageAllString = prop.getProperty(BUNDLE_IMPORT_PACKAGE);
		if (importPackageAllString != null) {
			String[] lineArray = importPackageAllString.split(",");
			for (String line : lineArray) {
				line = line.trim();

				String tmpName = null;
				if (line.contains(";")) {
					tmpName = line.split(";")[0].trim();
				} else {
					tmpName = line;
				}
				importPackageList.add(tmpName);
			}
		}
		// 导出的包
		String exportPackageAllString = prop.getProperty(BUNDLE_EXPORT_PACKAGE);
		if (exportPackageAllString != null) {
			String[] lineArray = exportPackageAllString.split(",");
			for (String line : lineArray) {
				line = line.trim();

				String tmpName = null;
				if (line.contains(";")) {
					tmpName = line.split(";")[0].trim();
				} else {
					tmpName = line;
				}
				exportPackageList.add(tmpName);
			}
		}
	}

	/**
	 * 从清单文件中加载信息
	 * 
	 * @param manifestInputStream
	 */
	public void loadManifestResource(InputStream manifestInputStream) {
		// 从插件的清单文件中得到Properties对象
		Properties prop = getBundleManifestProperties(manifestInputStream);
		loadProperties(prop);
	}

	public BundleInfo() {
		init(null);
	}

	/**
	 * 构造函数
	 * 
	 * @param bundleInputStream
	 *            插件的输入流，比如插件的文件流，网络流等
	 */
	public BundleInfo(InputStream bundleInputStream) {
		init(bundleInputStream);
	}

	private void init(InputStream inputStream) {
		requireBundleNameList = new ArrayList<String>();
		importPackageList = new ArrayList<String>();
		exportPackageList = new ArrayList<String>();

		if (inputStream != null)
			setBundleInputStream(inputStream);
	}

	/**
	 * 得到插件的输入流
	 * 
	 * @return
	 */
	public InputStream getBundleInputStream() {
		// 因为会被调用多次，所以每次初始化化一个ByteArrayInputStream
		return new ByteArrayInputStream(bundleContentBytes);
	}

	/**
	 * 设置插件的输入流
	 * 
	 * @param bundleInputStream
	 */
	public void setBundleInputStream(InputStream bundleInputStream) {
		try {
			// 先将输入流读取到一个ByteArrayOutputStream中
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			IOUtils.copy(bundleInputStream, outputStream);
			// 得到这个插件的字节数组
			bundleContentBytes = outputStream.toByteArray();
			if (bundleContentBytes.length < 2) {
				throw new RuntimeException("输入流中的字节数小于2，不是一个ZIP压缩流！");
			}
			if (bundleContentBytes[0] != 'P' || bundleContentBytes[1] != 'K') {
				throw new RuntimeException("输入流中的前两个字节不是'PK'，不是一个合法的ZIP压缩流！");
			}
			InputStream inputStream = getBundleInputStream();
			// 从ZIP流中解压并读出META-INF/MANIFEST.MF文件的内容
			ZipInputStream zipInputStream = new ZipInputStream(inputStream);
			long manifestFileLength = -1;
			while (true) {
				ZipEntry zipEntry = zipInputStream.getNextEntry();
				if (zipEntry == null)
					break;
				if (zipEntry.getName().equals(METAINF_FILE_PATH)) {
					manifestFileLength = zipEntry.getSize();
					break;
				}
			}
			if (manifestFileLength <= 0) {
				throw new RuntimeException(
						"ZIP压缩流中未能找到META-INF/MANIFEST.MF清单文件！");
			}
			// 将manifest文件的内容从压缩流中解压出来
			outputStream = new ByteArrayOutputStream();
			IOUtils.copy(zipInputStream, outputStream);
			zipInputStream.close();

			// 加载清单文件
			loadManifestResource(new ByteArrayInputStream(
					outputStream.toByteArray()));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public String getBundleName() {
		return bundleName;
	}

	public Version getBundleVersion() {
		return bundleVersion;
	}

	public List<String> getRequireBundleNameList() {
		return requireBundleNameList;
	}

	public List<String> getImportPackageList() {
		return importPackageList;
	}

	public List<String> getExportPackageList() {
		return exportPackageList;
	}
}

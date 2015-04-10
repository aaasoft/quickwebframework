package com.quickwebframework.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOExceptionWithCause;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Version;
import org.osgi.framework.wiring.FrameworkWiring;

import com.quickwebframework.web.listener.QuickWebFrameworkFactory;

/**
 * 插件辅助类
 * 
 * @author aaa
 * 
 */
public class BundleUtils {
	static Logger logger = QuickWebFrameworkFactory.getLogger(BundleUtils.class
			.getName());

	// 插件方法URL模板
	public static String bundleMethodUrlTemplate;

	/**
	 * 得到Bundle中的路径列表
	 * 
	 * @param bundle
	 *            Bundle对象
	 * @param startPath
	 *            起始路径，一般为"/"
	 * @return
	 */
	public static List<String> getPathListInBundle(Bundle bundle,
			String startPath) {
		List<String> rtnList = new ArrayList<String>();
		Enumeration<?> urlEnum = bundle.getEntryPaths(startPath);
		while (urlEnum.hasMoreElements()) {
			String url = (String) urlEnum.nextElement();
			rtnList.add(url);
			// 如果是目录
			if (url.endsWith("/")) {
				rtnList.addAll(getPathListInBundle(bundle, url));
			}
		}
		return rtnList;
	}

	/**
	 * 得到Bundle的header映射
	 * 
	 * @param bundle
	 * @return
	 */
	public static Map<String, String> getBundleHeadersMap(Bundle bundle) {
		Dictionary<String, String> headersDict = bundle.getHeaders();
		Map<String, String> headersMap = new HashMap<String, String>();
		Enumeration<String> keyEnumeration = headersDict.keys();
		while (keyEnumeration.hasMoreElements()) {
			String key = keyEnumeration.nextElement();
			headersMap.put(key, headersDict.get(key));
		}
		return headersMap;
	}

	/**
	 * 解压Bundle的文件
	 * 
	 * @param bundle
	 *            Bundle对象
	 * @param dirPath
	 *            解压到的目录
	 */
	public static void extractBundleFiles(Bundle bundle, String dirPath) {
		try {
			List<String> urlList = getPathListInBundle(bundle, "/");
			for (String url : urlList) {
				// 如果是目录
				if (url.endsWith("/")) {
					File folder = new File(dirPath + "/" + url);
					if (!folder.exists()) {
						folder.mkdirs();
					}
				} else {
					InputStream inputStream = bundle.getResource(url)
							.openStream();
					File file = new File(dirPath + "/" + url);
					file.createNewFile();
					FileOutputStream outputStream = new FileOutputStream(file);

					IOUtils.copy(inputStream, outputStream);
					outputStream.close();
					inputStream.close();
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 安装或更新插件
	 * 
	 * @param bundleContext
	 *            BundleContext
	 * @param bundleURLs
	 *            插件的URL数组
	 * @return
	 */
	public static Bundle[] installOrUpdateBundle(BundleContext bundleContext,
			URL[] bundleURLs) {
		try {
			InputStream[] bundleInputStreams = new InputStream[bundleURLs.length];
			for (int i = 0; i < bundleURLs.length; i++) {
				URL bundleURL = bundleURLs[i];
				bundleInputStreams[i] = bundleURL.openStream();
			}
			Bundle[] bundles = installOrUpdateBundle(bundleContext,
					bundleInputStreams);
			for (InputStream bundleInputStream : bundleInputStreams) {
				bundleInputStream.close();
			}
			return bundles;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 安装或更新插件
	 * 
	 * @param bundleContext
	 *            BundleContext
	 * @param bundleFiles
	 *            插件的文件数组
	 * @return
	 * @throws IOException
	 */
	public static Bundle[] installOrUpdateBundle(BundleContext bundleContext,
			File[] bundleFiles) throws IOException {
		InputStream[] bundleInputStreams = null;
		try {
			bundleInputStreams = new InputStream[bundleFiles.length];
			for (int i = 0; i < bundleFiles.length; i++) {
				File bundleFile = bundleFiles[i];
				try {
					// 尝试加载为ZIP文件
					ZipFile zipFile = new ZipFile(bundleFile);
					zipFile.close();
				} catch (IOException ex) {
					throw new IOExceptionWithCause(String.format(
							"将文件[%s]作为ZIP文件打开时出错！", bundleFile.getName()), ex);
				}
				bundleInputStreams[i] = new FileInputStream(bundleFile);
			}
			Bundle[] bundles = installOrUpdateBundle(bundleContext,
					bundleInputStreams);

			return bundles;
		} catch (IOException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (bundleInputStreams != null && bundleInputStreams.length > 0) {
				for (InputStream bundleInputStream : bundleInputStreams) {
					if (bundleInputStream == null)
						continue;
					try {
						bundleInputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 安装或更新插件
	 * 
	 * @param bundleContext
	 *            BundleContext
	 * @param bundleInputStreams
	 *            插件的输入流数组
	 * @return
	 * @throws IOException
	 * @throws BundleException
	 */
	public static Bundle[] installOrUpdateBundle(BundleContext bundleContext,
			InputStream[] bundleInputStreams) throws BundleException,
			IOException {
		List<BundleInfo> bundleInfoList = new ArrayList<BundleInfo>();
		for (InputStream bundleInputStream : bundleInputStreams) {
			// 得到插件的信息
			try {
				BundleInfo bundleInfo = new BundleInfo(bundleInputStream);
				bundleInfoList.add(bundleInfo);
			} catch (Exception ex) {
				logger.warning("警告：加载Bundle清单文件信息时失败。原因：" + ex.getMessage());
			}
		}

		// 排出安装顺序
		orderBundleInstallList(bundleInfoList);
		// 排出停止顺序(倒序)
		List<BundleInfo> shouldStopBundleInfoList = getShouldRefreshBundleInfoList(
				bundleInfoList, BundleUtils.getAllBundleInfoList(bundleContext));
		// 按照逆序停止Bundle
		for (int i = 0; i < shouldStopBundleInfoList.size(); i++) {
			BundleInfo bundleInfo = shouldStopBundleInfoList
					.get(shouldStopBundleInfoList.size() - i - 1);
			String bundleName = bundleInfo.getBundleName();
			Bundle bundle = getBundleByName(bundleContext, bundleName);
			if (bundle != null && bundle.getState() == Bundle.ACTIVE) {
				logger.config(String.format("插件智能安装函数：根据依赖关系准备停止[%s]插件！",
						bundleName));
				bundle.stop();
			}
		}
		// 休息0.001秒钟
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}
		Bundle[] installedBundles = new Bundle[bundleInfoList.size()];

		// 按照顺序安装，注意:此处只是安装并不启动插件
		for (int i = 0; i < bundleInfoList.size(); i++) {
			BundleInfo bundleInfo = bundleInfoList.get(i);
			installedBundles[i] = installOrUpdateBundle(bundleContext,
					bundleInfo);
		}

		try {
			// 刷新Bundle
			Bundle systemBundle = bundleContext.getBundle(0);
			FrameworkWiring frameworkWiring = systemBundle
					.adapt(FrameworkWiring.class);

			for (Bundle bundle : frameworkWiring.getRemovalPendingBundles()) {
				logger.config("RemovalPendingBundle:"
						+ bundle.getSymbolicName());
			}
			frameworkWiring.refreshBundles(null);

		} catch (Error error) {
			logger.warning("RemovalPendingBundle error." + error.getMessage());
		}

		// 休息0.001秒钟
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}

		// 重新排出启动或刷新顺序
		List<BundleInfo> shouldStartBundleInfoList = getShouldRefreshBundleInfoList(
				bundleInfoList, BundleUtils.getAllBundleInfoList(bundleContext));
		orderBundleInstallList(shouldStartBundleInfoList);

		// 按照顺序启动Bundle
		for (int i = 0; i < shouldStartBundleInfoList.size(); i++) {
			BundleInfo bundleInfo = shouldStartBundleInfoList.get(i);
			String bundleName = bundleInfo.getBundleName();
			Bundle bundle = getBundleByName(bundleContext, bundleName);
			if (bundle == null) {
				logger.warning(String.format(
						"插件智能安装函数警告：在OSGi容器中未发现名称为[%s]的插件！", bundleName));
				continue;
			} else {
				if (bundle.getState() == Bundle.ACTIVE) {
					logger.warning(String.format(
							"插件智能安装函数警告：[%s]插件在启动之前，已经是启动状态！", bundleName));
				}
				logger.config(String.format("插件智能安装函数：准备启动[%s]插件...",
						bundleName));
				bundle.start();
				logger.config(String.format("插件智能安装函数：启动[%s]插件完成！", bundleName));
			}
		}
		return installedBundles;
	}

	private static Bundle installOrUpdateBundle(BundleContext bundleContext,
			BundleInfo bundleInfo) throws BundleException, IOException {
		String bundleName = bundleInfo.getBundleName();
		Version bundleVersion = bundleInfo.getBundleVersion();

		Bundle preBundle = null;
		Bundle[] bundles = bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			if (bundleName.equals(bundle.getSymbolicName())) {
				preBundle = bundle;
				break;
			}
		}

		Bundle newBundle = null; // 如果之前没有此插件，则安装
		if (preBundle == null) {
			logger.info("自动安装新插件：" + bundleName + "  " + bundleVersion);
			newBundle = bundleContext.installBundle(bundleInfo.getBundleName(),
					bundleInfo.getBundleInputStream());
		}// 否则更新
		else {
			if (bundleVersion.compareTo(preBundle.getVersion()) >= 0) {
				preBundle.stop();
				logger.info("自动将插件：" + bundleName + " 由 "
						+ preBundle.getVersion() + "更新到" + bundleVersion);
				preBundle.update(bundleInfo.getBundleInputStream());
				newBundle = preBundle;
			} else {
				logger.warning("插件：" + bundleName + "的版本" + bundleVersion
						+ "小于已安装的版本" + preBundle.getVersion() + "，没有应用更新！");
			}
		}
		return newBundle;
	}

	/**
	 * 得到插件方法的URL
	 * 
	 * @param bundleName
	 * @param methodName
	 * @return
	 */
	public static String getBundleMethodUrl(String bundleName, String methodName) {
		if (StringUtils.isEmpty(bundleMethodUrlTemplate))
			return "Missing bundleMethodUrlTemplate";
		return String.format(bundleMethodUrlTemplate, bundleName, methodName);
	}

	/**
	 * 根据名称得到Bundle
	 * 
	 * @param bundleContext
	 * @param bundleName
	 * @return
	 */
	public static Bundle getBundleByName(BundleContext bundleContext,
			String bundleName) {
		if (bundleContext == null) {
			return null;
		}
		Bundle[] bundles = bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			if (bundleName.equals(bundle.getSymbolicName()))
				return bundle;
		}
		return null;
	}

	/**
	 * 得到指定Bundle的信息
	 * 
	 * @param bundle
	 * @return
	 */
	public static BundleInfo getBundleInfo(Bundle bundle) {
		try {
			BundleInfo newBundleInfo = new BundleInfo();
			Properties prop = new Properties();
			Dictionary<String, String> dict = bundle.getHeaders();
			Enumeration<String> keyEnumeration = dict.keys();
			while (keyEnumeration.hasMoreElements()) {
				String key = keyEnumeration.nextElement();
				prop.put(key, dict.get(key));
			}
			newBundleInfo.loadProperties(prop);
			return newBundleInfo;
		} catch (Exception ex) {
			logger.warning(String
					.format("[com.quickwebframework.util.BundleUtil.getBundleInfo]警告：读取插件[%s]的资源文件[%s]时出错，原因：[%s]",
							bundle.getSymbolicName(),
							BundleInfo.METAINF_FILE_PATH, ex));
			return null;
		}
	}

	/**
	 * 得到OSGi容器中已安装的全部插件的信息列表
	 * 
	 * @param bundleContext
	 * @return
	 */
	public static List<BundleInfo> getAllBundleInfoList(
			BundleContext bundleContext) {
		if (bundleContext == null) {
			return null;
		}

		List<BundleInfo> list = new ArrayList<BundleInfo>();

		Bundle[] bundles = bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			BundleInfo bundleInfo = getBundleInfo(bundle);
			list.add(bundleInfo);
		}
		return list;
	}

	// 得到Bundle名称列表(主要是为了得到最新的list中的顺序)
	private static List<String> getBundleNameList(List<BundleInfo> list) {
		List<String> bundleNameList = new ArrayList<String>();
		for (BundleInfo bundleInfo : list) {
			bundleNameList.add(bundleInfo.getBundleName());
		}
		return bundleNameList;
	}

	// 根据Bundle的依赖关系，排列出安装顺序
	private static void orderBundleInstallList(List<BundleInfo> list) {
		// 用于查询
		List<String> bundleNameList = getBundleNameList(list);
		// 导出包名与Bundle名对应Map
		Map<String, String> exportPackageBundleNameMap = new HashMap<String, String>();
		for (BundleInfo bundleInfo : list) {
			for (String exportPackage : bundleInfo.getExportPackageList()) {
				exportPackageBundleNameMap.put(exportPackage,
						bundleInfo.getBundleName());
			}
		}

		// 开始排序
		for (int i = 0; i < list.size();) {
			// 是否有对象移动
			boolean isItemMoved = false;

			BundleInfo bundleInfo = list.get(i);

			// 根据Require-Bundle排序
			for (String requireBundleName : bundleInfo
					.getRequireBundleNameList()) {
				// 如果依赖的Bundle不在要安装的插件列表中，则忽略
				if (!bundleNameList.contains(requireBundleName))
					continue;

				int requireBundleIndex = bundleNameList
						.indexOf(requireBundleName);

				// 如果需要的包在此包后面，则移动到前面
				if (requireBundleIndex > i) {
					BundleInfo requireBundleInfo = list.get(requireBundleIndex);
					list.remove(requireBundleIndex);
					list.add(i, requireBundleInfo);

					bundleNameList = getBundleNameList(list);
					isItemMoved = true;
					i++;
					logger.config(String
							.format("安装/更新顺序自动计算算法：因为插件[%s]需要插件[%s]，所以将插件[%s]移动到[%s]前面。",
									bundleInfo.getBundleName(),
									requireBundleName, requireBundleName,
									bundleInfo.getBundleName()));
				}
			}

			// 根据Import-Package排序
			for (String importPackage : bundleInfo.getImportPackageList()) {
				// 如果导入的包不在要安装的插件的导出包列表中，则忽略
				if (!exportPackageBundleNameMap.containsKey(importPackage))
					continue;
				String importPackageBelongBundleName = exportPackageBundleNameMap
						.get(importPackage);
				int importPackageBelongBundleIndex = bundleNameList
						.indexOf(importPackageBelongBundleName);

				// 如果需要的包在此包后面，则移动到前面
				if (importPackageBelongBundleIndex > i) {
					BundleInfo importPackageBelongBundle = list
							.get(importPackageBelongBundleIndex);
					list.remove(importPackageBelongBundleIndex);
					list.add(i, importPackageBelongBundle);

					bundleNameList = getBundleNameList(list);
					isItemMoved = true;
					i++;
					logger.warning(String
							.format("安装/更新顺序自动计算算法：因为插件[%s]导入了插件[%s]的包[%s]，所以将插件[%s]移动到[%s]前面。",
									bundleInfo.getBundleName(),
									importPackageBelongBundleName,
									importPackage,
									importPackageBelongBundleName,
									bundleInfo.getBundleName()));
				}

			}
			if (isItemMoved) {
				i = 0;
			} else {
				i++;
			}
		}
	}

	// 得到应该刷新的Bundle
	private static List<BundleInfo> getShouldRefreshBundleInfoList(
			List<BundleInfo> installedBundleInfoList,
			List<BundleInfo> allBundleInfoList) {
		// 已安装全部插件信息Map
		Map<String, BundleInfo> allBundleInfoMap = new HashMap<String, BundleInfo>();
		for (BundleInfo bundleInfo : allBundleInfoList) {
			allBundleInfoMap.put(bundleInfo.getBundleName(), bundleInfo);
		}

		// 应该刷新的插件名称列表
		List<String> shouldRefreshBundleNameList = new ArrayList<String>();
		// 应该刷新的插件信息列表
		List<BundleInfo> shouldRefreshBundleInfoList = new ArrayList<BundleInfo>();

		for (BundleInfo bundleInfo : installedBundleInfoList) {
			shouldRefreshBundleNameList.add(bundleInfo.getBundleName());
			shouldRefreshBundleInfoList.add(bundleInfo);
		}

		for (int i = 0; i < shouldRefreshBundleInfoList.size(); i++) {
			BundleInfo bundleInfo = shouldRefreshBundleInfoList.get(i);
			String bundleName = bundleInfo.getBundleName();
			List<String> bundleExportPackageList = bundleInfo
					.getExportPackageList();

			// 搜索全部的Bundle
			for (BundleInfo tmpBundleInfo : allBundleInfoList) {
				boolean isTrue = false;

				String tmpBundleName = tmpBundleInfo.getBundleName();

				// 如果满足Require-Bundle依赖关系
				if (tmpBundleInfo.getRequireBundleNameList().contains(
						bundleName)) {
					isTrue = true;
				}
				// 否则如果满足Import-Package依赖关系
				else {
					List<String> tmpBundleInfoImportPackageList = tmpBundleInfo
							.getImportPackageList();
					for (String tmpBundleInfoImportPackage : tmpBundleInfoImportPackageList) {
						if (bundleExportPackageList
								.contains(tmpBundleInfoImportPackage)) {
							isTrue = true;
							break;
						}
					}
				}
				// 如果满足依赖关系，并且未加入应该刷新的列表中
				if (isTrue
						&& !shouldRefreshBundleNameList.contains(tmpBundleName)) {
					shouldRefreshBundleNameList.add(tmpBundleName);
					shouldRefreshBundleInfoList.add(tmpBundleInfo);
				}
			}
		}
		return shouldRefreshBundleInfoList;
	}
}
package com.quickwebframework.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class WebResourceUtils {

	static {
		defaultMimeMap = new HashMap<String, String>();
		initDefaultMime();
	}

	private static Map<String, String> defaultMimeMap;

	// 初始化默认的MIME类型
	private static void initDefaultMime() {
		defaultMimeMap.put("323", "text/h323");
		defaultMimeMap.put("acx", "application/internet-property-stream");
		defaultMimeMap.put("ai", "application/postscript");
		defaultMimeMap.put("aif", "audio/x-aiff");
		defaultMimeMap.put("aifc", "audio/x-aiff");
		defaultMimeMap.put("aiff", "audio/x-aiff");
		defaultMimeMap.put("asf", "video/x-ms-asf");
		defaultMimeMap.put("asr", "video/x-ms-asf");
		defaultMimeMap.put("asx", "video/x-ms-asf");
		defaultMimeMap.put("au", "audio/basic");
		defaultMimeMap.put("avi", "video/x-msvideo");
		defaultMimeMap.put("axs", "application/olescript");
		defaultMimeMap.put("bas", "text/plain");
		defaultMimeMap.put("bcpio", "application/x-bcpio");
		defaultMimeMap.put("bin", "application/octet-stream");
		defaultMimeMap.put("bmp", "image/bmp");
		defaultMimeMap.put("c", "text/plain");
		defaultMimeMap.put("cat", "application/vndms-pkiseccat");
		defaultMimeMap.put("cdf", "application/x-cdf");
		defaultMimeMap.put("cer", "application/x-x509-ca-cert");
		defaultMimeMap.put("class", "application/octet-stream");
		defaultMimeMap.put("clp", "application/x-msclip");
		defaultMimeMap.put("cmx", "image/x-cmx");
		defaultMimeMap.put("cod", "image/cis-cod");
		defaultMimeMap.put("cpio", "application/x-cpio");
		defaultMimeMap.put("crd", "application/x-mscardfile");
		defaultMimeMap.put("crl", "application/pkix-crl");
		defaultMimeMap.put("crt", "application/x-x509-ca-cert");
		defaultMimeMap.put("csh", "application/x-csh");
		defaultMimeMap.put("css", "text/css");
		defaultMimeMap.put("dcr", "application/x-director");
		defaultMimeMap.put("der", "application/x-x509-ca-cert");
		defaultMimeMap.put("dir", "application/x-director");
		defaultMimeMap.put("dll", "application/x-msdownload");
		defaultMimeMap.put("dms", "application/octet-stream");
		defaultMimeMap.put("doc", "application/msword");
		defaultMimeMap.put("dot", "application/msword");
		defaultMimeMap.put("dvi", "application/x-dvi");
		defaultMimeMap.put("dxr", "application/x-director");
		defaultMimeMap.put("eps", "application/postscript");
		defaultMimeMap.put("etx", "text/x-setext");
		defaultMimeMap.put("evy", "application/envoy");
		defaultMimeMap.put("exe", "application/octet-stream");
		defaultMimeMap.put("fif", "application/fractals");
		defaultMimeMap.put("flr", "x-world/x-vrml");
		defaultMimeMap.put("gif", "image/gif");
		defaultMimeMap.put("gtar", "application/x-gtar");
		defaultMimeMap.put("gz", "application/x-gzip");
		defaultMimeMap.put("h", "text/plain");
		defaultMimeMap.put("hdf", "application/x-hdf");
		defaultMimeMap.put("hlp", "application/winhlp");
		defaultMimeMap.put("hqx", "application/mac-binhex40");
		defaultMimeMap.put("hta", "application/hta");
		defaultMimeMap.put("htc", "text/x-component");
		defaultMimeMap.put("htm", "text/html");
		defaultMimeMap.put("html", "text/html");
		defaultMimeMap.put("htt", "text/webviewhtml");
		defaultMimeMap.put("ico", "image/x-icon");
		defaultMimeMap.put("ief", "image/ief");
		defaultMimeMap.put("iii", "application/x-iphone");
		defaultMimeMap.put("ins", "application/x-internet-signup");
		defaultMimeMap.put("isp", "application/x-internet-signup");
		defaultMimeMap.put("jfif", "image/pipeg");
		defaultMimeMap.put("jpe", "image/jpeg");
		defaultMimeMap.put("jpeg", "image/jpeg");
		defaultMimeMap.put("jpg", "image/jpeg");
		defaultMimeMap.put("js", "application/x-javascript");
		defaultMimeMap.put("latex", "application/x-latex");
		defaultMimeMap.put("lha", "application/octet-stream");
		defaultMimeMap.put("lsf", "video/x-la-asf");
		defaultMimeMap.put("lsx", "video/x-la-asf");
		defaultMimeMap.put("lzh", "application/octet-stream");
		defaultMimeMap.put("m13", "application/x-msmediaview");
		defaultMimeMap.put("m14", "application/x-msmediaview");
		defaultMimeMap.put("m3u", "audio/x-mpegurl");
		defaultMimeMap.put("man", "application/x-troff-man");
		defaultMimeMap.put("mdb", "application/x-msaccess");
		defaultMimeMap.put("me", "application/x-troff-me");
		defaultMimeMap.put("mht", "message/rfc822");
		defaultMimeMap.put("mhtml", "message/rfc822");
		defaultMimeMap.put("mid", "audio/mid");
		defaultMimeMap.put("mny", "application/x-msmoney");
		defaultMimeMap.put("mov", "video/quicktime");
		defaultMimeMap.put("movie", "video/x-sgi-movie");
		defaultMimeMap.put("mp2", "video/mpeg");
		defaultMimeMap.put("mp3", "audio/mpeg");
		defaultMimeMap.put("mpa", "video/mpeg");
		defaultMimeMap.put("mpe", "video/mpeg");
		defaultMimeMap.put("mpeg", "video/mpeg");
		defaultMimeMap.put("mpg", "video/mpeg");
		defaultMimeMap.put("mpp", "application/vndms-project");
		defaultMimeMap.put("mpv2", "video/mpeg");
		defaultMimeMap.put("ms", "application/x-troff-ms");
		defaultMimeMap.put("mvb", "application/x-msmediaview");
		defaultMimeMap.put("nws", "message/rfc822");
		defaultMimeMap.put("oda", "application/oda");
		defaultMimeMap.put("p10", "application/pkcs10");
		defaultMimeMap.put("p12", "application/x-pkcs12");
		defaultMimeMap.put("p7b", "application/x-pkcs7-certificates");
		defaultMimeMap.put("p7c", "application/x-pkcs7-mime");
		defaultMimeMap.put("p7m", "application/x-pkcs7-mime");
		defaultMimeMap.put("p7r", "application/x-pkcs7-certreqresp");
		defaultMimeMap.put("p7s", "application/x-pkcs7-signature");
		defaultMimeMap.put("pbm", "image/x-portable-bitmap");
		defaultMimeMap.put("pdf", "application/pdf");
		defaultMimeMap.put("pfx", "application/x-pkcs12");
		defaultMimeMap.put("pgm", "image/x-portable-graymap");
		defaultMimeMap.put("pko", "application/yndms-pkipko");
		defaultMimeMap.put("pma", "application/x-perfmon");
		defaultMimeMap.put("pmc", "application/x-perfmon");
		defaultMimeMap.put("pml", "application/x-perfmon");
		defaultMimeMap.put("pmr", "application/x-perfmon");
		defaultMimeMap.put("pmw", "application/x-perfmon");
		defaultMimeMap.put("png", "image/png");
		defaultMimeMap.put("pnm", "image/x-portable-anymap");
		defaultMimeMap.put("pot", "application/vndms-powerpoint");
		defaultMimeMap.put("ppm", "image/x-portable-pixmap");
		defaultMimeMap.put("pps", "application/vndms-powerpoint");
		defaultMimeMap.put("ppt", "application/vndms-powerpoint");
		defaultMimeMap.put("prf", "application/pics-rules");
		defaultMimeMap.put("ps", "application/postscript");
		defaultMimeMap.put("pub", "application/x-mspublisher");
		defaultMimeMap.put("qt", "video/quicktime");
		defaultMimeMap.put("ra", "audio/x-pn-realaudio");
		defaultMimeMap.put("ram", "audio/x-pn-realaudio");
		defaultMimeMap.put("ras", "image/x-cmu-raster");
		defaultMimeMap.put("rgb", "image/x-rgb");
		defaultMimeMap.put("rmi", "audio/mid");
		defaultMimeMap.put("roff", "application/x-troff");
		defaultMimeMap.put("rtf", "application/rtf");
		defaultMimeMap.put("rtx", "text/richtext");
		defaultMimeMap.put("scd", "application/x-msschedule");
		defaultMimeMap.put("sct", "text/scriptlet");
		defaultMimeMap.put("setpay", "application/set-payment-initiation");
		defaultMimeMap.put("setreg", "application/set-registration-initiation");
		defaultMimeMap.put("sh", "application/x-sh");
		defaultMimeMap.put("shar", "application/x-shar");
		defaultMimeMap.put("sit", "application/x-stuffit");
		defaultMimeMap.put("snd", "audio/basic");
		defaultMimeMap.put("spc", "application/x-pkcs7-certificates");
		defaultMimeMap.put("spl", "application/futuresplash");
		defaultMimeMap.put("src", "application/x-wais-source");
		defaultMimeMap.put("sst", "application/vndms-pkicertstore");
		defaultMimeMap.put("stl", "application/vndms-pkistl");
		defaultMimeMap.put("stm", "text/html");
		defaultMimeMap.put("svg", "image/svg+xml");
		defaultMimeMap.put("sv4cpio", "application/x-sv4cpio");
		defaultMimeMap.put("sv4crc", "application/x-sv4crc");
		defaultMimeMap.put("swf", "application/x-shockwave-flash");
		defaultMimeMap.put("t", "application/x-troff");
		defaultMimeMap.put("tar", "application/x-tar");
		defaultMimeMap.put("tcl", "application/x-tcl");
		defaultMimeMap.put("tex", "application/x-tex");
		defaultMimeMap.put("texi", "application/x-texinfo");
		defaultMimeMap.put("texinfo", "application/x-texinfo");
		defaultMimeMap.put("tgz", "application/x-compressed");
		defaultMimeMap.put("tif", "image/tiff");
		defaultMimeMap.put("tiff", "image/tiff");
		defaultMimeMap.put("tr", "application/x-troff");
		defaultMimeMap.put("trm", "application/x-msterminal");
		defaultMimeMap.put("tsv", "text/tab-separated-values");
		defaultMimeMap.put("txt", "text/plain");
		defaultMimeMap.put("uls", "text/iuls");
		defaultMimeMap.put("ustar", "application/x-ustar");
		defaultMimeMap.put("vcf", "text/x-vcard");
		defaultMimeMap.put("vrml", "x-world/x-vrml");
		defaultMimeMap.put("wav", "audio/x-wav");
		defaultMimeMap.put("wcm", "application/vndms-works");
		defaultMimeMap.put("wdb", "application/vndms-works");
		defaultMimeMap.put("wks", "application/vndms-works");
		defaultMimeMap.put("wmf", "application/x-msmetafile");
		defaultMimeMap.put("wps", "application/vndms-works");
		defaultMimeMap.put("wri", "application/x-mswrite");
		defaultMimeMap.put("wrl", "x-world/x-vrml");
		defaultMimeMap.put("wrz", "x-world/x-vrml");
		defaultMimeMap.put("xaf", "x-world/x-vrml");
		defaultMimeMap.put("xbm", "image/x-xbitmap");
		defaultMimeMap.put("xla", "application/vndms-excel");
		defaultMimeMap.put("xlc", "application/vndms-excel");
		defaultMimeMap.put("xlm", "application/vndms-excel");
		defaultMimeMap.put("xls", "application/vndms-excel");
		defaultMimeMap.put("xlt", "application/vndms-excel");
		defaultMimeMap.put("xlw", "application/vndms-excel");
		defaultMimeMap.put("xof", "x-world/x-vrml");
		defaultMimeMap.put("xpm", "image/x-xpixmap");
		defaultMimeMap.put("xwd", "image/x-xwindowdump");
		defaultMimeMap.put("z", "application/x-compress");
		defaultMimeMap.put("zip", "application/zip");
	}

	/**
	 * 输出资源
	 * 
	 * @param response
	 * @param path
	 * @param input
	 * @throws IOException
	 */
	public static void outputResource(HttpServletResponse response,
			String path, InputStream input) throws IOException {
		outputResource(response, path, input, defaultMimeMap);
	}

	/**
	 * 
	 * @param response
	 * @param path
	 * @param input
	 * @param mimeMap
	 *            允许的MIME映射
	 * @throws IOException
	 */
	public static void outputResource(HttpServletResponse response,
			String path, InputStream input, Map<String, String> mimeMap)
			throws IOException {
		// 设置Content-Type
		String[] tmpStrs = StringUtils.split(path, ".");
		String contentType = null;
		if (tmpStrs.length <= 1) {
			response.sendError(400, "请求的资源[" + path + "]未包括后缀，不允许访问！");
			return;
		}
		String fileExtenion = tmpStrs[tmpStrs.length - 1];
		if (mimeMap.containsKey(fileExtenion)) {
			contentType = mimeMap.get(fileExtenion);
		}

		if (contentType == null) {
			throw new IOException("未知的MIME类型：" + fileExtenion);
		}
		response.setContentType(contentType);
		// 输出
		OutputStream output = response.getOutputStream();
		IOUtils.copyLarge(input, output);
		output.flush();
		input.close();
	}
}

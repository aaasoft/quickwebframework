<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>线程信息</title>
</head>
<body>
	<p>
		<b>线程信息</b>
	</p>
	<#include "qwf.test.core:navbar">
	<table>
		<tbody>
			<#list allStackTraces?keys as thread>
			<!-- 分隔 -->
			<tr>
				<td><b>线程[${thread}]</b></td>
			</tr>
			<#if allStackTraces[thread]??>
			<tr>
				<td><#list allStackTraces[thread] as stackTrace> <!-- 分隔 -->
					<p style="margin-left: 20px; margin-top: 0px; margin-bottom: 0px">${stackTrace}</p>
					<!-- 分隔 --></#list>
				</td>
			</tr>
			</#if>
			<!-- 分隔 -->
			</#list>
		</tbody>
	</table>
</body>
</html>
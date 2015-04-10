<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>监听器信息</title>
</head>
<body>
	<p>
		<b>监听器信息</b>
	</p>
	<#include "qwf.test.core:navbar">
	<table>
		<tbody>
			<#list listenersMap?keys as className>
			<!-- 分隔 -->
			<#if listenersMap[className]??>
			<tr>
				<td><b>[${className}]类型</b></td>
			</tr>
			<#list listenersMap[className] as listener>
			<tr>
				<td><p style="margin-left: 20px">${listener}</p></td>
			</tr>
			</#list>
			<!-- 分隔 -->
			</#if>
			<!-- 分隔 -->
			</#list>
		</tbody>
	</table>
</body>
</html>
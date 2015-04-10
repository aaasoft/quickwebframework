<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>过滤器信息</title>
</head>
<body>
	<p>
		<b>过滤器信息</b>
	</p>
	<#include "qwf.test.core:navbar">
	<table>
		<tbody>
			<tr>
				<td><b>过滤器列表</b></td>
			</tr>
			<#list filters as filter>
			<tr>
				<td><p style="margin-left: 20px">${filter}</p></td>
			</tr>
			</#list>
		</tbody>
	</table>
</body>
</html>
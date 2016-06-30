<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<base href="<%=basePath%>">
	<title>Insert title here</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes" />
</head>

<body>

	<div style="background-color: rgb(202, 234, 202)">
		<a href="#">图片</a>
		<form method="post" action="user/index.home" method="post" enctype="multipart/form-data">
			<table border="1" cellpadding="0" cellspacing="0" style="width: 100%">
				<tr>
					<td width="20%">名称</td>
					<td width="80%">内容</td>
				</tr>
				<tr>
					<td>file</td>
					<td><input type="file" name="file" value=''/></td>
				</tr>
				<tr>
					<td>点击测试</td>
					<td><input type="submit" value="测试"></td>
				</tr>
			</table>
		</form>
	</div>

</body>

</html>
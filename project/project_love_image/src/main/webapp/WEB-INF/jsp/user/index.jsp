<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<title>Insert title here</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes" />
</head>

<body>

	<div style="background-color: rgb(202, 234, 202); width: 100%; height: 100%">
		<a href="#">图片</a>
		<form method="post" action="user/index.home" method="post" enctype="multipart/form-data">
			<table border="1" cellpadding="0" cellspacing="0" style="width: 100%">
				<tr>
					<td width="20%">名称</td>
					<td width="80%">内容</td>
				</tr>
				<tr>
					<td>auth</td>
					<td><input type="text" name="auth" value='{"imei":"123456789"}'/></td>
				</tr>
				<tr>
					<td>info</td>
					<td><input type="text" name="info" value='{"userId":"1"}'/></td>
				</tr>
				<tr>
					<td>avatar</td>
					<td><input type="file" name="file" value=''/></td>
				</tr>
				<tr>
					<td>点击测试</td>
					<td><input type="submit" value="测试"></td>
				</tr>
			</table>
		</form>
	<input>
	
	</div>

</body>

</html>
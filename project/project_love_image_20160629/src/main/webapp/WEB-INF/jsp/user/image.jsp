<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<base href="<%=basePath%>">
	<title>Insert title here</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes" />
	
	<style type="text/css">
		/* bj */
	</style>
	
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
		
		<div style="background-color: rgb(202, 234, 202)">
			<table border="1" bordercolor="#d2d8db" width="95%" align="center" style="text-align:center; font-size:13px;">
				<tr style="height: 50px">
					<!-- 
					 -->
					<td width="3%"  bgcolor="#e5e5e5"><input id="checkbox_all" name="checkbox_all" type="checkbox" onclick="checkAll()"></td>
					<td width="3%"  bgcolor="#e5e5e5">序号</td>
					<td width="10%" bgcolor="#e5e5e5">标题</td>
					<td width="80%" bgcolor="#e5e5e5">图片</td>
				</tr>
				
				<c:forEach items="${image}" var="m" varStatus="p">
					<tr>
						<!-- 
						 -->
						<td>
							<input name="checkbox_name" type="checkbox" value="${m.id}">
						</td>
						<td>
							${p.count+(pageModel.currentPage-1)*pageModel.pageSize}
						</td>
						
						<td title="">${m.id }</td>
						<td title="">
							<c:if test="${not empty m.name }">
								<a title="" href="${m.name }" target="_black" >
								<img alt="" src="${m.name }" width="500px" height="500px" /></a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>

</body>

</html>
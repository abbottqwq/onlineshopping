<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhuohui
  Date: 12/15/20
  Time: 6:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Access Denied</title>
</head>
<body>
<h1>Access Denied!</h1>
<h3 style="color:red">Sorry, you can not access this page!</h3>
<a href="<spring:url value="/home"/>">go home</a>
</body>
</html>

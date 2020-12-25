<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhuohui
  Date: 11/27/20
  Time: 5:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h>${pageContext.request.userPrincipal.name}</h>
<br>
<security:authorize access="isAuthenticated()">
    authenticated as <security:authentication property="principal"/><br>
</security:authorize>
</body>
</html>

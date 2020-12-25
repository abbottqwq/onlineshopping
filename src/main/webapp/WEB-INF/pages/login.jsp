<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhuohui
  Date: 12/15/20
  Time: 6:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href='<spring:url value="/css/styles.css"/>' rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.5.0.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <title>Log in</title>
</head>
<body>
<form action="<spring:url value="/spring_security_check"/>" method="post">
    <table>
        <tr>
            <td>User Name</td>
            <td><input name="username"/></td>
        </tr>

        <tr>
            <td>Password</td>
            <td><input type="password" name="password"/></td>
        </tr>
        <%--        <tr>--%>
        <%--            <td>Remember Me:</td>--%>
        <%--            <td><input type="checkbox" name="remember-me" /></td>--%>
        <%--        </tr>--%>

        <tr>
            <td><input type="submit" value="Login"/></td>
            <td><c:if test="${param.error == 'true'}"><label style="color: red">login error</label></c:if></td>
        </tr>
    </table>
</form>
<a href="<spring:url value="/home"/>">go home</a>
</body>
<script>
    <c:if test="${param.error == 'true'}">
        alert("log in error");
    </c:if>
</script>
</html>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhuohui
  Date: 12/17/20
  Time: 7:51 PM
  To change this template use File | Settings | File Templates.
`
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href='<spring:url value="/css/styles.css"/>' rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.5.0.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <title>Change Password</title>
    <style>
        p {
            color: red;
        }
    </style>
</head>
<body>
<jsp:include page="_header.jsp"/>
<form:form action="changepassword" method="post">
    <label>old password<input type="password" required name="oldpassword"/></label>
    <p>${error1}</p><br>
    <label>new password<input type="password" required name="newpassword"/></label>
    <p>${error2}</p><br>
    <label>confirmed password<input type="password" required name="cnewpassword"/>
        <p>${error3}</p></label><br>
    <input type="submit" value="submit"/>
</form:form>
</body>
</html>

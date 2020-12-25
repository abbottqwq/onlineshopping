<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhuohui
  Date: 12/18/20
  Time: 3:16 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>checkout</title>
    <link href='<spring:url value="/css/styles.css"/>' rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.5.0.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
</head>
<body>
<h>test checkout result</h>
<form action="checkout" method="post">
    <label>
        <input type="radio" name="result" value="success"/>
        success
    </label>
    <br>
    <label>
        <input type="radio" name="result" value="fail"/>
        fail
    </label>
    <input type="submit" value="submit"/>
</form>
</body>
</html>

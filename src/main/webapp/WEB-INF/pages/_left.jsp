<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhuohui
  Date: 12/16/20
  Time: 7:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<div class="left">
    <a data-pid="-1" href="#">All</a></br>
    <c:forEach items="${producttype}" var="type">
        <a data-pid="${type.key}" href="#">${type.value}</a></br>
    </c:forEach>
</div>
</body>
</html>

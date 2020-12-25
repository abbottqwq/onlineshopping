<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhuohui
  Date: 12/16/20
  Time: 8:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href='<spring:url value="/css/styles.css"/>' rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.5.0.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <title>Shopping Cart</title>
</head>
<body>
<jsp:include page="_header.jsp"/>
<div class="right">
    <c:if test="${empty shoppingcart}">
    <h3>No Things in shopping cart</h3>
    </c:if>
    <c:if test="${not empty shoppingcart}">
    <form action="<spring:url value="/customer/updateshoppingcart"/>" method="post">
        <table>
            <th>No.</th>
            <th>Name</th>
            <th>Quantity</th>
            <c:forEach items="${shoppingcart}" var="item" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${item.name}</td>
                    <td><input value="${item.quantity}" name="quantity[]" type="number" min="0" step="1"/><input
                            type="hidden" name="id[]" value="${item.productID}"/></td>
                </tr>
            </c:forEach>
        </table>
        <input type="submit" value="Update"/>
    </form>
        <a href="<spring:url value="/customer/checkout"/>"><button>checkout</button></a>
    </c:if>
</body>
<script>
    <c:if test="${param.checkfail == 'true'}">
    alert("check out fail")
    </c:if>
</script>
</html>

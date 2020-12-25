<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhuohui
  Date: 12/18/20
  Time: 6:40 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>order history</title>
    <link href='<spring:url value="/css/styles.css"/>' rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.5.0.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
</head>
<body>
<jsp:include page="_header.jsp"/>
<c:if test="${empty ordermodel}">
    <h3>No Order History</h3>
</c:if>
<c:forEach items="${ordermodel}" var="order" varStatus="i">
    <c:if test="${not empty order}">
        <table>
            <caption>Order No.${i.index+1} at ${order.modifyDate}</caption>
            <tr>
                <th>No.</th>
                <th>name</th>
                <th>price</th>
                <th>quantity</th>
                <th>amount</th>
            </tr>
            <c:forEach items="${order.orderProductModelsOrderByProductID}" var="orderProduct" varStatus="j">
                <tr>
                    <td>${j.index+1}</td>
                    <td>${orderProduct.name}</td>
                    <td>$ ${orderProduct.price}</td>
                    <td>${orderProduct.quantity}</td>
                    <td>${orderProduct.amount}</td>
                </tr>
            </c:forEach>
            <tr>
                <th>Total:</th>
                <td colspan="4">${order.totalAmount}</td>
            </tr>
        </table>
        <br>
    </c:if>
</c:forEach>
</body>
</html>

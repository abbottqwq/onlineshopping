<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhuohui
  Date: 12/16/20
  Time: 1:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.edu.neu.project.authority.AuthorityEnum" %>
<html>

<body>
<div>

    <div class="site-name">Online Shop</div>

    <div class="header-right">
        <a href="<spring:url value="/home"/>">home</a>
        <c:out value="  |  "/>
        <c:if test="${pageContext.request.userPrincipal.name != null}">
            Hello
            <a href="<spring:url value="/account/accountinfo"/>"><label id="displaynameheader"></label></a>
            <security:authorize access="hasAnyAuthority('${AuthorityEnum.Customer.authority}')">
                <c:out value="  |  "/>
                <a href="<spring:url value="/customer/showshoppingcart"/>">Shopping Cart</a>
            </security:authorize>
            <c:out value="  |  "/>
            <a href="<spring:url value="/logout"/>">Logout</a>

        </c:if>
        <c:if test="${pageContext.request.userPrincipal.name == null}">
            <a href="<spring:url value="/login"/>">Login</a>
            <c:out value="  |  "/>
            <a href="<spring:url value="/registration"/>">Register</a>
        </c:if>
    </div>
</div>
</body>

<script>
    $(document).ready(() => {
        let userPrincipal = "${pageContext.request.userPrincipal}";
        $.ajax({
            type: "get",
            url: "<spring:url value="/account/getdisplayname"/>",
            success: (data) => {
                $("#displaynameheader").text(data);
            }
        })
    })
</script>
</html>

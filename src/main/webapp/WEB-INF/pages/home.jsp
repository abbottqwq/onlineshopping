<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<body>
<h2>Hello World!</h2>

<a href="${pageContext.request.contextPath}/register">test register</a><br/>
<a href="${pageContext.request.contextPath}/productmanager/upload">test upload products</a><br/>
<a href="${pageContext.request.contextPath}/listproduct">list product</a><br/>

<a href="<spring:url value="/registration"/>">registration </a><br/>
<a href="<spring:url value="/test"/>">test </a><br/>
<a href="<spring:url value="/login"/>">test login </a><br/>
<img src="<spring:url value='/product/image/6'/>">

</body>
<script src="https://code.jquery.com/jquery-3.5.0.js"></script>
</html>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  UserAccount: lizhuohui
  Date: 11/26/20
  Time: 10:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <link href='<spring:url value="/css/styles.css"/>' rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.5.0.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <title>Upload Product</title>
    <style>
        p {
            color: red;
        }

    </style>
</head>

<body>
<jsp:include page="_header.jsp"/>
<form:form action="upload" method="post" modelAttribute="productinfoform" enctype="multipart/form-data">

    <table style="text-align: left">
        <tr>
            <td>Name</td>
            <td><input id="name" type="text" name="name" value="${productinfoform.name}"/> <form:errors
                    path="name" cssClass="valid-error"/></td>
            <td><p id="nameerror"></p></td>

        </tr>
        <tr>
            <td>price</td>
            <td><input id="number" type="number" step="0.01" min="0" name="price"
                       value="${productinfoform.price}"/><form:errors
                    path="price" cssClass="valid-error"/></td
            <td><p id="priceerror"></p></td>
        </tr>
        <tr>
            <td>type</td>
            <td>
                <select name="productTypeID">
                    <c:forEach items="${producttype}" var="type">
                        <option value="${type.key}">${type.value}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>Image</td>
            <td>
                <div id="imgpreview"></div>
            </td>
        </tr>
        <tr>
            <td>Upload Image</td>
            <td><input required type="file" accept="image/*" name="imgFile" multiple="multiple"
                       id="imgFile"><form:errors
                    path="imgFile" cssClass="valid-error"/></td>
        </tr>
        <tr>
            <td>Description</td>
            <td><textarea id="description" rows="8" name="description"
            >${productinfoform.description}</textarea><form:errors
                    path="description" cssClass="valid-error"/></td>
            <td><p id="descriptionerror"></p></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Submit"/></td>
        </tr>
    </table>
</form:form>

</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src='<spring:url value="/js/imgpreview.js"/>'></script>
<script>
    function findInvalidChars(string) {
        let regex = RegExp("^[a-zA-Z0-9_]*$");
        //console.log(string + " " + regex.test(string))
        return !regex.test(string);
    }

    function checkPriceValue(p) {
        const regex = /^\d+(.\d{1,2})?$/gm;
        return regex.test(p)
    }

    function findXSSString(string) {
        const regex = /[\<]\w+[\>]/gm;
        return regex.test(string)
    }

    $("#name").on('keyup change', function (e) {
        let inp = $("#name").val();
        let message = "";
        if (findInvalidChars(inp)) message += "Invalid input; ";
        if (inp.length < 5) message += "Too Short; ";
        if (inp.length > 40) message += "Too Long; ";
        $("#nameerror").text(message);
    });

    $("#price").on('keyup change', function (e) {
        let inp = $("#price").val();
        let message = "";
        if (!checkPriceValue(inp)) message += "Invalid price"
        $("#priceerror").text(message);
    });

    $("#description").on('keyup change', function (e) {
        let inp = $("#description").val();
        let message = "";
        if (findXSSString(inp)) message += "Find XSS"
        $("#descriptionerror").text(message);
    });


</script>
</html>

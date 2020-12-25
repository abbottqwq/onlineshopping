<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhuohui
  Date: 12/17/20
  Time: 1:16 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.edu.neu.project.authority.AuthorityEnum" %>
<html>
<head>
    <title>Account Info</title>
    <link href='<spring:url value="/css/styles.css"/>' rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.5.0.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
</head>
<body>
<jsp:include page="_header.jsp"/>
<security:authorize
        access="hasAnyAuthority('${AuthorityEnum.Customer.authority}','${AuthorityEnum.Manager.authority}','${AuthorityEnum.Admin.authority}')">
    <p>username: ${username}</p><br>

    <input type="text" name="displayname" id="displayName" value="${displayname}"/>
    <button type="button" id="submitdisplayname"
            disabled>change
    </button>
    <p style="color: red" id="displayNameError"></p>
    <br>
    <a href="<spring:url value="/account/changepassword"/>">change password</a><br>
</security:authorize>

<security:authorize access="hasAnyAuthority('${AuthorityEnum.Customer.authority}')">
    <a href="<spring:url value="/customer/orderhistory"/>">view order history</a><br>
</security:authorize>

<security:authorize access="hasAnyAuthority('${AuthorityEnum.Manager.authority}','${AuthorityEnum.Admin.authority}')">
    <a href="<spring:url value="/listproduct"/>">manager product</a><br>
    <a href="<spring:url value="/productmanager/upload"/>">upload product</a><br>
</security:authorize>
<security:authorize access="hasAnyAuthority('${AuthorityEnum.Admin.authority}')">
    <a href="<spring:url value="/admin/registerproductmanager"/>">register product manager</a><br>
    <div class="ui-widget">
        <label for="productmanagerusername">delete product manager by username:</label>
        <input id="productmanagerusername">
    </div>
    <button type="button" id="deletebutton">delete</button>
    <br>
</security:authorize>
</body>
<script>
    <security:authorize access="hasAnyAuthority('${AuthorityEnum.Admin.authority}')">

    $.get("/admin/getproductmanagerusernamelist", (res) => {
            let productManagerUsernames = res;
            $("#productmanagerusername").autocomplete({
                source: productManagerUsernames
            })
        }
    )
    $("#deletebutton").click(() => {
        let pmUsername = $("#productmanagerusername").val();
        $.ajax({
            type: "POST",
            url: "/admin/deleteproductmanager",
            data: {
                "productmanagerusername": pmUsername
            },
            success: (res) => {
                if (res["success"]) {alert("delete successfully")}
                else alert(res["error"]);
            }
        })
    })
    </security:authorize>



    <c:if test="${param.rpms == 'true'}">
    alert("register successfully")
    </c:if>
    <c:if test="${param.changepasswordsuccessfully ==true}">
    alert("change password successfully")
    </c:if>

    <c:if test="${param.addproductsuccessfully ==true}">
    alert("add product successfully  successfully")
    </c:if>

    function findInvalidChars(string) {
        let regex = RegExp("^[a-zA-Z0-9_]*$");


        return !regex.test(string);
    }


    $("#displayName").on('keyup change', function (e) {
        let message = "";
        let inp = $("#displayName").val();
        if (findInvalidChars(inp)) message += "Invalid input(only digits, letters and '_'); ";
        if (inp.length < 5) message += "Too Short(at least 5); ";
        if (inp.length > 10) message += "Too Long(at most 10); ";
        $("#displayNameError").text(message);
        if (message === "")
            $("#submitdisplayname").prop("disabled", false)
        else $("#submitdisplayname").prop("disabled", true)
    });
    $('#submitdisplayname').click(() => {
        let newdisplayname = $("#displayName").val();
        if (!findInvalidChars(newdisplayname) && 5 <= newdisplayname.length <= 10) {
            $.ajax({
                    url: "/account/updatedisplayname",
                    method: "get",
                    data: {"newdisplayname": newdisplayname},
                    success: (res) => {
                        if (res["success"]) {
                            alert("update successfully");
                            $("#displaynameheader").text(res["displayname"]);
                        } else {
                            alert("fail")
                        }
                    }
                }
            )
        } else {
            alert("inputerror");
        }

    })
</script>
</html>

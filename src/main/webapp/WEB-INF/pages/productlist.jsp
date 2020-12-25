<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.edu.neu.project.authority.AuthorityEnum" %>
<html>
<head>
    <link href='<spring:url value="/css/styles.css"/>' rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.5.0.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
</head>
<body>
<jsp:include page="_header.jsp"/>
<jsp:include page="_left.jsp"/>

<div class="ui-widget">
    <label for="productName">search by product name: </label>
    <input id="productName">
</div>
<button id="search">search</button>
<br>
<security:authorize access="hasAnyAuthority('${AuthorityEnum.Customer.authority}')">
    <input id="authority" type="hidden" value="customer"/>
</security:authorize>
<security:authorize access="hasAnyAuthority('${AuthorityEnum.Manager.authority}')">
    <input id="authority" type="hidden" value="manager"/>
</security:authorize>
<security:authorize access="hasAnyAuthority('${AuthorityEnum.Admin.authority}')">
    <input id="authority" type="hidden" value="admin"/>
</security:authorize>
<div class="right">
    <div id="output" class="product-previewer">
    </div>
</div>
</body>
<script>
    let productTypeID = -1;
    let productNames;
    $("[data-pid]").click(function () {
        let pid = $(this).data("pid");
        productTypeID = pid;
        refresh(productTypeID);
    });

    <c:if test="${param.updatesuccessfully == true}">
    alert("update successfully")
    </c:if>


    $.get("product/getproductnamelist", (res) => {
            productNames = res;

            $("#productName").autocomplete({
                source: productNames
            })
        }
    )

    $('#search').click(() => {
        $.getJSON('product/searchbyproductname', {name: $("#productName").val()}, generateProductCallback);
    });

    $(document).ready(() => {
        refresh(productTypeID);

    });

    function isAllDigit(s) {
        let regex = RegExp("^[0-9]+$")
        return regex.test(s)
    }

    function addToCart(val) {
        let productID = $(val).attr('value');
        let quantity = $("#number_" + productID).val();
        if (!isAllDigit(quantity))
            alert("input error")
        else {
            $.ajax({
                url: "customer/addtocart",
                data: {
                    productID,
                    quantity
                },
                success: (res) => {
                    if (res) alert("add successfully")
                    else alert("add fail");
                },

            });
        }
        $('#number_' + productID).val("");
    }


    function refresh(productTypeID) {
        $.getJSON('product/list', {productTypeID}, generateProductCallback);
    }

    const generateProductCallback = (data) => {
        let auth = $("#authority").val();
        console.log(auth)
        console.log(data)
        $('#output').empty();
        if (data.length !== 0) {
            $.each(data, (key, value) => {
                function addLi(prop, propdisplayname, value) {
                    $('<li/>', {
                        'id': 'li_' + prop + '_'.concat(value['productID']),
                        'html': propdisplayname + ": " + value[prop]
                    }).appendTo("#" + 'ul_'.concat(value['productID']));
                }

                $('<ul/>', {
                    'id': 'ul_'.concat(value['productID']),
                }).appendTo("#output");
                $('<li/>', {
                    'id': 'li_image_'.concat(value['productID']),
                    'html': '<img class="product-image" src="${pageContext.request.contextPath}/product/image/' + value["productID"] + '"/>'
                }).appendTo("#" + 'ul_'.concat(value['productID']));
                addLi('name', 'name', value);
                addLi('price', 'price', value);
                addLi('productTypeName', 'type', value)
                addLi('description', 'description', value);
                if (auth === "customer") {
                    $('<li/>', {
                        'html': "<input required id='number_" + value['productID'] + "' type=number min='0' step='1'/>" +
                            "<button onclick='addToCart(this)'  type='button' value=" + value['productID'] + ">add</button>"
                    }).appendTo("#" + 'ul_'.concat(value['productID']));
                } else if (auth === "admin" || auth === "manager") {
                    addLi('lastModifiedDate', 'last modified time', value);
                    $('<li/>', {
                        'html': "<a href='<spring:url value="/productmanager/manageproduct?id={id}"/>'>manage</a>".replace("{id}", value['productID'])
                    }).appendTo("#" + 'ul_'.concat(value['productID']));
                }
            });
        } else {
            $('<p/>', {
                'html': "No Such Product"
            }).appendTo($('#output'))
        }
    };

</script>
</html>
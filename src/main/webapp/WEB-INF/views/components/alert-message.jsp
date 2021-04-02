<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ include file="../components/head.jsp" %>
</head>

<body>

<%@ include file="../components/header.jsp" %>

<div class="main">
    <!--menu da direita-->
    <div class="white_box">
        <div class="perfil-main-col">
            <div class="perfil-row">
                <div class="perfil-row">
                    <img src="${imageURL}" alt="fill form" class="img-fill-form"/>
                    <h3 class="title_next_appt">${message}</h3>
                </div>
            </div>
        </div>


        <div class="perfil-row">
            <div class="cell-back">
                <a href="/player" class="btn btn-outline-dark btn-back">Regressar à página inicial</a>
            </div>
        </div>

        <c:if test="${hasButton2}">
            <div class="perfil-row">
                <div class="cell-back">
                    <a href="${button2_url}" class="btn btn-outline-dark btn-back">${button2_text}</a>
                </div>
            </div>
        </c:if>
    </div>


</div>
<%@ include file="../components/footer.jsp" %>

</body>
</html>
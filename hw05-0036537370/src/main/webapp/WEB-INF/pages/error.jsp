<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

    <head>
        <style>
            * {
                color: red;
            }
        </style>
    </head>

    <body>
        <header style="display: flex; align-items: center; justify-content: space-between;">
            <c:if test="${ sessionScope['current.user.id'] == null }">
                <p>Not logged in</p>
            </c:if>
            <a href="<c:url value ="/servleti/main"/>">Home</a>
            <c:if test="${ sessionScope['current.user.id'] != null }">
                <p>${ sessionScope['current.user.fn'] } ${ sessionScope['current.user.ln'] }</p>
                <a href="<c:url value ="/servleti/logout"/>">Logout</a>
            </c:if>
        </header>
        <h1>The following error occurred: </h1>
        <p>${errMessage == null ? "Undefined error :(" : errMessage}</p>
        <a href = "<c:url value="/servleti/main" />">Home</a>
    </body>

</html>
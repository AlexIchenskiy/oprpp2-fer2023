<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

    <head>
        <style>
            body {
                background-color: #<%= session.getAttribute("pickedBgCol") == null ? "FFFFFF" : session.getAttribute("pickedBgCol") %>;
            }
        </style>
    </head>

    <body>
        <a href = "<c:url value = "/setcolor">
                    <c:param name="pickedBgCol" value="FFFFFF"/>
                   </c:url>">WHITE</a>
        <a href = "<c:url value = "/setcolor">
                    <c:param name="pickedBgCol" value="FF0000"/>
                   </c:url>">RED</a>
        <a href = "<c:url value = "/setcolor">
                    <c:param name="pickedBgCol" value="00FF00"/>
                   </c:url>">GREEN</a>
        <a href = "<c:url value = "/setcolor">
                    <c:param name="pickedBgCol" value="00FFFF"/>
                   </c:url>">CYAN</a>
    </body>

</html>
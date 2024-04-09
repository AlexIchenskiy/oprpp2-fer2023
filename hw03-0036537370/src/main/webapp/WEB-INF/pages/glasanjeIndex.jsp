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
        <h1>Glasanje za omiljeni bend:</h1>
        <p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!</p>
        <ol>
            <c:forEach var="i" begin="0" end="${ ids.size() - 1 }" step="1">
                <li><a href = "<c:url value = "glasanje-glasaj?id=${ ids.get(i) }" />">${ names.get(i) }</a></li>
            </c:forEach>
        </ol>
    </body>

</html>
<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

    <head>
        <style>
            body {
                background-color: #<%= session.getAttribute("pickedBgCol") == null ? "FFFFFF" : session.getAttribute("pickedBgCol") %>;
            }

            h1, p {
                color: red;
            }
        </style>
    </head>

    <body>
        <h1>The following error occurred: </h1>
        <p>${errMessage == null ? "Undefined error :(" : errMessage}</p>
        <a href = "<c:url value="/index.jsp" />">Home</a>
    </body>

</html>
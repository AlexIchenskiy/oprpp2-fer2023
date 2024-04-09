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
        <h1>OS Usage</h1>
        <p>Here are the results of OS usage in survey that we completed.</p>

        <img src = "<c:url value = "/reportImage" />" alt = "OS Chart" />
    </body>

</html>
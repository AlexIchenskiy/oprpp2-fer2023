<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

    <head>
        <style>
            body {
                background-color: #<%= session.getAttribute("pickedBgCol") == null ? "FFFFFF" : session.getAttribute("pickedBgCol") %>;
            }

            p {
                color: <%= String.format("#%06x", (int) (Math.random() * 0xffffff)) %>;
            }
        </style>
    </head>

    <body>
        <p>Volim C#</p>
    </body>

</html>
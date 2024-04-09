<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

    <head>
        <style>
            body {
                background-color: #<%= session.getAttribute("pickedBgCol") == null ? "FFFFFF" : session.getAttribute("pickedBgCol") %>;
            }

            table {
                width: 480px;
            }

            table, td {
                border: 1px solid #323232;
                border-collapse: collapse;
            }
        </style>
    </head>

    <body>
        <table>
            <thead>
                <tr>
                    <td>Angle</td>
                    <td>Sin</td>
                    <td>Cos</td>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="i" begin="0" end="${ sin.size() - 1 }" step="1">
                    <tr>
                        <td>${ start + i }</td>
                        <td>${ sin.get(i) }</td>
                        <td>${ cos.get(i) }</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>

</html>
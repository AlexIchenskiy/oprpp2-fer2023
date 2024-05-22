<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

    <head>
        <style>
            table.rez td {
                text-align: center;
            }
        </style>
    </head>

    <body>
        <h1>Rezultati glasanja</h1>
        <p>Ovo su rezultati glasanja</p>
        <table border="1" cellspacing="0" class="rez">
            <thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
            <tbody>
                <c:if test="${ names.size() > 0 }">
                    <c:forEach var="i" begin="0" end="${ names.size() - 1 }" step="1">
                        <tr>
                            <td>${ names.get(i) }</td>
                            <td>${ res.get(i) }</td>
                        </tr>
                    </c:forEach>
                </c:if>
            </tbody>
        </table>

        <h2>Grafički prikaz rezultata</h2>
        <img alt="Pie-chart" src="<c:url value = "/servleti/glasanje-grafika">
                                    <c:param name="pollID" value="${ pollID }"/>
                                  </c:url>" width="720" height="480" />

        <h2>Rezultati u XLS formatu</h2>
        <p>Rezultati u XLS formatu dostupni su <a href="<c:url value = "/servleti/glasanje-xls">
                                                            <c:param name="pollID" value="${ pollID }"/>
                                                        </c:url>">ovdje</a></p>

        <h2>Razno</h2>
        <p>Primjeri pjesama pobjedničkih bendova:</p>
        <ul>
            <c:if test="${ videos.size() > 0 }">
                <c:forEach var="i" begin="0" end="${ videos.size() - 1 }" step="1">
                    <li><a href="${videos.get(i)}">${ names.get(i) }</a></li>
                </c:forEach>
            </c:if>
        </ul>
    </body>

</html>
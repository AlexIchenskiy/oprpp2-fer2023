<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

    <body>
        <ul>
            <c:forEach var="i" begin="0" end="${ polls.size() - 1 }" step="1">
                <li><a href = "<c:url value = "/servleti/glasanje">
                                <c:param name="pollID" value="${ polls.get(i).getId() }"/>
                               </c:url>">${ polls.get(i).getTitle() }</a></li>
            </c:forEach>
        </ul>
    </body>

</html>
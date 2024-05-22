<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

    <body>
        <h1>${ poll.getTitle() }</h1>
        <p>${ poll.getMessage() }</p>
        <c:if test="${ids.size() > 0}">
            <ol>
                <c:forEach var="i" begin="0" end="${ ids.size() - 1 }" step="1">
                    <li><a href = "<c:url value = "/servleti/glasanje-glasaj">
                                    <c:param name="pollID" value="${ poll.getId() }"/>
                                    <c:param name="optionID" value="${ ids.get(i) }"/>
                                   </c:url>">${ names.get(i) }</a></li>
                </c:forEach>
            </ol>
        </c:if>
    </body>

</html>
<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
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

        <main>
            <h1>Blog entries of user ${ author }:</h1>
            <c:if test="${ entries.isEmpty() }">
                No blog entries :(
            </c:if>
            <c:if test="${ !entries.isEmpty() }">
                <ol>
                    <c:forEach items="${ entries }" var="entry">
                        <li>
                            <a href = "<c:url value="/servleti/author/${ author }/${ entry.getId() }" />">${ entry.getTitle() }</a>
                        </li>
                    </c:forEach>
                </ol>
            </c:if>
            <br>
            <c:if test="${ isAuthor }">
                <a href = "<c:url value="/servleti/author/${ author }/new" />">New post</a>
            </c:if>
        </main>
    </body>
</html>
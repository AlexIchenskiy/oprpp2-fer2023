<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <body>
        <header>

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
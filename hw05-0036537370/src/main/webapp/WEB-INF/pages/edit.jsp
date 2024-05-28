<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <body>
        <header>
        </header>

        <main>
            <form action="<c:url value="/servleti/author/${ author }/edit/${ entry.getId() }"/>" method="post">
                <label for="title">Title:</label><br>
                <input type="text" id="title" name="title" value="${ entry.getTitle() }"><br>
                <label for="text">Text:</label><br>
                <textarea id="text" name="text">${ entry.getText() }</textarea><br>
                <c:if test="${error != null}">
                    <p class="error">${error}</p>
                </c:if>
                <input type="submit" value="Submit"><br>
            </form>
        </main>
    </body>
</html>
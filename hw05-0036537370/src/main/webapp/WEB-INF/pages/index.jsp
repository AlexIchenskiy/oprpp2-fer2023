<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <body>
        <header>

        </header>

        <main>
            <div>
                <form action="<c:url value="/servleti/main" />" method="post">
                    <label for="nick">Nickname:</label><br>
                    <input type="text" id="nick" name="nick" value="${nick}" /><br>
                    <label for="pass">Password:</label><br>
                    <input type="password" id="pass" name="pass" /><br>
                    ${current.user.id}
                    <c:if test="${error != null}">
                        <p class="error">${error}</p>
                    </c:if>
                    <button type="submit">Login</button>
                </form>
                <h4>Don`t have an account? <a href="<c:url value="/servleti/register" />">Create one!</a></h4>
            </div>
            <div>
                <c:if test="${ authors.isEmpty() }">
                    No authors yet :(
                </c:if>
                <c:if test="${ !authors.isEmpty() }">
                    Authors list:
                    <ol>
                        <c:forEach items="${authors}" var="author">
                            <li><a href = "<c:url value="/servleti/author/${ author.getNick() }" />">${ author.getNick() }</a></li>
                        </c:forEach>
                    </ol>
                </c:if>
            </div>
        </main>
    </body>
</html>
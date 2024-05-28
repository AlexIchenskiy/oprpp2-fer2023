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
            <div>
                <c:if test="${ sessionScope['current.user.id'] == null }">
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
                </c:if>
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
            <c:if test="${ sessionScope['current.user.id'] != null }">
                <a href="<c:url value="/servleti/logout" />">Logout</a>
            </c:if>
        </main>
    </body>
</html>
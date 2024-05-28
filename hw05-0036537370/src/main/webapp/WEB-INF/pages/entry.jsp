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
            <h1>${ entry.getTitle() }</h1>
            <div>
                <span>
                    ${ entry.getText() }
                </span>
            </div>

            <c:if test="${ isAuthor }">
                <a href="<c:url value="/servleti/author/${ author }/edit/${ entry.getId() }" />">Edit</a>
            </c:if>

            <h2>Comments: </h2>
            <c:if test="${ entry.getComments().isEmpty() }">
                <h4>No comments yet. Be the first!</h4>
            </c:if>
            <c:if test="${ !entry.getComments().isEmpty() }">
                <c:forEach items="${ entry.getComments() }" var="comment">
                    <div>
                        <h3>${ comment.getUsersEMail() }</h3><br>
                        <p>${ comment.getMessage() }</p><br>
                        <h6>${ comment.getPostedOn() }</h6><br>
                    </div>
                </c:forEach>
            </c:if>
            <form action="<c:url value="/servleti/comments" />" method="post">
                <textarea id="message" name="message"></textarea>
                <input type="hidden" id="author" name="author" value="${ sessionScope["current.user.nick"] == null ? "" : sessionScope["current.user.nick"] }">
                <input type="hidden" id="id" name="id" value="${ entry.getId() }">
                <input type="hidden" id="entryAuthor" name="entryAuthor" value="${ entry.getCreator().getNick() }">
                <br>
                <p class="error">
                    ${ commError }
                </p>
                <button type="submit">Submit</button>
            </form>
        </main>
    </body>
</html>
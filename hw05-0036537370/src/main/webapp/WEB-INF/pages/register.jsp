<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <body>
        <form action="<c:url value="/servleti/register" />" method="post">
            <label for="firstname">First name:</label><br>
            <input type="text" id="firstname" name="firstname" value="${firstname}" /><br>
            <label for="lastname">Last name:</label><br>
            <input type="text" id="lastname" name="lastname" value="${lastname}" /><br>
            <label for="email">Email:</label><br>
            <input type="text" id="email" name="email" value="${email}" /><br>
            <label for="nick">Nickname:</label><br>
            <input type="text" id="nick" name="nick" value="${nick}" /><br>
            <label for="pass">Password:</label><br>
            <input type="password" id="pass" name="pass" /><br>
            <c:if test="${error != null}">
                <p class="error">${error}</p>
            </c:if>
            <button type="submit">Register</button>
        </form>
        <h4>Already have an account? <a href="<c:url value="/servleti/main" />">Login!</a></h4>
    </body>
</html>
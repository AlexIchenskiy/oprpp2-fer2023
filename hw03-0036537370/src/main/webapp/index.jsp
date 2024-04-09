<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

    <head>
        <style>
            body {
                background-color: #<%= session.getAttribute("pickedBgCol") == null ? "FFFFFF" : session.getAttribute("pickedBgCol") %>;
            }
        </style>
    </head>

    <body>
        <marquee>My JSP WebApp</marquee>
        <h1>Color change</h1>
        <a href = "<c:url value = "/colors.jsp"/>">Background color chooser</a>
        <br />
        <br />
        <h1>Trigonometry</h1>
        <a href = "<c:url value = "/trigonometric">
                    <c:param name="a" value="0"/>
                    <c:param name="b" value="90"/>
                   </c:url>">sin() and cos() values for 0-90 angles</a>
        <br />
        <br />

        <form action="trigonometric" method="GET">
            Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
            Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
            <input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
        </form>

        <br />
        <br />

        <a href = "<c:url value = "/stories/funny.jsp"/>">(Not) Funny</a>

        <br />
        <br />

        <h1>Report section</h1>
        <a href = "<c:url value = "/report.jsp" />">OS Usage report</a>

        <br />
        <br />

        <h1>Powers XLS section</h1>
        <a href = "<c:url value = "/powers">
                    <c:param name="a" value="1"/>
                    <c:param name="b" value="100"/>
                    <c:param name="n" value="3"/>
                   </c:url>">Powers from 1 to 3 of numbers in range [1, 100]</a>

        <br />
        <br />

        <h1>App info section</h1>
        <a href = "<c:url value = "/appinfo.jsp" />">App info</a>

        <h1>Voting section</h1>
        <a href = "<c:url value = "/glasanje" />">Vote!</a>
    </body>

</html>
<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.Duration" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="d" value="${Duration.ofMillis(System.currentTimeMillis() - applicationScope.start)}" />
<c:set var="uptime" value="${d.toDays()} days ${d.toHours() % 24} hours ${d.toMinutes() % 60} minutes ${d.toSeconds() % 60} seconds and ${d.toMillis() % 1000} milliseconds" />

<html>

    <head>
        <style>
            body {
                background-color: #<%= session.getAttribute("pickedBgCol") == null ? "FFFFFF" : session.getAttribute("pickedBgCol") %>;
            }
        </style>
    </head>

    <body>
        <h1>App Info</h1>
        <h3>The app is up for ${ uptime }</h3>
    </body>

</html>
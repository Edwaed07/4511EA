<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>4511 Web System - Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo"> Bakery</div>
        <nav>
            <ul>
                <li><a href="index.jsp">Home</a></li>
            </ul>
        </nav>
        <h1>Login<c:if test="${not empty param.role}"> as ${param.role}</c:if></h1>
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <form action="LoginServlet" method="post" class="login-form">
            <input type="hidden" name="role" value="${param.role}">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <input type="submit" value="Login">
        </form>
        <p style="text-align: center; margin-top: 15px;">
            <a href="createAccount.jsp?role=${param.role}">Create new account</a> | 
            <a href="index.jsp">Back to Role Selection</a>
        </p>
    </div>
    <footer>
        © 2025 4511 Bakery. All rights reserved.
    </footer>
</body>
</html>
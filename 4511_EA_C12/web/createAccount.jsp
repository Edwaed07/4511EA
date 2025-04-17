<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Web System - Sign Up</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="login.jsp?role=<%= request.getParameter("role") %>">Login</a></li>
            </ul>
        </nav>
        <h1>Sign Up as <%= request.getParameter("role") %></h1>
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <form action="CreateAccountServlet" method="post">
            <input type="hidden" name="role" value="<%= request.getParameter("role") %>">
            <label>Email:</label>
            <input type="email" name="email" required>
            <label>Name:</label>
            <input type="text" name="email" required>
            <label>Password:</label>
            <input type="password" name="password" required>
            <label>Confirm Password:</label>
            <input type="password" name="confirmPassword" required>
            <input type="submit" value="Create Account">
        </form>
        <p style="text-align: center; margin-top: 15px;">
            <a href="login.jsp?role=<%= request.getParameter("role") %>">Back to Login</a>
        </p>
    </div>
    <footer>
        &copy; 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html>
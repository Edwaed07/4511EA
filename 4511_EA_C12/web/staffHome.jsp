<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Staff Home - AIB Web System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%
        if (session.getAttribute("email") == null || !"shopStaff".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.jsp");
        }
    %>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <h1>Welcome, <%= session.getAttribute("email") %>!</h1>
        <nav>
            <ul>
                <li><a href="reserveFruit.jsp">Reserve Fruits (from source city)</a></li>
                <li><a href="borrowFruit.jsp">Borrow Fruits (from shops in the same city)</a></li>
                <li><a href="checkReserve.jsp">Check Reserve Records</a></li>
                <li><a href="updateStock.jsp">Update Fruit Stock Level</a></li>
                <li><a href="logout">Logout</a></li>
            </ul>
        </nav>
    </div>
</body>
</html>
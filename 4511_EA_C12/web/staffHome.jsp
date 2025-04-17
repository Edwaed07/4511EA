<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Home - AIB Web System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="FruitServlet">View Fruits</a></li>
                <li><a href="ReserveFruitServlet">Reserve Fruit</a></li>
                <li><a href="BorrowFruitServlet">Borrow Fruit</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Welcome, Bakery Shop Staff!</h1>
        <p style="text-align: center;">Email: ${sessionScope.email}</p>
        <p style="text-align: center;">Shop ID: ${sessionScope.shopId}</p>
    </div>
    <footer>
        &copy; 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html>
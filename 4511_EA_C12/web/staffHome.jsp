<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Home - 4511 Web System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="staffHome.jsp">Home</a></li>
                <li><a href="updateStockServlet">Shop Stock</a></li>
                <li><a href="FruitServlet">Reserve Fruit</a></li>
                <li><a href="FruitServlet?page=borrowFruit">Borrow Fruit</a></li>
                <li><a href="CheckReserveServlet">Check Reservations</a></li>
                <li><a href="FruitManagementServlet">Manage Fruits</a></li>
                <li><a href="ApproveServlet">Approve Requests</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Welcome, Bakery Shop Staff!</h1>
        <div class="user-info">
            <p><strong>Name:</strong> ${sessionScope.name}</p>
            <p><strong>Email:</strong> ${sessionScope.email}</p>
            <p><strong>Branch:</strong> ${sessionScope.employeeBranch}</p>
            <p><strong>Role:</strong> ${sessionScope.role}</p>
        </div>
    </div>
    <footer>
        © 2025 Acer International Bakery.
    </footer>
</body>
</html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Web System - Warehouse Staff Home</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="warehouseHome.jsp">Home</a></li>
                <li><a href="FruitServlet?page=fruitList">Fruits List</a></li>
                <li><a href="warehouseCheckIn.jsp">Check-In Stock</a></li>
                <li><a href="WarehouseServlet?action=viewTotalNeeds">Total Needs by Country</a></li>
                <li><a href="WarehouseServlet?action=viewDeliveries">Manage Deliveries</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Welcome, Warehouse Staff</h1>
        
        <!-- 用户个人信息显示部分 -->
        <div class="user-info">
            <p><strong>Name:</strong> ${sessionScope.name}</p>
            <p><strong>Email:</strong> ${sessionScope.email}</p>
            <p><strong>Role:</strong> ${sessionScope.role}</p>
            <p><strong>Employee ID:</strong> ${sessionScope.employeeId}</p>
        </div>
    </div>
    <footer>
        © 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html> 
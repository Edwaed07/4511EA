<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Web System - Error</title>
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
        <h1>Error</h1>
        
        <div class="error-container">
            <h2>系統錯誤</h2>
            <p>抱歉，處理您的請求時發生錯誤：</p>
            <div class="error-message">
                ${error}
            </div>
            <p>請返回首頁重試，或聯繫系統管理員獲取幫助。</p>
            <p><a href="warehouseHome.jsp" class="btn">返回首頁</a></p>
        </div>
    </div>
    <footer>
        © 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html> 
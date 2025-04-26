<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Web System - Warehouse Stock Check-In</title>
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
        <h1>Warehouse Stock Check-In</h1>
        
        <c:if test="${not empty message}">
            <div class="message ${messageType}">${message}</div>
        </c:if>
        
        <div class="form-section">
            <h2>Update Stock Levels</h2>
            <form action="WarehouseServlet" method="post">
                <input type="hidden" name="action" value="checkIn">
                
                <div class="form-group">
                    <label for="fruit">Select Fruit:</label>
                    <select id="fruit" name="fruitId" required>
                        <option value="">-- Select a Fruit --</option>
                        <c:forEach var="fruit" items="${fruits}">
                            <option value="${fruit.id}">${fruit.name} (Source: ${fruit.sourceCity}, ${fruit.country})</option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="quantity">Quantity to Check-In:</label>
                    <input type="number" id="quantity" name="quantity" min="1" required>
                </div>
                
                <div class="form-group">
                    <label for="warehouseLocation">Warehouse Location:</label>
                    <select id="warehouseLocation" name="warehouseLocation" required>
                        <option value="">-- Select Location --</option>
                        <option value="Central">Central Warehouse</option>
                        <option value="Source">Source Warehouse</option>
                    </select>
                </div>
                
                <input type="submit" value="Check-In Stock">
            </form>
        </div>
        
        <div class="stock-summary">
            <h2>Current Warehouse Stock</h2>
            <table>
                <thead>
                    <tr>
                        <th>Fruit Name</th>
                        <th>Source</th>
                        <th>Country</th>
                        <th>Central Stock</th>
                        <th>Source Stock</th>
                        <th>Last Updated</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="stock" items="${warehouseStock}">
                        <tr>
                            <td>${stock.fruitName}</td>
                            <td>${stock.sourceCity}</td>
                            <td>${stock.country}</td>
                            <td>${stock.centralStock}</td>
                            <td>${stock.sourceStock}</td>
                            <td>${stock.lastUpdated}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <footer>
        © 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html> 
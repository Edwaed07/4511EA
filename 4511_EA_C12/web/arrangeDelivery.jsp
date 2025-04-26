<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Web System - Arrange Delivery</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="warehouseHome.jsp">Home</a></li>
                <li><a href="FruitServlet?page=fruitList">Fruits List</a></li>
                <li><a href="WarehouseServlet?action=loadCheckIn">Check-In Stock</a></li>
                <li><a href="WarehouseServlet?action=viewTotalNeeds">Total Needs by Country</a></li>
                <li><a href="WarehouseServlet?action=viewDeliveries">Manage Deliveries</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Arrange Delivery for Approved Need</h1>
        
        <c:if test="${not empty message}">
            <div class="message ${messageType}">${message}</div>
        </c:if>
        
        <div class="need-details">
            <h2>Need Details</h2>
            <table>
                <tr>
                    <th>Need ID:</th>
                    <td>${need.id}</td>
                </tr>
                <tr>
                    <th>Country:</th>
                    <td>${need.country}</td>
                </tr>
                <tr>
                    <th>Fruit:</th>
                    <td>${need.fruitName}</td>
                </tr>
                <tr>
                    <th>Total Quantity:</th>
                    <td>${need.totalQuantity}</td>
                </tr>
                <tr>
                    <th>Number of Requests:</th>
                    <td>${need.requestCount}</td>
                </tr>
                <tr>
                    <th>Date Range:</th>
                    <td>${need.startDate} to ${need.endDate}</td>
                </tr>
                <tr>
                    <th>Status:</th>
                    <td>${need.status}</td>
                </tr>
            </table>
        </div>
        
        <div class="form-section">
            <h2>Delivery Details</h2>
            <form action="WarehouseServlet" method="post">
                <input type="hidden" name="action" value="arrangeDelivery">
                <input type="hidden" name="needId" value="${need.id}">
                
                <div class="form-group">
                    <label for="sourceWarehouse">Source Warehouse:</label>
                    <select id="sourceWarehouse" name="sourceWarehouse" required>
                        <option value="">-- Select Source --</option>
                        <c:forEach var="warehouse" items="${sourceWarehouses}">
                            <option value="${warehouse.id}">${warehouse.name} (Stock: ${warehouse.stock})</option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="destinationWarehouse">Destination Central Warehouse:</label>
                    <select id="destinationWarehouse" name="destinationWarehouse" required>
                        <option value="">-- Select Destination --</option>
                        <c:forEach var="warehouse" items="${destinationWarehouses}">
                            <option value="${warehouse.id}">${warehouse.name}</option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="quantity">Quantity:</label>
                    <input type="number" id="quantity" name="quantity" min="1" max="${need.totalQuantity}" value="${need.totalQuantity}" required>
                    <small>Maximum available: ${availableStock}</small>
                </div>
                
                <div class="form-group">
                    <label for="estimatedDeliveryDays">Estimated Delivery Time (days):</label>
                    <input type="number" id="estimatedDeliveryDays" name="estimatedDeliveryDays" min="1" value="5" required>
                </div>
                
                <div class="form-group">
                    <label for="notes">Notes:</label>
                    <textarea id="notes" name="notes" rows="3"></textarea>
                </div>
                
                <input type="submit" value="Arrange Delivery">
            </form>
        </div>
        
        <div class="shop-distributions">
            <h2>Shop Distribution</h2>
            <p>These shops have requested this fruit and will receive deliveries from the central warehouse:</p>
            <table>
                <thead>
                    <tr>
                        <th>Shop Name</th>
                        <th>City</th>
                        <th>Requested Quantity</th>
                        <th>Request Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="shop" items="${shopRequests}">
                        <tr>
                            <td>${shop.name}</td>
                            <td>${shop.city}</td>
                            <td>${shop.quantity}</td>
                            <td>${shop.requestDate}</td>
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
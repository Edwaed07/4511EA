<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Web System - Manage Deliveries</title>
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
        <h1>Manage Deliveries</h1>
        
        <c:if test="${not empty message}">
            <div class="message ${messageType}">${message}</div>
        </c:if>
        
        <div class="tab-navigation">
            <a href="#" class="tab-link active" data-tab="pending">Pending Deliveries</a>
            <a href="#" class="tab-link" data-tab="in-transit">In Transit</a>
            <a href="#" class="tab-link" data-tab="completed">Completed</a>
            <a href="#" class="tab-link" data-tab="new-delivery">Arrange New Delivery</a>
        </div>
        
        <div id="pending" class="tab-content active">
            <h2>Pending Deliveries</h2>
            <table>
                <thead>
                    <tr>
                        <th>Delivery ID</th>
                        <th>From</th>
                        <th>To</th>
                        <th>Fruit</th>
                        <th>Quantity</th>
                        <th>Created Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="delivery" items="${pendingDeliveries}">
                        <tr>
                            <td>${delivery.id}</td>
                            <td>${delivery.sourceLocation}</td>
                            <td>${delivery.destinationLocation}</td>
                            <td>${delivery.fruitName}</td>
                            <td>${delivery.quantity}</td>
                            <td>${delivery.createdDate}</td>
                            <td>${delivery.status}</td>
                            <td>
                                <form action="WarehouseServlet" method="post">
                                    <input type="hidden" name="action" value="startDelivery">
                                    <input type="hidden" name="deliveryId" value="${delivery.id}">
                                    <input type="submit" value="Start Delivery" class="btn-small">
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <div id="in-transit" class="tab-content">
            <h2>In Transit Deliveries</h2>
            <table>
                <thead>
                    <tr>
                        <th>Delivery ID</th>
                        <th>From</th>
                        <th>To</th>
                        <th>Fruit</th>
                        <th>Quantity</th>
                        <th>Ship Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="delivery" items="${inTransitDeliveries}">
                        <tr>
                            <td>${delivery.id}</td>
                            <td>${delivery.sourceLocation}</td>
                            <td>${delivery.destinationLocation}</td>
                            <td>${delivery.fruitName}</td>
                            <td>${delivery.quantity}</td>
                            <td>${delivery.shipDate}</td>
                            <td>${delivery.status}</td>
                            <td>
                                <form action="WarehouseServlet" method="post">
                                    <input type="hidden" name="action" value="completeDelivery">
                                    <input type="hidden" name="deliveryId" value="${delivery.id}">
                                    <input type="submit" value="Mark as Delivered" class="btn-small">
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <div id="completed" class="tab-content">
            <h2>Completed Deliveries</h2>
            <table>
                <thead>
                    <tr>
                        <th>Delivery ID</th>
                        <th>From</th>
                        <th>To</th>
                        <th>Fruit</th>
                        <th>Quantity</th>
                        <th>Delivery Date</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="delivery" items="${completedDeliveries}">
                        <tr>
                            <td>${delivery.id}</td>
                            <td>${delivery.sourceLocation}</td>
                            <td>${delivery.destinationLocation}</td>
                            <td>${delivery.fruitName}</td>
                            <td>${delivery.quantity}</td>
                            <td>${delivery.deliveryDate}</td>
                            <td>${delivery.status}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <div id="new-delivery" class="tab-content">
            <h2>Arrange New Delivery</h2>
            <form action="WarehouseServlet" method="post">
                <input type="hidden" name="action" value="createDelivery">
                
                <div class="form-group">
                    <label for="deliveryType">Delivery Type:</label>
                    <select id="deliveryType" name="deliveryType" required>
                        <option value="">-- Select Type --</option>
                        <option value="ToCountry">Source to Central Warehouse</option>
                        <option value="ToShop">Central Warehouse to Local Shop</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="sourceLocation">Source Location:</label>
                    <select id="sourceLocation" name="sourceLocation" required>
                        <option value="">-- Select Source --</option>
                        <c:forEach var="location" items="${sourceLocations}">
                            <option value="${location.id}">${location.name} (${location.type})</option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="destinationLocation">Destination:</label>
                    <select id="destinationLocation" name="destinationLocation" required>
                        <option value="">-- Select Destination --</option>
                        <c:forEach var="location" items="${destinationLocations}">
                            <option value="${location.id}">${location.name} (${location.type})</option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="fruit">Fruit:</label>
                    <select id="fruit" name="fruitId" required>
                        <option value="">-- Select Fruit --</option>
                        <c:forEach var="fruit" items="${availableFruits}">
                            <option value="${fruit.id}">${fruit.name} (Available: ${fruit.stockLevel})</option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="quantity">Quantity:</label>
                    <input type="number" id="quantity" name="quantity" min="1" required>
                </div>
                
                <input type="submit" value="Create Delivery">
            </form>
        </div>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Tab navigation
            const tabLinks = document.querySelectorAll('.tab-link');
            const tabContents = document.querySelectorAll('.tab-content');
            
            tabLinks.forEach(link => {
                link.addEventListener('click', function(e) {
                    e.preventDefault();
                    
                    // Remove active class from all tabs
                    tabLinks.forEach(tab => tab.classList.remove('active'));
                    tabContents.forEach(content => content.classList.remove('active'));
                    
                    // Add active class to current tab
                    this.classList.add('active');
                    const tabId = this.getAttribute('data-tab');
                    document.getElementById(tabId).classList.add('active');
                });
            });
        });
    </script>
    
    <footer>
        © 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html> 
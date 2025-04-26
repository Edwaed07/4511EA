<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Web System - Total Needs by Country</title>
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
        <h1>Total Needs by Country</h1>
        
        <c:if test="${not empty message}">
            <div class="message ${messageType}">${message}</div>
        </c:if>
        
        <div class="filter-form">
            <form action="WarehouseServlet" method="get">
                <input type="hidden" name="action" value="viewTotalNeeds">
                <div class="form-group">
                    <label for="country">Filter by Country:</label>
                    <select id="country" name="country">
                        <option value="">All Countries</option>
                        <c:forEach var="country" items="${countries}">
                            <option value="${country}" ${param.country eq country ? 'selected' : ''}>${country}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="status">Filter by Status:</label>
                    <select id="status" name="status">
                        <option value="">All Statuses</option>
                        <option value="Pending" ${param.status eq 'Pending' ? 'selected' : ''}>Pending</option>
                        <option value="Approved" ${param.status eq 'Approved' ? 'selected' : ''}>Approved</option>
                        <option value="Rejected" ${param.status eq 'Rejected' ? 'selected' : ''}>Rejected</option>
                    </select>
                </div>
                <input type="submit" value="Filter">
            </form>
        </div>
        
        <div class="needs-table">
            <h2>Pending Approvals</h2>
            <table>
                <thead>
                    <tr>
                        <th>Country</th>
                        <th>Fruit</th>
                        <th>Total Quantity</th>
                        <th>Number of Requests</th>
                        <th>Requested Date Range</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="need" items="${totalNeeds}">
                        <tr>
                            <td>${need.country}</td>
                            <td>${need.fruitName}</td>
                            <td>${need.totalQuantity}</td>
                            <td>${need.requestCount}</td>
                            <td>${need.startDate} to ${need.endDate}</td>
                            <td>${need.status}</td>
                            <td>
                                <c:if test="${need.status eq 'Pending'}">
                                    <form action="WarehouseServlet" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="approveNeed">
                                        <input type="hidden" name="needId" value="${need.id}">
                                        <input type="submit" value="Approve" class="btn-small">
                                    </form>
                                    <form action="WarehouseServlet" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="rejectNeed">
                                        <input type="hidden" name="needId" value="${need.id}">
                                        <input type="submit" value="Reject" class="btn-small btn-danger">
                                    </form>
                                </c:if>
                                <c:if test="${need.status eq 'Approved' && need.deliveryStatus ne 'Shipped'}">
                                    <a href="WarehouseServlet?action=arrangeDelivery&needId=${need.id}" class="btn-small">Arrange Delivery</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <div class="summary-section">
            <h2>Summary by Country</h2>
            <table>
                <thead>
                    <tr>
                        <th>Country</th>
                        <th>Total Pending</th>
                        <th>Total Approved</th>
                        <th>Total Rejected</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="summary" items="${countrySummary}">
                        <tr>
                            <td>${summary.country}</td>
                            <td>${summary.pendingCount}</td>
                            <td>${summary.approvedCount}</td>
                            <td>${summary.rejectedCount}</td>
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
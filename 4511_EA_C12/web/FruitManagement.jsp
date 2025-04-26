<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fruit Management - AIB Web System</title>
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
        <h1>Fruit Management (Branch: ${sessionScope.employeeBranch})</h1>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>

        <!-- 新增水果到分店庫存 -->
        <h2>Add Fruit to Branch Inventory</h2>
        <form action="FruitManagementServlet" method="post" class="filter-form">
            <input type="hidden" name="action" value="create">
            <label for="fruit">Select Fruit:</label>
            <select name="fruit" id="fruit" required>
                <option value="">-- Select a Fruit --</option>
                <c:forEach var="fruit" items="${availableFruits}">
                    <option value="${fruit.name}:${fruit.sourceCity}">${fruit.name} (${fruit.sourceCity}, ${fruit.country})</option>
                </c:forEach>
            </select>
            <label for="stockLevel">Stock Level:</label>
            <input type="number" name="stockLevel" id="stockLevel" min="0" required>
            <input type="submit" value="Add to Inventory">
        </form>

        <!-- 編輯分店庫存 -->
        <c:if test="${not empty editInventory}">
            <h2>Edit Branch Inventory</h2>
            <form action="FruitManagementServlet" method="post" class="filter-form">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="fruitName" value="${editInventory.name}">
                <input type="hidden" name="sourceCity" value="${editInventory.sourceCity}">
                <label>Fruit Name:</label>
                <span>${editInventory.name}</span>
                <label>Source City:</label>
                <span>${editInventory.sourceCity}</span>
                <label>Country:</label>
                <span>${editInventory.country}</span>
                <label for="stockLevel">Stock Level:</label>
                <input type="number" name="stockLevel" id="stockLevel" min="0" value="${editInventory.stockLevel}" required>
                <input type="submit" value="Update Inventory">
            </form>
        </c:if>

        <!-- 分店庫存清單 -->
        <h2>Branch Inventory</h2>
        <c:if test="${empty inventory}">
            <p class="error">No inventory found for this branch.</p>
        </c:if>
        <c:if test="${not empty inventory}">
            <table>
                <tr>
                    <th>Fruit Name</th>
                    <th>Source City</th>
                    <th>Country</th>
                    <th>Stock Level</th>
                    <th>Actions</th>
                </tr>
                <c:forEach var="item" items="${inventory}">
                    <tr>
                        <td>${item.name}</td>
                        <td>${item.sourceCity}</td>
                        <td>${item.country}</td>
                        <td>${item.stockLevel}</td>
                        <td>
                            <form action="FruitManagementServlet" method="get" class="action-form">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" name="fruitName" value="${item.name}">
                                <input type="hidden" name="sourceCity" value="${item.sourceCity}">
                                <input type="submit" value="Edit">
                            </form>
                            <form action="FruitManagementServlet" method="post" class="action-form">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="fruitName" value="${item.name}">
                                <input type="hidden" name="sourceCity" value="${item.sourceCity}">
                                <input type="submit" value="Delete" onclick="return confirm('Are you sure you want to delete this inventory item?');">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <p style="text-align: center; margin-top: 15px;">
            <a href="staffHome.jsp">Back to Home</a>
        </p>
    </div>
    <footer>
        © 2025 Acer International Bakery.
    </footer>
</body>
</html>
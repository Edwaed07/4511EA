<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.aib.bean.Fruit"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fruit List - AIB Web System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        
        <!-- 根据用户角色显示不同的导航栏 -->
        <nav>
            <ul>
                <c:choose>
                    <c:when test="${sessionScope.role eq 'Warehouse Staff'}">
                        <!-- 仓库职员导航栏 -->
                        <li><a href="warehouseHome.jsp">Home</a></li>
                        <li><a href="FruitServlet?page=fruitList">Fruits List</a></li>
                        <li><a href="warehouseCheckIn.jsp">Check-In Stock</a></li>
                        <li><a href="WarehouseServlet?action=viewTotalNeeds">Total Needs by Country</a></li>
                        <li><a href="WarehouseServlet?action=viewDeliveries">Manage Deliveries</a></li>
                        <li><a href="LogoutServlet">Logout</a></li>
                    </c:when>
                    <c:otherwise>
                        <!-- 商店职员导航栏 -->
                        <li><a href="staffHome.jsp">Home</a></li>
                        <li><a href="updateStockServlet">Shop Stock</a></li>
                        <li><a href="FruitServlet">Reserve Fruit</a></li>
                        <li><a href="FruitServlet?page=borrowFruit">Borrow Fruit</a></li>
                        <li><a href="CheckReserveServlet">Check Reservations</a></li>
                        <li><a href="FruitManagementServlet">Manage Fruits</a></li>
                        <li><a href="ApproveServlet">Approve Requests</a></li>
                        <li><a href="LogoutServlet">Logout</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
        
        <h1>Fruit List</h1>

        <!-- 显示员工分店（如果有） -->
        <c:if test="${not empty sessionScope.employeeBranch}">
            <h2>Branch: ${sessionScope.employeeBranch}</h2>
        </c:if>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>

        <!-- 使用 JSP Action 显示水果列表 -->
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Source Location</th>
                <th>Stock (box)</th>
                <th>Action</th>
            </tr>
            <%
                @SuppressWarnings("unchecked")
                List<Fruit> fruits = (List<Fruit>) request.getAttribute("fruits");
                if (fruits != null && !fruits.isEmpty()) {
                    for (int i = 0; i < fruits.size(); i++) {
                        pageContext.setAttribute("fruit", fruits.get(i));
            %>
                <jsp:useBean id="fruit" type="com.aib.bean.Fruit" scope="page"/>
                <tr>
                    <td><jsp:getProperty name="fruit" property="id"/></td>
                    <td><jsp:getProperty name="fruit" property="name"/></td>
                    <td><jsp:getProperty name="fruit" property="sourceCity"/></td>
                    <td><jsp:getProperty name="fruit" property="stockLevel"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${sessionScope.role eq 'Warehouse Staff'}">
                                <!-- 仓库职员不显示预订按钮 -->
                                <span>N/A</span>
                            </c:when>
                            <c:otherwise>
                                <!-- 商店职员显示预订按钮 -->
                                <form action="FruitServlet" method="post" class="action-form">
                                    <input type="hidden" name="page" value="reserveFruit">
                                    <input type="hidden" name="fruitId" value="<%= ((Fruit) pageContext.getAttribute("fruit")).getId() %>">
                                    <div class="stock-group">
                                        <input type="number" name="quantity" min="1" max="<%= ((Fruit) pageContext.getAttribute("fruit")).getStockLevel() %>" required>
                                        <input type="submit" value="Reserve">
                                    </div>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="5" class="error">No fruits found.</td>
                </tr>
            <%
                }
            %>
        </table>

        <p style="text-align: center; margin-top: 15px;">
            <c:choose>
                <c:when test="${sessionScope.role eq 'Warehouse Staff'}">
                    <a href="warehouseHome.jsp">Back to Home</a>
                </c:when>
                <c:otherwise>
                    <a href="staffHome.jsp">Back to Home</a>
                </c:otherwise>
            </c:choose>
        </p>
    </div>
    <footer>
        © 2025 Acer International Bakery.
    </footer>
</body>
</html>
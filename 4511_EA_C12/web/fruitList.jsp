<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.aib.model.Fruit"%>
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
        <h1>Fruit List</h1>


        <p class="success"><strong>Database Connection Status:</strong> ${connectionStatus}</p>


        <h2>Borrow Branch: ${sessionScope.employeeBranch}</h2>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>


        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Source City</th>
                <th>Stock (box)</th>
                <th>Action</th>
            </tr>
            <%
                List<Fruit> fruits = (List<Fruit>) request.getAttribute("fruits");
                if (fruits != null && !fruits.isEmpty()) {
                    for (int i = 0; i < fruits.size(); i++) {
                        pageContext.setAttribute("fruit", fruits.get(i));
            %>
                <jsp:useBean id="fruit" type="com.aib.model.Fruit" scope="page"/>
                <tr>
                    <td><jsp:getProperty name="fruit" property="id"/></td>
                    <td><jsp:getProperty name="fruit" property="name"/></td>
                    <td><jsp:getProperty name="fruit" property="sourceCity"/></td>
                    <td><jsp:getProperty name="fruit" property="stockLevel"/></td>
                    <td>
                        <form action="FruitServlet" method="post" class="action-form">
                            <input type="hidden" name="page" value="reserveFruit">
                            <input type="hidden" name="fruitId" value="<%= ((Fruit) pageContext.getAttribute("fruit")).getId() %>">
                            <div class="stock-group">
                                <input type="number" name="quantity" min="1" max="<%= ((Fruit) pageContext.getAttribute("fruit")).getStockLevel() %>" required>
                                <input type="submit" value="Reserve">
                            </div>
                        </form>
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
            <a href="staffHome.jsp">Back to Home</a>
        </p>
    </div>
    <footer>
        © 2025 Acer International Bakery.
    </footer>
</body>
</html>
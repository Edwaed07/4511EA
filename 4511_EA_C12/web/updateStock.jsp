<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.aib.util.DatabaseConnection"%>
<%@page import="java.sql.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Web System - Update Stock</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="warehouseHome.jsp">Home</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Update Stock</h1>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>

        <h2>Fruit Stock</h2>
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Current Stock</th>
                <th>Update Stock</th>
            </tr>
            <%
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM fruits")) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        int stock = rs.getInt("stock");
            %>
            <tr>
                <td><%= id %></td>
                <td><%= name %></td>
                <td><%= stock %></td>
                <td>
                    <form action="UpdateStockServlet" method="post">
                        <input type="hidden" name="fruitId" value="<%= id %>">
                        <input type="number" name="newStock" min="0" value="<%= stock %>" required>
                        <input type="submit" value="Update">
                    </form>
                </td>
            </tr>
            <%
                    }
                } catch (SQLException e) {
                    request.setAttribute("error", "Database error: " + e.getMessage());
                }
            %>
        </table>
    </div>
    <footer>
        © 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.aib.util.DatabaseConnection"%>
<%@page import="java.sql.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Web System - Borrow Fruit</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="borrowFruit.jsp">Borrow Fruit</a></li>
                <li><a href="reserveFruit.jsp">Reserve Fruit</a></li>
                <li><a href="checkReserve.jsp">Check Reservations</a></li>
                <li><a href="LogoutServlet.java">Logout</a></li>
            </ul>
        </nav>
        <h1>Borrow Fruit</h1>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>

        <h2>Available Fruits</h2>
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Stock</th>
                <th>Action</th>
            </tr>
            <%
                Connection conn = null;
                Statement stmt = null;
                ResultSet rs = null;
                try {
                    conn = DatabaseConnection.getConnection();
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery("SELECT * FROM fruits WHERE stock > 0");
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
                    <form action="BorrowFruitServlet" method="post">
                        <input type="hidden" name="fruitId" value="<%= id %>">
                        <input type="number" name="quantity" min="1" max="<%= stock %>" required>
                        <input type="submit" value="Borrow">
                    </form>
                </td>
            </tr>
            <%
                    }
                } catch (SQLException e) {
                    request.setAttribute("error", "Database error: " + e.getMessage());
                } finally {
                    if (rs != null) try { rs.close(); } catch (SQLException e) {}
                    if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
                    if (conn != null) try { conn.close(); } catch (SQLException e) {}
                }
            %>
        </table>
    </div>
    <footer>
        © 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html>
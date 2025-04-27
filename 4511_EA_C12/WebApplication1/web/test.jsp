<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="db.DBConnection, java.sql.Connection, java.sql.SQLException" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>数据库连接测试</title>
</head>
<body>
    <h1>数据库连接测试</h1>
    <%
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                out.println("<p style='color:green'>数据库连接成功！</p>");
                DBConnection.closeConnection(conn);
            } else {
                out.println("<p style='color:red'>数据库连接失败！</p>");
            }
        } catch (ClassNotFoundException e) {
            out.println("<p style='color:red'>错误: 找不到驱动类 - " + e.getMessage() + "</p>");
        } catch (SQLException e) {
            out.println("<p style='color:red'>错误: SQL异常 - " + e.getMessage() + "</p>");
        } catch (Exception e) {
            out.println("<p style='color:red'>错误: " + e.getMessage() + "</p>");
        }
    %>
</body>
</html> 
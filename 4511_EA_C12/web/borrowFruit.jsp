<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Borrow Fruits - AIB Web System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h1>Borrow Fruits (from shops in the same city)</h1>
    <form action="BorrowFruitServlet" method="post">
        <label>Fruit Name:</label>
        <input type="text" name="fruitName"><br>
        <label>Quantity:</label>
        <input type="number" name="quantity"><br>
        <label>Borrow From Shop:</label>
        <input type="text" name="borrowShopName"><br>
        <input type="submit" value="Borrow">
    </form>
    <a href="staffHome.jsp">Back to Home</a>
</body>
</html>
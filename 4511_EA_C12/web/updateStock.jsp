<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Fruit Stock - AIB Web System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h1>Update Fruit Stock Level</h1>
    <form action="UpdateStockServlet" method="post">
        <label>Fruit Name:</label>
        <input type="text" name="fruitName"><br>
        <label>New Stock Level:</label>
        <input type="number" name="stockLevel"><br>
        <input type="submit" value="Update">
    </form>
    <a href="staffHome.jsp">Back to Home</a>
</body>
</html>
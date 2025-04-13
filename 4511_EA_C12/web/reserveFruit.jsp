<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Reserve Fruits - AIB Web System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h1>Reserve Fruits (from source city)</h1>
    <form action="ReserveFruitServlet" method="post">
        <label>Fruit Name:</label>
        <input type="text" name="fruitName"><br>
        <label>Quantity:</label>
        <input type="number" name="quantity"><br>
        <label>Source City:</label>
        <input type="text" name="sourceCity"><br>
        <input type="submit" value="Reserve">
    </form>
    <a href="staffHome.jsp">Back to Home</a>
</body>
</html>
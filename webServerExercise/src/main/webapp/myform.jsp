<%--
  Created by IntelliJ IDEA.
  User: kevin
  Date: 2025/4/5
  Time: 下午 10:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="http://localhost:8080/sessionRegis" method="get">

    <label>Book ID:</label>
    <input type="text" name = "bookId">
    <br>

    <label>Book Name:</label>
    <input type="text" name = "name">
    <br>
    <label>Book Price:</label>
    <input type="number" name ="price">
    <br>
    <label>Author:</label>
    <input type="text" name ="author">
    <br>
    <input type="submit" value = "submit">
</form>
</body>
</html>

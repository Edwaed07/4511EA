<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>4511 Web System - Borrow Fruit</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">4511 Bakery</div>
        <nav>
            <ul>
                <li><a href="FruitServlet?page=borrowFruit">Borrow Fruit</a></li>
                <li><a href="reserveFruit.jsp">Reserve Fruit</a></li>
                <li><a href="checkReserve.jsp">Check Reservations</a></li>
                <li><a href="FruitServlet">Fruit List</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Borrow Fruit</h1>

        <p class="success"><strong>Database Connection Status:</strong> ${sessionScope.connectionStatus}</p>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>

        <h2>Available Fruits</h2>

        <form action="FruitServlet?page=borrowFruit" method="get" class="filter-form">
            <input type="hidden" name="page" value="borrowFruit">
            <label for="sourceCity">Source City:</label>
            <select name="sourceCity" id="sourceCity">
                <option value="">All Cities</option>
                <c:forEach var="city" items="${sourceCities}">
                    <option value="${city}" <c:if test="${city == selectedSourceCity}">selected</c:if>>${city}</option>
                </c:forEach>
            </select>
            <label for="branch">Branch:</label>
            <select name="branch" id="branch">
                <option value="">All Branches</option>
                <c:forEach var="branch" items="${branches}">
                    <option value="${branch}" <c:if test="${branch == selectedBranch}">selected</c:if>>${branch}</option>
                </c:forEach>
            </select>
            <label for="country">Country:</label>
            <select name="country" id="country">
                <option value="">All Countries</option>
                <c:forEach var="country" items="${countries}">
                    <option value="${country}" <c:if test="${country == selectedCountry}">selected</c:if>>${country}</option>
                </c:forEach>
            </select>
            <label for="minStock">Stock Range:</label>
            <input type="number" name="minStock" id="minStock" min="0" value="${minStock}" placeholder="Min">
            <label for="maxStock">to</label>
            <input type="number" name="maxStock" id="maxStock" min="0" value="${maxStock}" placeholder="Max">
            <input type="submit" value="Filter">
        </form>

        <c:if test="${not empty selectedSourceCity or not empty selectedBranch or not empty selectedCountry or not empty minStock or not empty maxStock}">
            <div class="filter-info">
                <span><strong>Filtered by:</strong></span>
                <span>
                    <c:choose>
                        <c:when test="${not empty selectedSourceCity}">${selectedSourceCity}</c:when>
                        <c:otherwise>All Cities</c:otherwise>
                    </c:choose>
                </span>
                <span>
                    <c:choose>
                        <c:when test="${not empty selectedBranch}">${selectedBranch}</c:when>
                        <c:otherwise>All Branches</c:otherwise>
                    </c:choose>
                </span>
                <span>
                    <c:choose>
                        <c:when test="${not empty selectedCountry}">${selectedCountry}</c:when>
                        <c:otherwise>All Countries</c:otherwise>
                    </c:choose>
                </span>
                <c:if test="${not empty minStock or not empty maxStock}">
                    <span>
                        <c:choose>
                            <c:when test="${not empty minStock and not empty maxStock}">${minStock} to ${maxStock}</c:when>
                            <c:when test="${not empty minStock}">${minStock} and above</c:when>
                            <c:when test="${not empty maxStock}">0 to ${maxStock}</c:when>
                        </c:choose>
                    </span>
                </c:if>
            </div>
        </c:if>

        <table>
            <tr>
                <th>Name</th>
                <th>Source City</th>
                <th>Country</th>
                <th>Branch</th>
                <th>Stock</th>
                <th>Action</th>
            </tr>
            <c:forEach var="fruit" items="${fruits}">
                <c:forEach var="branchEntry" items="${fruit.branchStocks}">
                    <tr>
                        <td>${fruit.name}</td>
                        <td>${fruit.sourceCity}</td>
                        <td>${fruit.country}</td>
                        <td>${branchEntry.key}</td>
                        <td>${branchEntry.value}</td>
                        <td>
                            <form action="FruitServlet" method="post" class="action-form">
                                <input type="hidden" name="page" value="borrowFruit">
                                <input type="hidden" name="fruitId" value="${fruit.id}">
                                <div class="branch-group">
                                    <label for="branch_${fruit.id}_${branchEntry.key}">Branch:</label>
                                    <select name="branch" id="branch_${fruit.id}_${branchEntry.key}">
                                        <c:forEach var="availBranch" items="${fruit.availableBranches}">
                                            <c:if test="${availBranch != selectedBranch}">
                                                <option value="${availBranch}" <c:if test="${availBranch == branchEntry.key}">selected</c:if>>${availBranch}</option>
                                            </c:if>
                                        </c:forEach>
                                        <c:if test="${empty fruit.availableBranches}">
                                            <option value="">No branches available</option>
                                        </c:if>
                                    </select>
                                </div>
                                <div class="borrow-group">
                                    <input type="number" name="quantity" min="1" max="${branchEntry.value}" required>
                                    <input type="submit" value="Borrow">
                                </div>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </c:forEach>
        </table>
    </div>
    <footer>
        © 2025 4511 Bakery. All rights reserved.
    </footer>
</body>
</html>
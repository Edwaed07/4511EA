<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User, model.Fruit, model.Inventory, model.Reserve, java.util.List" %>
<%@ include file="includes/header.jsp" %>

<div class="container">
    <h1 class="mb-4">Warehouse Dashboard</h1>
    
    <div class="row">
        <div class="col-md-6 mb-4">
            <div class="card h-100">
                <div class="card-header bg-primary text-white">
                    <h5 class="card-title mb-0">Inventory Overview</h5>
                </div>
                <div class="card-body">
                    <% 
                        List<Inventory> inventory = (List<Inventory>) request.getAttribute("inventory");
                        List<Fruit> fruits = (List<Fruit>) request.getAttribute("fruits");
                        if (inventory != null && !inventory.isEmpty()) {
                    %>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Fruit</th>
                                    <th>Current Stock</th>
                                    <th>Last Updated</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                    for (Inventory item : inventory) {
                                        String fruitName = "Unknown";
                                        if (fruits != null) {
                                            for (Fruit fruit : fruits) {
                                                if (fruit.getId() == item.getFruitId()) {
                                                    fruitName = fruit.getName();
                                                    break;
                                                }
                                            }
                                        }
                                %>
                                <tr>
                                    <td><%= fruitName %></td>
                                    <td><%= item.getQuantity() %></td>
                                    <td><%= item.getLastUpdated() %></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                    <a href="${pageContext.request.contextPath}/warehouse/inventory" class="btn btn-sm btn-outline-primary">View All</a>
                    <% } else { %>
                    <p class="text-muted">No inventory data available.</p>
                    <a href="${pageContext.request.contextPath}/warehouse/inventory" class="btn btn-sm btn-primary">Manage Inventory</a>
                    <% } %>
                </div>
            </div>
        </div>
        
        <div class="col-md-6 mb-4">
            <div class="card h-100">
                <div class="card-header bg-warning">
                    <h5 class="card-title mb-0">Pending Reservations</h5>
                </div>
                <div class="card-body">
                    <% 
                        List<Reserve> pendingReserves = (List<Reserve>) request.getAttribute("pendingReserves");
                        if (pendingReserves != null && !pendingReserves.isEmpty()) {
                    %>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Fruit</th>
                                    <th>Quantity</th>
                                    <th>Shop</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                    for (Reserve reserve : pendingReserves) {
                                        String fruitName = "Unknown";
                                        if (fruits != null) {
                                            for (Fruit fruit : fruits) {
                                                if (fruit.getId() == reserve.getFruitId()) {
                                                    fruitName = fruit.getName();
                                                    break;
                                                }
                                            }
                                        }
                                %>
                                <tr>
                                    <td><%= fruitName %></td>
                                    <td><%= reserve.getQuantity() %></td>
                                    <td><%= reserve.getShopName() %></td>
                                    <td><span class="badge bg-<%= reserve.getStatus().equals("pending") ? "warning" : "success" %>"><%= reserve.getStatus() %></span></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                    <a href="${pageContext.request.contextPath}/warehouse/reserves" class="btn btn-sm btn-outline-warning">View All</a>
                    <% } else { %>
                    <p class="text-muted">No pending reservations.</p>
                    <a href="${pageContext.request.contextPath}/warehouse/reserves" class="btn btn-sm btn-warning">Manage Reservations</a>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="includes/footer.jsp" %> 
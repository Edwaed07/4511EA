<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User, model.Fruit, model.Inventory, model.Delivery, java.util.List, java.util.Map, java.util.HashMap" %>
<%@ include file="includes/header.jsp" %>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Delivery Management</h1>
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#distributeToShopModal">
            Distribute to Shop
        </button>
    </div>
    
    <div class="card mb-4">
        <div class="card-header bg-primary text-white">
            <h5 class="card-title mb-0">Incoming Deliveries</h5>
        </div>
        <div class="card-body">
            <% 
                List<Delivery> incomingDeliveries = (List<Delivery>) request.getAttribute("incomingDeliveries");
                List<Fruit> fruits = (List<Fruit>) request.getAttribute("fruits");
                
                // Create fruit ID to name mapping
                Map<Integer, String> fruitMap = new HashMap<Integer, String>();
                if (fruits != null) {
                    for (Fruit fruit : fruits) {
                        fruitMap.put(fruit.getId(), fruit.getName());
                    }
                }
                
                if (incomingDeliveries != null && !incomingDeliveries.isEmpty()) {
            %>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Fruit</th>
                            <th>Source Location</th>
                            <th>Quantity</th>
                            <th>Delivery Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Delivery delivery : incomingDeliveries) { 
                           String fruitName = fruitMap.containsKey(delivery.getFruitId()) ? fruitMap.get(delivery.getFruitId()) : "Unknown";
                        %>
                        <tr>
                            <td><%= delivery.getId() %></td>
                            <td><%= fruitName %></td>
                            <td><%= delivery.getSourceLocation() %></td>
                            <td><%= delivery.getQuantity() %></td>
                            <td><%= delivery.getDeliveryDate() %></td>
                            <td>
                                <span class="badge bg-<%= delivery.getStatus().equals("pending") ? "warning" : 
                                                       (delivery.getStatus().equals("in-transit") ? "info" : 
                                                       (delivery.getStatus().equals("delivered") ? "success" : "secondary")) %>">
                                    <%= delivery.getStatus() %>
                                </span>
                            </td>
                            <td>
                                <% if (delivery.getStatus().equals("in-transit")) { %>
                                <form action="${pageContext.request.contextPath}/warehouse/deliveries" method="post" style="display: inline">
                                    <input type="hidden" name="action" value="complete">
                                    <input type="hidden" name="deliveryId" value="<%= delivery.getId() %>">
                                    <button type="submit" class="btn btn-sm btn-success">Complete Delivery</button>
                                </form>
                                <% } else { %>
                                <button type="button" class="btn btn-sm btn-secondary" disabled>Completed</button>
                                <% } %>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <p class="text-muted">No incoming deliveries.</p>
            <% } %>
        </div>
    </div>
    
    <div class="card mb-4">
        <div class="card-header bg-success text-white">
            <h5 class="card-title mb-0">Outgoing Deliveries</h5>
        </div>
        <div class="card-body">
            <% 
                List<Delivery> outgoingDeliveries = (List<Delivery>) request.getAttribute("outgoingDeliveries");
                if (outgoingDeliveries != null && !outgoingDeliveries.isEmpty()) {
            %>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Fruit</th>
                            <th>Destination</th>
                            <th>Type</th>
                            <th>Quantity</th>
                            <th>Delivery Date</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Delivery delivery : outgoingDeliveries) { 
                           String fruitName = fruitMap.containsKey(delivery.getFruitId()) ? fruitMap.get(delivery.getFruitId()) : "Unknown";
                        %>
                        <tr>
                            <td><%= delivery.getId() %></td>
                            <td><%= fruitName %></td>
                            <td><%= delivery.getDestinationLocation() %></td>
                            <td><%= delivery.getDestinationType() %></td>
                            <td><%= delivery.getQuantity() %></td>
                            <td><%= delivery.getDeliveryDate() %></td>
                            <td>
                                <span class="badge bg-<%= delivery.getStatus().equals("pending") ? "warning" : 
                                                       (delivery.getStatus().equals("in-transit") ? "info" : 
                                                       (delivery.getStatus().equals("delivered") ? "success" : "secondary")) %>">
                                    <%= delivery.getStatus() %>
                                </span>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <p class="text-muted">No outgoing deliveries.</p>
            <% } %>
        </div>
    </div>
    
    <div class="card">
        <div class="card-header bg-info text-white">
            <h5 class="card-title mb-0">Current Inventory</h5>
        </div>
        <div class="card-body">
            <% 
                List<Inventory> inventory = (List<Inventory>) request.getAttribute("inventory");
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
                        <% for (Inventory item : inventory) { 
                           String fruitName = fruitMap.containsKey(item.getFruitId()) ? fruitMap.get(item.getFruitId()) : "Unknown";
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
            <% } else { %>
            <p class="text-muted">No inventory data.</p>
            <% } %>
        </div>
    </div>
</div>

<!-- Distribute to Shop Modal -->
<div class="modal fade" id="distributeToShopModal" tabindex="-1" aria-labelledby="distributeToShopModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="distributeToShopModalLabel">Distribute to Shop</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="${pageContext.request.contextPath}/warehouse/deliveries" method="post">
                <div class="modal-body">
                    <input type="hidden" name="action" value="distribute">
                    
                    <div class="mb-3">
                        <label for="fruitId" class="form-label">Fruit</label>
                        <select class="form-select" id="fruitId" name="fruitId" required>
                            <option value="">Select fruit...</option>
                            <% if (inventory != null) {
                                for (Inventory item : inventory) {
                                    if (item.getQuantity() > 0) {
                                        String fruitName = fruitMap.containsKey(item.getFruitId()) ? fruitMap.get(item.getFruitId()) : "Unknown";
                            %>
                            <option value="<%= item.getFruitId() %>"><%= fruitName %> (Stock: <%= item.getQuantity() %>)</option>
                            <% 
                                    }
                                }
                            } %>
                        </select>
                    </div>
                    
                    <div class="mb-3">
                        <label for="shop" class="form-label">Shop Name</label>
                        <input type="text" class="form-control" id="shop" name="shop" required>
                    </div>
                    
                    <div class="mb-3">
                        <label for="city" class="form-label">City</label>
                        <input type="text" class="form-control" id="city" name="city" required>
                    </div>
                    
                    <div class="mb-3">
                        <label for="quantity" class="form-label">Quantity</label>
                        <input type="number" class="form-control" id="quantity" name="quantity" min="1" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Confirm</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="includes/footer.jsp" %> 
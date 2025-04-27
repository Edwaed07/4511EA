<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User, model.Fruit, model.Inventory, java.util.List, java.util.Map, java.util.HashMap" %>
<%@ include file="includes/header.jsp" %>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Inventory Management</h1>
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#updateInventoryModal">
            <i class="bi bi-plus-circle"></i> Update Inventory
        </button>
    </div>
    
    <div class="card mb-4">
        <div class="card-header bg-primary text-white">
            <h5 class="card-title mb-0">Current Inventory</h5>
        </div>
        <div class="card-body">
            <% 
                List<Inventory> inventory = (List<Inventory>) request.getAttribute("inventory");
                List<Fruit> fruits = (List<Fruit>) request.getAttribute("fruits");
                
                // Create fruit ID to name mapping
                Map<Integer, String> fruitMap = new HashMap<Integer, String>();
                if (fruits != null) {
                    for (Fruit fruit : fruits) {
                        fruitMap.put(fruit.getId(), fruit.getName());
                    }
                }
                
                if (inventory != null && !inventory.isEmpty()) {
            %>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Fruit</th>
                            <th>Location Type</th>
                            <th>Location Name</th>
                            <th>Current Stock</th>
                            <th>Last Updated</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Inventory item : inventory) { 
                           String fruitName = fruitMap.containsKey(item.getFruitId()) ? fruitMap.get(item.getFruitId()) : "Unknown";
                        %>
                        <tr>
                            <td><%= item.getId() %></td>
                            <td><%= fruitName %></td>
                            <td><%= item.getLocationType() %></td>
                            <td><%= item.getLocationName() %></td>
                            <td><%= item.getQuantity() %></td>
                            <td><%= item.getLastUpdated() %></td>
                            <td>
                                <button type="button" class="btn btn-sm btn-outline-primary" 
                                        onclick="prepareUpdate('<%= item.getFruitId() %>', '<%= fruitName %>', '<%= item.getQuantity() %>')">
                                    Update
                                </button>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <p class="text-muted">No inventory data available. Please add inventory.</p>
            <% } %>
        </div>
    </div>
    
    <div class="card">
        <div class="card-header bg-info text-white">
            <h5 class="card-title mb-0">Available Fruits</h5>
        </div>
        <div class="card-body">
            <% if (fruits != null && !fruits.isEmpty()) { %>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Source Country</th>
                            <th>Description</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Fruit fruit : fruits) { %>
                        <tr>
                            <td><%= fruit.getId() %></td>
                            <td><%= fruit.getName() %></td>
                            <td><%= fruit.getSourceCountry() %></td>
                            <td><%= fruit.getDescription() %></td>
                            <td>
                                <button type="button" class="btn btn-sm btn-outline-primary" 
                                        onclick="prepareUpdate('<%= fruit.getId() %>', '<%= fruit.getName() %>', '0')">
                                    Add Inventory
                                </button>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <p class="text-muted">No fruit data available.</p>
            <% } %>
        </div>
    </div>
</div>

<!-- Update Inventory Modal -->
<div class="modal fade" id="updateInventoryModal" tabindex="-1" aria-labelledby="updateInventoryModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateInventoryModalLabel">Update Inventory</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="${pageContext.request.contextPath}/warehouse/inventory" method="post">
                <div class="modal-body">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" id="fruitId" name="fruitId" value="">
                    
                    <div class="mb-3">
                        <label for="fruitName" class="form-label">Fruit Name</label>
                        <input type="text" class="form-control" id="fruitName" readonly>
                    </div>
                    
                    <div class="mb-3">
                        <label for="locationType" class="form-label">Location Type</label>
                        <select class="form-select" id="locationType" name="locationType" required>
                            <option value="centralWarehouse">Central Warehouse</option>
                        </select>
                    </div>
                    
                    <div class="mb-3">
                        <label for="quantity" class="form-label">Quantity</label>
                        <input type="number" class="form-control" id="quantity" name="quantity" min="0" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    function prepareUpdate(fruitId, fruitName, quantity) {
        document.getElementById('fruitId').value = fruitId;
        document.getElementById('fruitName').value = fruitName;
        document.getElementById('quantity').value = quantity;
        
        // Open modal
        var myModal = new bootstrap.Modal(document.getElementById('updateInventoryModal'));
        myModal.show();
    }
</script>

<%@ include file="includes/footer.jsp" %> 
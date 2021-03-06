package ecommerce.system.api.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class StoreCashFlowByOrderReportModel {

    private UUID id;
    private int storeId;
    private int orderId;
    private double value;
    private int productId;
    private String productName;
    private int productQuantity;
    private LocalDateTime timestamp;

    public StoreCashFlowByOrderReportModel(UUID id, int storeId, int orderId, double value, int productId, String productName, int productQuantity, LocalDateTime timestamp) {
        this.id = id;
        this.storeId = storeId;
        this.orderId = orderId;
        this.value = value;
        this.productId = productId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

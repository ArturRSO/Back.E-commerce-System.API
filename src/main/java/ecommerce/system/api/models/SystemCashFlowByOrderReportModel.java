package ecommerce.system.api.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class SystemCashFlowByOrderReportModel {

    private UUID id;
    private int orderId;
    private String storeName;
    private LocalDateTime timestamp;

    public SystemCashFlowByOrderReportModel(UUID id, int orderId, String storeName, LocalDateTime timestamp) {
        this.id = id;
        this.orderId = orderId;
        this.storeName = storeName;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
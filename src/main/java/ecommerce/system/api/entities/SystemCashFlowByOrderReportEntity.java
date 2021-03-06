package ecommerce.system.api.entities;

import ecommerce.system.api.models.SystemCashFlowByOrderReportModel;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "SystemCashFlowByOrderReportEntity")
@Immutable
@Table(name = "vw_systemCashFlowByOrder")
public class SystemCashFlowByOrderReportEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "orderId")
    private int orderId;

    @Column(name = "storeId")
    private int storeId;

    @Column(name = "storeName")
    private String storeName;

    @Column(name = "value")
    private double value;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public SystemCashFlowByOrderReportEntity() {
    }

    public SystemCashFlowByOrderReportEntity(UUID id, int orderId, int storeId, String storeName, double value, LocalDateTime timestamp) {
        this.id = id;
        this.orderId = orderId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.value = value;
        this.timestamp = timestamp;
    }

    public SystemCashFlowByOrderReportModel toModel() {
        return new SystemCashFlowByOrderReportModel(this.id, this.orderId, this.storeId, this.storeName, this.value, this.timestamp);
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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

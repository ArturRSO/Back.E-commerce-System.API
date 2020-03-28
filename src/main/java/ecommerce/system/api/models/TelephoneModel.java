package ecommerce.system.api.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class TelephoneModel {

    private int telephoneId;

    @NotNull
    private int userId;

    @NotNull
    private int telephoneTypeId;

    @NotNull
    @Size(min = 2, max = 5)
    private String internationalCode;

    @NotNull
    private int localCode;

    @NotNull
    @Size(min = 8, max = 9)
    private String number;

    private LocalDateTime creationDate;
    private LocalDateTime lastUpdate;

    public TelephoneModel(int telephoneId,
                          int userId,
                          int telephoneTypeId,
                          String internationalCode,
                          int localCode,
                          String number,
                          LocalDateTime creationDate,
                          LocalDateTime lastUpdate) {
        this.telephoneId = telephoneId;
        this.userId = userId;
        this.telephoneTypeId = telephoneTypeId;
        this.internationalCode = internationalCode;
        this.localCode = localCode;
        this.number = number;
        this.creationDate = creationDate;
        this.lastUpdate = lastUpdate;
    }

    public int getTelephoneId() {
        return telephoneId;
    }

    public void setTelephoneId(int telephoneId) {
        this.telephoneId = telephoneId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTelephoneTypeId() {
        return telephoneTypeId;
    }

    public void setTelephoneTypeId(int telephoneTypeId) {
        this.telephoneTypeId = telephoneTypeId;
    }

    public String getInternationalCode() {
        return internationalCode;
    }

    public void setInternationalCode(String internationalCode) {
        this.internationalCode = internationalCode;
    }

    public int getLocalCode() {
        return localCode;
    }

    public void setLocalCode(int localCode) {
        this.localCode = localCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

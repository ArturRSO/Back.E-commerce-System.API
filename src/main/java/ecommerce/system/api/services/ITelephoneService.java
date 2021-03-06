package ecommerce.system.api.services;

import ecommerce.system.api.exceptions.InvalidOperationException;
import ecommerce.system.api.models.TelephoneModel;

import java.io.IOException;
import java.util.List;

public interface ITelephoneService {

    int createTelephone(TelephoneModel telephone, boolean relateWithUser) throws InvalidOperationException;
    List<TelephoneModel> getTelephonesByUserId(int userId) throws InvalidOperationException;
    TelephoneModel getTelephoneById(int telephoneId);
    void updateTelephone(TelephoneModel telephone) throws InvalidOperationException;
    void deleteTelephone(int telephoneId) throws InvalidOperationException, IOException;
}

package ecommerce.system.api.repositories.implementations;

import ecommerce.system.api.entities.AddressEntity;
import ecommerce.system.api.entities.UserAddressEntity;
import ecommerce.system.api.entities.embedded.UserAddressKey;
import ecommerce.system.api.models.AddressModel;
import ecommerce.system.api.repositories.IAddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(rollbackOn = {Exception.class})
public class AddressRepository implements IAddressRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public int create(AddressModel object) {

        AddressEntity address = new AddressEntity(object);

        this.entityManager.persist(address);
        this.entityManager.flush();

        return address.getAddressId();
    }

    @Override
    public void relateAddressAndUser(int userId, int adddressId) {

        UserAddressKey userAddressKey = new UserAddressKey(userId, adddressId);
        UserAddressEntity userAddress = new UserAddressEntity(userAddressKey);

        this.entityManager.persist(userAddress);
    }

    @Override
    public AddressModel getById(int id) {

        try {

            String query = "FROM AddressEntity a WHERE a.active = true AND a.addressId = :addressId";
            TypedQuery<AddressEntity> result = this.entityManager.createQuery(query, AddressEntity.class)
                    .setParameter("addressId", id);
            AddressEntity address = result.getSingleResult();

            return address.toModel();

        } catch (NoResultException nre) {

            logger.error(nre.getMessage());

            return null;
        }
    }

    @Override
    public List<AddressModel> getAddressesByUserId(int userId) {

        String query = "SELECT a FROM AddressEntity a, UserAddressEntity ua WHERE a.active = true AND ua.id.userId = :userId AND a.addressId = ua.id.addressId ORDER BY a.addressId ASC";
        TypedQuery<AddressEntity> result = this.entityManager.createQuery(query, AddressEntity.class)
                .setParameter("userId", userId);
        List<AddressEntity> entities = result.getResultList();

        if (entities == null || entities.isEmpty()) {
            return null;
        }

        ArrayList<AddressModel> addresses = new ArrayList<>();
        (entities).forEach((address) -> addresses.add(address.toModel()));

        return addresses;
    }

    @Override
    public boolean update(AddressModel object) {

        AddressEntity address = this.entityManager.find(AddressEntity.class, object.getAddressId());

        if (address == null || !address.isActive()) {
            return false;
        }

        object.setCreationDate(address.getCreationDate());
        object.setActive(address.isActive());

        AddressEntity updatedAddress = new AddressEntity(object);
        this.entityManager.merge(updatedAddress);

        return true;
    }

    @Override
    public boolean delete(int id) {

        AddressEntity address = this.entityManager.find(AddressEntity.class, id);

        if (address == null || !address.isActive()) {
            return false;
        }

        address.setActive(false);
        address.setLastUpdate(LocalDateTime.now());

        this.entityManager.merge(address);

        return true;
    }
}

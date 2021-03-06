package ecommerce.system.api.services.implementations;

import ecommerce.system.api.enums.MessagesEnum;
import ecommerce.system.api.enums.NotificationsEnum;
import ecommerce.system.api.enums.OrderStatusEnum;
import ecommerce.system.api.enums.RolesEnum;
import ecommerce.system.api.exceptions.InvalidOperationException;
import ecommerce.system.api.exceptions.InvalidTokenException;
import ecommerce.system.api.models.OrderModel;
import ecommerce.system.api.models.UserModel;
import ecommerce.system.api.repositories.IUserRepository;
import ecommerce.system.api.services.*;
import ecommerce.system.api.tools.NotificationHandler;
import ecommerce.system.api.tools.SHAEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService implements IUserService {

    private final IAuthenticationService authenticationService;
    private final IFileService fileService;
    private final IOrderService orderService;
    private final IStoreService storeService;
    private final IUserRepository userRepository;
    private final SHAEncoder shaEncoder;
    private final NotificationHandler notificationHandler;

    @Value("${application.image-path-users-default}")
    private String defaultProfileImagePath;

    @Autowired
    public UserService(IAuthenticationService authenticationService,
                       IFileService fileService,
                       @Lazy IOrderService orderService,
                       @Lazy IStoreService storeService,
                       IUserRepository userRepository,
                       SHAEncoder shaEncoder,
                       NotificationHandler notificationHandler) {
        this.authenticationService = authenticationService;
        this.fileService = fileService;
        this.orderService = orderService;
        this.storeService = storeService;
        this.userRepository = userRepository;
        this.shaEncoder = shaEncoder;
        this.notificationHandler = notificationHandler;
    }

    @Override
    public int createUser(UserModel user) throws NoSuchAlgorithmException, InvalidOperationException {

        String userRole = RolesEnum.getRoleById(user.getRoleId());

        if (userRole == null) {
            throw new InvalidOperationException(MessagesEnum.UNALLOWED.getMessage());
        }

        if (userRole.equals("system_admin") && this.authenticationService.isNotSystemAdmin()) {
            throw new InvalidOperationException(MessagesEnum.UNALLOWED.getMessage());
        }

        String encodedPassword = this.shaEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        user.setProfileImage(this.defaultProfileImagePath);
        user.setCreationDate(LocalDateTime.now());
        user.setLastUpdate(null);
        user.setVerifiedEmail(true);
        user.setActive(true);

        UserModel checkedUser = this.userRepository.getUserByDocumentNumber(user.getDocumentNumber());

        if (checkedUser == null) {
            checkedUser = this.userRepository.getUserByEmail(user.getEmail());

            if (checkedUser != null) {

                if (!checkedUser.isActive()) {
                    checkedUser.setEmail(checkedUser.getEmail() + " [Inactive]");
                    this.userRepository.update(checkedUser);

                } else {
                    throw new InvalidOperationException("Já existe um usuário cadastrado com o e-mail informado.");
                }
            }

            return this.userRepository.create(user);

        } else {

            if (checkedUser.isActive()) {
                throw new InvalidOperationException("Já existe um usuário cadastrado com o número do documento informado.");

            } else {

                if (checkedUser.getEmail().equals(user.getEmail())) {
                    checkedUser.setEmail(checkedUser.getEmail() + " [Inactive]");
                }

                checkedUser.setDocumentNumber(checkedUser.getDocumentNumber() + " [Inactive]");
                this.userRepository.update(checkedUser);

               return this.userRepository.create(user);
            }
        }
    }

    @Override
    public void createProfileImage(MultipartFile file, int userId) throws InvalidOperationException, IOException {

        if (!this.authenticationService.isLoggedUser(userId)) {
            throw new InvalidOperationException(MessagesEnum.UNALLOWED.getMessage());
        }

        String imagePath = this.fileService.saveMultpartImage(file, "user", userId);

        UserModel user = this.userRepository.getById(userId);
        user.setProfileImage(imagePath);
        user.setLastUpdate(LocalDateTime.now());

        if (!this.userRepository.update(user)) {
            throw new InvalidOperationException("Usuário não encontrado!");
        }
    }

    @Override
    public List<UserModel> getAllUsers() throws IOException {

        List<UserModel> users = this.userRepository.getAllUsers();

        for (UserModel user : users) {
            user.setProfileImage(this.fileService.getImageBase64(user.getProfileImage()));
        }

        return users;
    }

    @Override
    public List<UserModel> getUsersByRoleId(int roleId) throws IOException {

        List<UserModel> users = this.userRepository.getUsersByRoleId(roleId);

        for (UserModel user : users) {
            user.setProfileImage(this.fileService.getImageBase64(user.getProfileImage()));
        }

        return users;
    }

    @Override
    public List<UserModel> getUsersByStoreId(int storeId) throws IOException {

        List<UserModel> users = this.userRepository.getUsersByStoreId(storeId);

        for (UserModel user : users) {
            user.setProfileImage(this.fileService.getImageBase64(user.getProfileImage()));
        }

        return users;
    }

    @Override
    public UserModel getUserById(int id, boolean imagePath) throws IOException {

        UserModel user = this.userRepository.getById(id);

        if (!imagePath) {
            user.setProfileImage(this.fileService.getImageBase64(user.getProfileImage()));
        }

        return user;
    }

    @Override
    public UserModel getUserByEmail(String email) {

        return this.userRepository.getUserByEmail(email);
    }

    @Override
    public UserModel getUserProfile() throws IOException {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserModel user = this.getUserByEmail(email);

        user.setProfileImage(this.fileService.getImageBase64(user.getProfileImage()));

        return user;
    }

    @Override
    public boolean checkPasswordRecoverToken(String token) throws Exception {

        return this.notificationHandler.validateToken(token, NotificationsEnum.PASSWORD_RECOVER);
    }

    @Override
    public boolean sendPasswordRecoverEmail(String email) throws Exception {

        UserModel user = this.getUserByEmail(email);

        if (user != null) {

            this.notificationHandler.sendEmail(user.getUserId(), user.getEmail(), NotificationsEnum.PASSWORD_RECOVER, null);

            return true;
        }

        return false;
    }

    @Override
    public void recoverPassword(String password, String token) throws Exception {

        if (this.checkPasswordRecoverToken(token)) {

            int userId = this.notificationHandler.extractUserId(token, NotificationsEnum.PASSWORD_RECOVER);

            this.updateUserPassword(true, userId, password);

        } else {
            throw new InvalidTokenException("Token expirado");
        }
    }

    @Override
    public void updateUserPassword(boolean isRecover, int userId, String password)
            throws InvalidOperationException, NoSuchAlgorithmException {

        if (!isRecover) {
            if (!this.authenticationService.isLoggedUser(userId)) {
                throw new InvalidOperationException(MessagesEnum.UNALLOWED.getMessage());
            }
        }

        UserModel user = this.userRepository.getById(userId);

        if (user == null) {
            throw new InvalidOperationException("Usuário não encontrado!");
        }

        String encodedPassword = this.shaEncoder.encode(password);

        if (user.getPassword().equals(encodedPassword)) {
            throw new InvalidOperationException("Nova senha não pode ser igual a anterior!");
        }

        user.setPassword(encodedPassword);
        user.setLastUpdate(LocalDateTime.now());

        if (!this.userRepository.update(user)) {
            throw new InvalidOperationException("Usuário não encontrado!");
        }
    }

    @Override
    public void updateUser(UserModel user) throws InvalidOperationException, IOException {

        if (!this.authenticationService.isLoggedUser(user.getUserId())) {
            throw new InvalidOperationException(MessagesEnum.UNALLOWED.getMessage());
        }

        UserModel oldUser = this.getUserById(user.getUserId(), true);

        if (oldUser == null) {
            throw new InvalidOperationException("Usuário não encontrado!");
        }

        user.setRoleId(oldUser.getRoleId());
        user.setProfileImage(oldUser.getProfileImage());
        user.setPassword(oldUser.getPassword());
        user.setCreationDate(oldUser.getCreationDate());
        user.setVerifiedEmail(oldUser.isVerifiedEmail());
        user.setActive(oldUser.isActive());
        user.setLastUpdate(LocalDateTime.now());

        if (!this.userRepository.update(user)) {
            throw new InvalidOperationException("Usuário não encontrado!");
        }
    }

    @Override
    public void deleteUser(int userId) throws InvalidOperationException, IOException {

        UserModel user = this.userRepository.getById(userId);

        if (user == null) {
            throw new InvalidOperationException("Usuário não encontrado.");
        }

        if (!this.authenticationService.isLoggedUser(userId) || user.getRoleId() != RolesEnum.SYSTEM_ADMIN.getId()) {
            throw new InvalidOperationException(MessagesEnum.UNALLOWED.getMessage());
        }

        if (user.getRoleId() == RolesEnum.STORE_ADMIN.getId()) {

            if (this.storeService.getStoresByUserId(userId) != null) {
                throw new InvalidOperationException("Não é possível desativar um perfil associado a uma loja ativa.");
            }

        } else if (user.getRoleId() == RolesEnum.CUSTOMER.getId()) {

            List<OrderModel> orders = this.orderService.getOrderSummariesByUserId(userId);

            for (OrderModel order : orders) {

                if (order.getOrderStatusId() != OrderStatusEnum.FINISHED.getId()) {
                    throw new InvalidOperationException("Não é possível desativar um perfil com pedidos em aberto.");
                }
            }
        }

        this.userRepository.delete(userId);
    }
}

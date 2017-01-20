package pfe.ece.LinkUS.Service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Exception.EmailExistsException;
import pfe.ece.LinkUS.Exception.UnauthorizedInformationException;
import pfe.ece.LinkUS.Exception.UserNotFoundException;
import pfe.ece.LinkUS.Model.Enum.Role;
import pfe.ece.LinkUS.Model.FriendGroup;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Model.UserCreateForm;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.FriendGroupRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.UserEntityService.UserServiceImpl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 12/10/2016.
 */
@Service
public class UserService {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.UserService");

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(String userId) {
        User user = userRepository.findOne(userId);
        if(user == null) {
            throw new UserNotFoundException(userId);
        } else {
            return user;
        }
    }

    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findOneByEmail(email);
        if(user.get() == null) {
            throw new UserNotFoundException(email);
        } else {
            return user.get();
        }
    }

    public boolean checkUserByEmail(String email) {
        Optional<User> user = userRepository.findOneByEmail(email);
        if(user.get() == null) {
            return false;
        } else {
            return true;
        }
    }

    public List<User> searchUserByPartialFirstnameOrLastname(String textToFind) {

        List<User> userList = userRepository.findByFirstNameLikeOrLastNameLike(textToFind, textToFind, createPageRequest());
        // TODO: Tester pageable

        return userList;
    }

    public List<User> findUsersByLastName(String name) {
        List<User> userList = userRepository.findByLastNameIgnoreCase(name);

        if(userList == null || userList.isEmpty()) {
            throw new UserNotFoundException(name);
        } else {
            return userList;
        }
    }

    public List<User> findUsersByIds(List<String> userIdList) {

        List<User> userList = new ArrayList<>();

        for (String userId: userIdList) {
            userList.add(findUserById(userId));
        }

        return userList;
    }

    public void save(User user) {
        // Set to null not to erase another object with the same Id (new object)
        user.setId(null);
        LOGGER.info("Saving new user" + user.toString());
        userRepository.save(user);
    }

    private void update(User user) {
        LOGGER.info("Updating user" + user.toString());
        userRepository.save(user);
    }

    private void delete(User user) {
        LOGGER.info("Deleting user" + user.toString());
        userRepository.delete(user);
    }

    public boolean checkFriend(User user, String friendId) {

        return user.getFriendList().contains(friendId);
    }

    public boolean friendRequest(String userId, String friendId) {

        User user = findUserById(userId);

        if(!user.getFriendPendingList().contains(friendId)) {
            LOGGER.info("New friend request with friendID: " + friendId);
            user.getFriendPendingList().add(friendId);
            update(user);
            return true;
        }
        return false;
    }

    public boolean acceptFriend(String userId, String friendId) {

        User user = findUserById(userId);

        if(user.getFriendPendingList().contains(friendId)) {
            LOGGER.info("New friend with friendID: " + friendId);
            user.getFriendList().add(friendId);
            user.getFriendPendingList().remove(friendId);
            update(user);
            return true;
        }
        return false;
    }

    public boolean refuseFriend(String userId, String friendId) {

        User user = findUserById(userId);

        if(user.getFriendPendingList().contains(friendId)) {
            LOGGER.info("Friend request refused with friendID: " + friendId);
            user.getFriendPendingList().remove(friendId);
            update(user);
            return true;
        }
        return false;
    }

    public boolean removeFriend(String userId, String friendId) {

        User user = findUserById(userId);

        if(user.getFriendList().contains(friendId)) {
            LOGGER.info("Friend with friendID: " + friendId + " removed.");
            user.getFriendList().remove(friendId);
            update(user);
            return true;
        }
        return false;
    }

    public List<User> findFriends(String userId) {

        User user = findUserById(userId);
        return findUsersByIds(user.getFriendList());
    }

    public User findFriend(String userId, String friendId) {

        User user = findUserById(userId);

        if (user.getFriendList().contains(friendId)) {
            return findUserById(friendId);
        } else {
            throw new UnauthorizedInformationException();
        }
    }

    public List<FriendGroup> findFriendGroupsOwned(FriendGroupRepository friendGroupRepository ,String userId) {

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        return friendGroupService.findFriendGroupsByOwnerId(userId);
    }

    public void checkData(List<User> userList) {

        if(userList != null) {
            for (User user: userList) {
                checkData(user);
            }
        }
    }

    public void checkData(User user) {

        if(user != null) {
            // Lors de l'envoi de user, le password, role et image de profil ne doivent pas figurer.
            user.setPasswordHash(null);
            user.setRole(null);
            user.setProfilImgUrl(null);
        }
    }
    public void checkNotificationEnabledString(List<String> userIdList) {

        List<User> userList = findUsersByIds(userIdList);
        userIdList = checkNotificationEnabledUser(userList);
    }

    public List<String> checkNotificationEnabledUser(List<User> userList) {
        List<String> userIdListChecked = new ArrayList<>();
        if(userList != null) {
            for(User user: userList) {
                if(user.getConfigUser().isReceiveNotification()) {
                    userIdListChecked.add(user.getId());
                }
            }
        }
        return userIdListChecked;
    }

    private Pageable createPageRequest() {
        return createPageRequest(0);
    }

    private Pageable createPageRequest(int page) {
        return new PageRequest(page, 20, Sort.Direction.ASC, "lastName", "firstName");
    }

    public String createFakeUser(String name) throws EmailExistsException, ParseException {
        pfe.ece.LinkUS.Service.UserEntityService.UserService userService = new UserServiceImpl(userRepository);
        User user = new User();
        user.setEmail(name + "@yopmail.com");
        user.setDateofBirth(new Date(117, 0, 17, 17, 17));
        user.setFirstName(name);
        user.setLastName("Corea");
        user.setSexe("Male");
        user.setRole(Role.USER);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Pattern corresponding to ISO Format
        Date timestamp = dateFormat.parse(dateFormat.format(new Date()));

        user.setDateofRegistration(timestamp); // Setting the date of the registration
        user.setPasswordHash(new BCryptPasswordEncoder().encode("1".concat(timestamp.toString())));
        user.setEnabled(true);

        save(user);

        // PARTIE LOCALE
        File directory = new File("./images/" + user.getId());
        if (!directory.exists()) {
            directory.mkdir();
        }

        return user.getId();
    }

    public boolean removeFakeUser(String name) throws IOException {

        return removeUser(name + "@yopmail.com");
    }

    public boolean removeUser(String email) throws IOException {

        if(checkUserByEmail(email)) {
            User user = findUserByEmail(email);
            FileUtils.deleteDirectory(new File("./images/" + user.getId()));
            delete(user);
            return true;
        }

        return false;
    }

    public void addFakeFriend(String userId, String friendId) {

        friendRequest(userId, friendId);
        acceptFriend(userId, friendId);
    }
}

package pfe.ece.LinkUS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Exception.UserNotFoundException;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;

import java.util.ArrayList;
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

    @Autowired
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

    public User findUserByUsername(String username) {
        Optional<User> user = userRepository.findOneByEmail(username);
        if(user.get() == null) {
            throw new UserNotFoundException(username);
        } else {
            return user.get();
        }
    }

    public List<User> findUsersByName(String name) {
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

    private void save(User user) {
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

    public void friendRequest(String userId, String friendId) {

        User user = findUserById(userId);

        if(!user.getFriendPendingList().contains(friendId)) {
            LOGGER.info("New friend request with friendID: " + friendId);
            user.getFriendPendingList().add(friendId);
        }
        update(user);

    }

    public void acceptFriend(String userId, String friendId) {

        User user = findUserById(userId);

        if(user.getFriendPendingList().contains(friendId)) {
            LOGGER.info("New friend with friendID: " + friendId);
            user.getFriendList().add(friendId);
            user.getFriendPendingList().remove(friendId);
        }
        update(user);
    }

    public void refuseFriend(String userId, String friendId) {

        User user = findUserById(userId);

        if(user.getFriendPendingList().contains(friendId)) {
            LOGGER.info("Friend request refused with friendID: " + friendId);
            user.getFriendPendingList().remove(friendId);
        }
        update(user);
    }


}

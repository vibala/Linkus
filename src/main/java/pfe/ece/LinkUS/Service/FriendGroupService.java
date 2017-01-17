package pfe.ece.LinkUS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.FriendGroup;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.FriendGroupRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 14/10/2016.
 */
@Service
public class FriendGroupService {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.FriendGroupService");

    FriendGroupRepository friendGroupRepository;

    public FriendGroupService(FriendGroupRepository friendGroupRepository) {
        this.friendGroupRepository = friendGroupRepository;
    }

    public List<FriendGroup> findFriendGroupsByIds(List<String> idList) {
        List<FriendGroup> friendGroupList = new ArrayList<>();

        for (String id: idList) {
            friendGroupList.add(findFriendGroupById(id));
        }
        return friendGroupList;
    }

    public List<FriendGroup> findFriendGroupsByOwnerId(String ownerId) {
        List<FriendGroup> friendGroupList = new ArrayList<>();

        return friendGroupRepository.findByOwnerId((ownerId));
    }

    public FriendGroup findFriendGroupById(String id) {
        FriendGroup friendGroup = friendGroupRepository.findOne(id);
        if (friendGroup != null) {
             return friendGroup;
        }
        return null;
    }

    /**
     * Find friend group by UserId (every friendGroup where the user is)
     */
    public List<FriendGroup> findFriendGroupByUserId(String id) {
        return friendGroupRepository.findFriendGroupByMembers(id);
    }

    public List<FriendGroup> findFriendGroupByOwnerId(String ownerId) {
        return friendGroupRepository.findByOwnerId(ownerId);
    }

    public List<String> findUserIdsByFriendGroups(List<String> groupIdList) {
        List<String> userIdList = new ArrayList<>();

        for (String groupId: groupIdList) {
            FriendGroup friendGroup = friendGroupRepository.findOne(groupId);
            userIdList.addAll(friendGroup.getMembers());
        }
        return userIdList;
    }

    private void save(FriendGroup friendGroup) {
        // Set to null not to erase another object with the same Id (new object)
        friendGroup.setId(null);
        LOGGER.info("Saving new friendGroup: " + friendGroup.toString());
        friendGroupRepository.save(friendGroup);
    }

    private void update(FriendGroup friendGroup) {
        LOGGER.info("Updating friendGroup: " + friendGroup.toString());
        friendGroupRepository.save(friendGroup);
    }

    private void delete(FriendGroup friendGroup) {
        LOGGER.info("Deleting friendGroup: " + friendGroup.toString());
        friendGroupRepository.delete(friendGroup);
    }

    public boolean addFilledFriendGroup(String name, String ownerId, List<String> userIdList) {

        // Initialisation du nouveau friendGroup
        FriendGroup friendGroup = new FriendGroup();

        if(name != null) {
            friendGroup.setName(name);
        }
        if(ownerId != null) {
            friendGroup.setOwnerId(ownerId);
        }
        if(userIdList != null && !userIdList.isEmpty()) {
            friendGroup.setMembers(userIdList);
        }

        // Recherche d'un friendGroup deja existant pour l'owner
        List<FriendGroup> friendGroupList = findFriendGroupByOwnerId(ownerId);
        if(!existingFriendGroup(friendGroupList, friendGroup)) {
            save(friendGroup);
            return true;
        }
        return false;
    }

    public boolean existingFriendGroup(List<FriendGroup> friendGroupList, FriendGroup friendGroup) {
        boolean existing = false;

        if(friendGroupList != null && !friendGroupList.isEmpty()) {
            for(FriendGroup fgIte: friendGroupList) {
                if(friendGroup.equals(fgIte)){
                    existing = true;
                }
            }
        }
        return existing;
    }

    public boolean addUserToFriendGroup(String friendGroupId, String userId) {

        FriendGroup friendGroup = findFriendGroupById(friendGroupId);

        if(friendGroup != null && !friendGroup.getMembers().contains(userId)) {
            friendGroup.getMembers().add(userId);
            update(friendGroup);
            return true;
        }
        return false;
    }

    public boolean removeUserToFriendGroup(String friendGroupId, String userId) {

        FriendGroup friendGroup = findFriendGroupById(friendGroupId);

        if (friendGroup != null && friendGroup.getMembers().contains(userId)) {
            friendGroup.getMembers().remove(userId);
            update(friendGroup);
            return true;
        }
        return false;
    }
}

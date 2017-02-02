package pfe.ece.LinkUS.Service;

import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Enum.Right;
import pfe.ece.LinkUS.Model.IdRight;
import pfe.ece.LinkUS.Model.Instant;
import pfe.ece.LinkUS.Model.Moment;

import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 07/01/2017.
 */
@Service
public class InstantService {

    Logger LOGGER = Logger.getLogger("LinkUS.Service.InstantService");


    public Instant createInstant(String name) {

        Instant instant = new Instant();
        instant.setName(name);
        instant.setPublishDate(new Date());
        instant.setTimeZone("Europe/Paris");
        return instant;
    }

    public boolean addInstantToMoment(Moment moment, Instant instant) {
        if(!moment.getInstantList().contains(instant)) {
            LOGGER.info("Adding instant: " + instant.getId() + " to moment: " + moment.getId());
            moment.getInstantList().add(instant);
            return true;
        }
        return false;
    }

    public Instant findInstantInMoment(Moment moment, String instantId) {
        for (Instant instant: moment.getInstantList()) {
            if(instant.getId().equals(instantId)){
                return instant;
            }
        }
        return null;
    }

    public boolean deleteInstantFromMoment(Moment moment, Instant instant) {
        Instant foundInstant = null;
        for(Instant instantItr: moment.getInstantList()) {
            if(instantItr.equals(instant)) {
                foundInstant = instantItr;
            }
        }

        if(foundInstant != null) {
            LOGGER.info("Removing instant: " + foundInstant.getId() + " from moment: " + moment.getId());
            moment.getInstantList().remove(foundInstant);
            return true;
        }
        return false;
    }

    public boolean addUserToAllInstantAllIdRight(Moment moment, String userId) {

        boolean bool = true;
        for (Instant instant: moment.getInstantList()) {
            if(!addUserToInstantAllIdRight(instant, userId)) {
                bool = false;
            }
        }
        return bool;
    }

    /**
     * On crée le IdRight dans tous les cas et si il existe on ne l'ajoute pas à instant
     *
     * @param instant
     * @param userId
     */
    public boolean addUserToInstantAllIdRight(Instant instant, String userId) {

        boolean bool = true;
        IdRightService idRightService = new IdRightService();
        for(Right right: Right.values()) {
            IdRight idRight = new IdRight(right.name());
            if(!idRightService.addUserToIdRight(idRight, userId) ||
                    !idRightService.addIdRightToInstant(instant, idRight)) {
                bool = false;
            }
        }
        return bool;
    }

    public boolean addUserToAllInstantIdRight(Moment moment, String right, String userId) {

        boolean bool = true;
        for (Instant instant: moment.getInstantList()) {
            if(!addUserToInstantIdRight(instant, right, userId)) {
                bool = false;
            }
        }
        return bool;
    }

    /**
     * On crée le IdRight dans tous les cas et si il existe on ne l'ajoute pas  à instant
     *
     * @param instant
     * @param right
     * @param userId
     */
    public boolean addUserToInstantIdRight(Instant instant, String right, String userId) {

        boolean bool = true;
        boolean exist = false;
        IdRightService idRightService = new IdRightService();

        // Si l'IdRight existe deja on ajoute le userId
        for(IdRight idRight: instant.getIdRight()) {
            if(idRight.getRight().equals(right)) {
                idRightService.addUserToIdRight(idRight, userId);
                return true;
            }
        }
        //Sinon on crée l'IdRight et on ajoute le userId
        IdRight idRight = new IdRight(right);
        if(!idRightService.addUserToIdRight(idRight, userId) ||
                !idRightService.addIdRightToInstant(instant, idRight)) {
            return false;
        }
        return true;
    }

    public void checkAllInstantDataRight(Moment moment, String userId) {
        Iterator<Instant> iter = moment.getInstantList().iterator();
        while (iter.hasNext()) {
            Instant instant = iter.next();
            if(!checkInstantDataRight(instant, userId)) {
                iter.remove();
            }
        }
    }

    public boolean checkInstantDataRight(Instant instant, String userId) {

        // Si le user ne possède pas le droit LECTURE on delete
        for (IdRight idRight: instant.getIdRight()) {
            if (idRight.getRight().equals(Right.LECTURE.name()) && !idRight.getUserIdList().contains(userId)) {
                return false;
            }
        }
        return true;
    }
}
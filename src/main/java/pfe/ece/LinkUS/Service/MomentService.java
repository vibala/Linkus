package pfe.ece.LinkUS.Service;

import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.Instant;
import pfe.ece.LinkUS.Model.Moment;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 06/01/2017.
 */
@Service
public class MomentService {

    Logger LOGGER = Logger.getLogger("LinkUS.Service.MomentService");

    public Moment newDefaultMoment() {
        LOGGER.info("Creating new default moment.");
        Moment moment = new Moment();
        moment.setId("0");
        moment.setName("Default");
        return moment;
    }
/*
    public String createMomentSaveToAlbum(Album album, String name) {

        if(album != null) {
            Moment moment = createMoment(name, null);

            // Temporaire
            Instant instant = new Instant();
            instant.setName("pipi");
            InstantService instantService = new InstantService();
            instantService.addInstantToMoment(moment, instant);
            //

            MomentService momentService = new MomentService();
            momentService.addMomentToAlbum(album, moment);

            return moment.getId();
        }
        return null;
    }*/

    public Moment createMoment(String name, ArrayList<Instant> instantList) {

        Moment moment = new Moment();

        moment.setName(name);
        if(instantList != null && !instantList.isEmpty()) {
            moment.setInstantList(instantList);
        }
        return moment;

    }

    public boolean addMomentToAlbum(Album album, Moment moment) {

        // On cherche si le moment existe, si non: on l'ajoute
        if(!album.getMoments().contains(moment)) {
            LOGGER.info("Adding moment: " + moment + " to album: " + album.getName());
            album.getMoments().add(moment);
            return true;
        }
        return false;
    }


/*    public String saveInstantToAlbumMoment(Album album, String momentId, Instant instant) {

        MomentService momentService = new MomentService();
        Moment moment = momentService.findMomentInAlbum(album, momentId);

        return saveInstantToAlbumMoment(moment, instant);
    }*/

/*    public String saveInstantToAlbumMoment(Moment moment, Instant instant) {

        InstantService instantService = new InstantService();

        instantService.addInstantToMoment(moment, instant);

        return instant.getId();
    }*/
    /**
     *
     * @param album
     * @param momentId
     * @return
     *      moment or null
     */
    public Moment findMomentInAlbum(Album album, String momentId) {
        for (Moment moment: album.getMoments()) {
            if(moment.getId().equals(momentId)){
                return moment;
            }
        }
        return null;
    }

    public boolean deleteMomentFromAlbum(Album album, Moment moment) {
        boolean bool = false;
        Moment foundMoment = null;
        for(Moment momentItr: album.getMoments()) {
            if(momentItr.equals(moment)) {
                foundMoment = momentItr;
            }
        }
        if(foundMoment != null) {
            album.getMoments().remove(foundMoment);
            bool = true;
        }

        // Si il n'y a plus de moments on en rajoute 1 par défaut
        if(album.getMoments().isEmpty()) {
            LOGGER.info("Removing moment: " + foundMoment.getId() + " from album: " + album.getName());
            album.getMoments().add(newDefaultMoment());
        }
        return bool;
    }

    public void checkAllMomentDataRight(Album album, String userId) {

        InstantService instantService = new InstantService();
        for(Moment moment: album.getMoments()) {

            // Check if instant are available for the user
            instantService.checkAllInstantDataRight(moment, userId);

            if (moment.getInstantList().isEmpty()) {
                deleteMomentFromAlbum(album, moment);
            }
        }
    }

    public void checkAllMomentDataNews(Album album, boolean news, String userId) {

        for(Moment moment: album.getMoments()) {
            if (moment.isNews() != news) {
                deleteMomentFromAlbum(album, moment);
            }
        }
    }

    public void setMainInstantUsingCotation(Moment moment) {

        Instant bestInstant = null;
        double bestCotation = -1;
        if(moment.getInstantList() != null) {
            for(Instant instant: moment.getInstantList()) {
                if(instant.getCotation() > bestCotation) {
                    bestCotation = instant.getCotation();
                    bestInstant = instant;
                }
            }
            moment.setMainInstant(bestInstant.getId());
        } else {
            moment.setMainInstant("");
        }
    }
}
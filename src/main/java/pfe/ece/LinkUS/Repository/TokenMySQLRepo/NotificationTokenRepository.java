package pfe.ece.LinkUS.Repository.TokenMySQLRepo;

import org.springframework.data.repository.CrudRepository;
import pfe.ece.LinkUS.Model.NotificationToken;

import javax.transaction.Transactional;

/**
 * Created by Vignesh on 12/15/2016.
 */
@Transactional
public interface NotificationTokenRepository extends CrudRepository<NotificationToken,Long> {
    public NotificationToken findByUsername(String username);
}

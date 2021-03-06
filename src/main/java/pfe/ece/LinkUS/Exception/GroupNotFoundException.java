package pfe.ece.LinkUS.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by DamnAug on 14/10/2016.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class GroupNotFoundException extends RuntimeException {

    public GroupNotFoundException(String userId) {
        super("Could not find user '" + userId + "'.");
    }
}

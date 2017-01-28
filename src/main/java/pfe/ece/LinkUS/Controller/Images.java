package pfe.ece.LinkUS.Controller;

import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by DamnAug on 05/01/2017.
 */
@Controller
public class Images {

    @RequestMapping(value = "/images", method = RequestMethod.GET)
    public void returnImage(HttpServletResponse response,
                            @RequestParam("name") String name,
                            @RequestParam("albumId") String albumId,
                            @RequestParam("userId") String userId) throws IOException {

        Path path = Paths.get("./images/" + userId + "/" + albumId + "/" + name);
        InputStream in = new FileInputStream(path.toString());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }
}

package tm.store.meninki.api.data.response;

import java.util.ArrayList;

import tm.store.meninki.api.data.MediaDto;
import tm.store.meninki.api.data.Rating;
import tm.store.meninki.api.data.UserProfile;

public class ResponseGetOnePost {
    private String id;
    private String name;
    private String description;

    private ArrayList<MediaDto> medias;
    private UserProfile user;
    private String productId;
    private Rating rating;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<MediaDto> getMedias() {
        return medias;
    }

    public UserProfile getUser() {
        return user;
    }

    public String getProductId() {
        return productId;
    }

    public Rating getRating() {
        return rating;
    }
}

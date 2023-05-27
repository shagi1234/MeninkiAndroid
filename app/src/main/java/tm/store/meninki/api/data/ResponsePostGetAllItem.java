package tm.store.meninki.api.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import tm.store.meninki.api.data.MediaDto;
import tm.store.meninki.api.data.Rating;
import tm.store.meninki.api.data.UserProfile;

public class ResponsePostGetAllItem {

    @SerializedName("productTitle")
    private String productTitle;

    @SerializedName("medias")
    private List<MediaDto> medias;

    @SerializedName("productMedia")
    private String productMedia;

    @SerializedName("productId")
    private String productId;

    @SerializedName("name")
    private String name;

    @SerializedName("rating")
    private tm.store.meninki.api.data.Rating rating;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private String id;

    @SerializedName("user")
    private UserProfile user;

    public String getProductTitle() {
        return productTitle;
    }

    public List<MediaDto> getMedias() {
        return medias;
    }

    public Object getProductMedia() {
        return productMedia;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Rating getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public UserProfile getUser() {
        return user;
    }
}
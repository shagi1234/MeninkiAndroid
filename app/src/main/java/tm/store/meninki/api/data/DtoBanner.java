package tm.store.meninki.api.data;

import com.google.gson.annotations.SerializedName;

public class DtoBanner {

    @SerializedName("bannerImage")
    private String bannerImage;

    @SerializedName("bannerType")
    private int bannerType;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public Object getBannerImage() {
        return bannerImage;
    }

    public void setBannerType(int bannerType) {
        this.bannerType = bannerType;
    }

    public int getBannerType() {
        return bannerType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
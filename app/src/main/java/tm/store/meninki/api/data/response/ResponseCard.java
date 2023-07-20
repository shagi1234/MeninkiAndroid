package tm.store.meninki.api.data.response;

import com.google.gson.annotations.SerializedName;

import tm.store.meninki.api.data.Rating;

public class ResponseCard {

    @SerializedName("images")
    private String[] images;

    @SerializedName("avatarId")
    private String avatarId;

    @SerializedName("price")
    private Double price;

    @SerializedName("name")
    private String name="";

    @SerializedName("discountPrice")
    private Double discountPrice;

    @SerializedName("count")
    private int count;

    @SerializedName("rating")
    private Rating rating;

    @SerializedName("id")
    private String id;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("avatarType")
    private int avatarType;

    @SerializedName("type")
    private int type;

    public String[] getImages() {
        return images;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public Double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public int getCount() {
        return count;
    }

    public Object getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getAvatarType() {
        return avatarType;
    }

    public int getType() {
        return type;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

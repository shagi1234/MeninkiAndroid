package tm.store.meninki.data;

import java.util.ArrayList;

import tm.store.meninki.api.data.SocialMediaDto;

public class ShopDTO {
    private String imgPath;
    private String id;
    private String slug;
    private String descriptionRu;
    private String decriptionEn;
    private int multiplier;
    private int shopType;
    private int moderationStatus;
    private String name;
    private String email;
    private String user;
    private String userId;
    private String phone;
    private String createDate;
    private boolean isSubscribed;
    private int subscriberCount;
    private int placeInRating;
    private int orderCount;
    private int visiterCount;
    private int totalProduct;
    private ArrayList<SocialMediaDto> socialMedias;
    private String endStoresPaymentPeriod;
    private String description;

    public String getImgPath() {
        return imgPath;
    }

    public String getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescriptionRu() {
        return descriptionRu;
    }

    public String getDecriptionEn() {
        return decriptionEn;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getShopType() {
        return shopType;
    }

    public int getModerationStatus() {
        return moderationStatus;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUser() {
        return user;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getCreateDate() {
        return createDate;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public int getSubscriberCount() {
        return subscriberCount;
    }

    public int getPlaceInRating() {
        return placeInRating;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public int getVisiterCount() {
        return visiterCount;
    }

    public int getTotalProduct() {
        return totalProduct;
    }

    public ArrayList<SocialMediaDto> getSocialMedias() {
        return socialMedias;
    }

    public String getEndStoresPaymentPeriod() {
        return endStoresPaymentPeriod;
    }

    public String getDescription() {
        return description;
    }
}
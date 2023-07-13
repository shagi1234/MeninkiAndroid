package tm.store.meninki.api.request;

import tm.store.meninki.api.data.SocialMediaDto;

public class RequestCreateShop {
    private String userName;
    private String name;
    private String description;
    private String url;
    private int multiplier;
    private String email;
    private String phoneNumber;
    private String endStoresPaymentPeriod;
    private int shopType;
    private String userId;
    private String[] categories;
    private SocialMediaDto socialMedias;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEndStoresPaymentPeriod() {
        return endStoresPaymentPeriod;
    }

    public void setEndStoresPaymentPeriod(String endStoresPaymentPeriod) {
        this.endStoresPaymentPeriod = endStoresPaymentPeriod;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public SocialMediaDto getSocialMedias() {
        return socialMedias;
    }

    public void setSocialMedias(SocialMediaDto socialMedias) {
        this.socialMedias = socialMedias;
    }
}

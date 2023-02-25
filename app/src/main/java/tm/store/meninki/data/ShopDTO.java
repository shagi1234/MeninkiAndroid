package tm.store.meninki.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import tm.store.meninki.api.data.ProductDto;

public class ShopDTO {
    private String image;
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
    private String phoneNumber;
    private String createDate;
    private String endStoresPaymentPeriod;
    @SerializedName("descriptionTm")
    private String desc;
    private ArrayList<ProductDto> product;
    private ArrayList<CategoryDto> categories;

    public ShopDTO(String image, String id, String slug, String descriptionRu, String decriptionEn, int multiplier, int shopType, int moderationStatus, String name, String email, String user, String userId, String phoneNumber, String createDate, String endStoresPaymentPeriod, String desc, ArrayList<ProductDto> product, ArrayList<CategoryDto> categories) {
        this.image = image;
        this.id = id;
        this.slug = slug;
        this.descriptionRu = descriptionRu;
        this.decriptionEn = decriptionEn;
        this.multiplier = multiplier;
        this.shopType = shopType;
        this.moderationStatus = moderationStatus;
        this.name = name;
        this.email = email;
        this.user = user;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.createDate = createDate;
        this.endStoresPaymentPeriod = endStoresPaymentPeriod;
        this.desc = desc;
        this.product = product;
        this.categories = categories;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescriptionRu() {
        return descriptionRu;
    }

    public void setDescriptionRu(String descriptionRu) {
        this.descriptionRu = descriptionRu;
    }

    public String getDecriptionEn() {
        return decriptionEn;
    }

    public void setDecriptionEn(String decriptionEn) {
        this.decriptionEn = decriptionEn;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public int getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(int moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getEndStoresPaymentPeriod() {
        return endStoresPaymentPeriod;
    }

    public void setEndStoresPaymentPeriod(String endStoresPaymentPeriod) {
        this.endStoresPaymentPeriod = endStoresPaymentPeriod;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<ProductDto> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<ProductDto> product) {
        this.product = product;
    }

    public ArrayList<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryDto> categories) {
        this.categories = categories;
    }
}

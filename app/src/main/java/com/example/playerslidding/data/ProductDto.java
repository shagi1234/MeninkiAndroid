package com.example.playerslidding.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductDto {
    private String orientation;
    private String imagePath;
    @SerializedName("name")
    private String title;
    private String shopId;
    private String id;
    private String slug;
    private String price;
    private String description;
    @SerializedName("priceBonus")
    private String oldPrice;
    @SerializedName("discountPrice")
    private String sale;
    private String createDate;
    @SerializedName("numberView")
    private int count;
    private int moderationStatus;
    private boolean isDeleted;
    private ArrayList<CategoryDto> categories;
    private ShopDTO shop;

/*          "posts": null,
            "rating": null,
            "images": null,*/

    public ProductDto(String orientation, String imagePath, String title, String shopId, String id, String slug, String price, String description, String oldPrice, String sale, String createDate, int count, int moderationStatus, boolean isDeleted, ArrayList<CategoryDto> categories, ShopDTO shop) {
        this.orientation = orientation;
        this.imagePath = imagePath;
        this.title = title;
        this.shopId = shopId;
        this.id = id;
        this.slug = slug;
        this.price = price;
        this.description = description;
        this.oldPrice = oldPrice;
        this.sale = sale;
        this.createDate = createDate;
        this.count = count;
        this.moderationStatus = moderationStatus;
        this.isDeleted = isDeleted;
        this.categories = categories;
        this.shop = shop;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(int moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public ArrayList<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryDto> categories) {
        this.categories = categories;
    }

    public ShopDTO getShop() {
        return shop;
    }

    public void setShop(ShopDTO shop) {
        this.shop = shop;
    }
}

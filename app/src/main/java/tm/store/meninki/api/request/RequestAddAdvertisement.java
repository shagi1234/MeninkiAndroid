package tm.store.meninki.api.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestAddAdvertisement {
    String Name;
    String description;
    @SerializedName("categoryId")
    String categoryId;
    String userId;
    int price;
    String phoneNumber;
    int region;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getRegion() {
        return region;
    }

    public int getPrice() {
        return price;
    }

    public String getCategoryId() {
        return categoryId;

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;

    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}

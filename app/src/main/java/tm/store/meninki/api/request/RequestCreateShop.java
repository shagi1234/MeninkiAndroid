package tm.store.meninki.api.request;

public class RequestCreateShop {
    private String name;
    private String descriptionTm;
    private String email;
    private String phoneNumber;
    private String userId;
    private String[] categories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionTm() {
        return descriptionTm;
    }

    public void setDescriptionTm(String descriptionTm) {
        this.descriptionTm = descriptionTm;
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
}

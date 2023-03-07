package tm.store.meninki.api.data;

public class UserDto {
    private String id;
    private String imagePath;
    private String name;
    private String phoneNumber;
    private String email;
    private boolean isSubscribed;
    private int subscriberCount;
    private int subscriptionCount;
    private int boughtProducts;
    private int favoriteCount;
    private int shopCount;

    public String getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public int getSubscriberCount() {
        return subscriberCount;
    }

    public int getSubscriptionCount() {
        return subscriptionCount;
    }

    public int getBoughtProducts() {
        return boughtProducts;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public int getShopCount() {
        return shopCount;
    }
}

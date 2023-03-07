package tm.store.meninki.api.data;

public class UserProfile {
    private String id;
    private String imgPath;
    private String name;
    private String phoneNumber;
    private String email;
    private boolean isSubscribed;
    private int subscriberCount;
    private int subscriptionCount;
    private int productCount;
    private int placeInRating; // ine shop-da bar, user profile-da yok
    private String description;  //dine shop-da bar, user profile-da yok
    private int boughtProducts; // dine oz profilymda bar
    private int favoriteCount; // dine oz profilymda bar
    private int shopCount;  //dine oz profilymda bar

    private int orderCount; // dine oz shopymda bar
    private int visiterCount; // dine oz shopymda bar

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public int getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(int subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public int getSubscriptionCount() {
        return subscriptionCount;
    }

    public void setSubscriptionCount(int subscriptionCount) {
        this.subscriptionCount = subscriptionCount;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getPlaceInRating() {
        return placeInRating;
    }

    public void setPlaceInRating(int placeInRating) {
        this.placeInRating = placeInRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(int boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getVisiterCount() {
        return visiterCount;
    }

    public void setVisiterCount(int visiterCount) {
        this.visiterCount = visiterCount;
    }
}

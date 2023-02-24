package tm.store.meninki.data;

public class StoreDTO {
    private String storeName;
    private String imagePath;
    private String posterImagePath;

    public StoreDTO(String storeName, String imagePath, String posterImagePath) {
        this.storeName = storeName;
        this.imagePath = imagePath;
        this.posterImagePath = posterImagePath;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPosterImagePath() {
        return posterImagePath;
    }

    public void setPosterImagePath(String posterImagePath) {
        this.posterImagePath = posterImagePath;
    }
}

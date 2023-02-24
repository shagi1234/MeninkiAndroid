package tm.store.meninki.data;

public class CharImageDto {
    String img;
    boolean isActive;

    public CharImageDto(String img, boolean isActive) {
        this.img = img;
        this.isActive = isActive;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

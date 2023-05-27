package tm.store.meninki.api.request;

import java.io.File;

public class RequestUploadImage {
    private String objectId;
    private boolean isAvatar;
    private int imageType;
    private int width;
    private int height;
    private File data;
    private File thumb;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public boolean isAvatar() {
        return isAvatar;
    }

    public void setAvatar(boolean avatar) {
        isAvatar = avatar;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public File getData() {
        return data;
    }

    public void setData(File data) {
        this.data = data;
    }

    public File getThumb() {
        return thumb;
    }

    public void setThumb(File thumb) {
        this.thumb = thumb;
    }
}

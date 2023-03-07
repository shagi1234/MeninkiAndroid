package tm.store.meninki.api.request;

import java.io.File;

public class RequestUploadImage {
    private String objectId;
    private boolean isAvatar;
    private int imageType;
    private int width;
    private int height;
    private String filename;
    private File data;

    public RequestUploadImage(String objectId, boolean isAvatar, int imageType, int width, int height, String filename, File data) {
        this.objectId = objectId;
        this.isAvatar = isAvatar;
        this.imageType = imageType;
        this.width = width;
        this.height = height;
        this.filename = filename;
        this.data = data;
    }

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public File getData() {
        return data;
    }

    public void setData(File data) {
        this.data = data;
    }
}

package tm.store.meninki.data;

import com.google.gson.annotations.SerializedName;

public class MediaLocal {
    private int id;
    private String uuid;
    @SerializedName("image_path")
    private String path;
    private int type;

    public MediaLocal(int id, String path, int type) {
        this.id = id;
        this.path = path;
        this.type = type;
    }
    public MediaLocal(String uuid, String path) {
        this.uuid = uuid;
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

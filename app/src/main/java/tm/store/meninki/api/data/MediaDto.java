package tm.store.meninki.api.data;

public class MediaDto {
    private String id;
    private String path;
    private int orientationType;
    private int mediaType;

    public int getMediaType() {
        return mediaType;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public int getOrientationType() {
        return orientationType;
    }
}

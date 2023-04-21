/*
 * Copyright (c) 2023.  Daniyarow Shahruh
 */

package tm.store.meninki.data;

public class VideoDto {
    private String path;
    private String title;
    private String username;
    private String duration;
    private boolean isLiked;

    public VideoDto(String path, String title, String username, String duration) {
        this.path = path;
        this.title = title;
        this.username = username;
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}

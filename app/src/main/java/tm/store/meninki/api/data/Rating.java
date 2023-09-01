package tm.store.meninki.api.data;

import java.util.HashMap;

public class Rating {
    HashMap<String, Boolean> userRating;
    long total;

    public HashMap<String, Boolean> getUserRating() {
        return userRating;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setUserRating(HashMap<String, Boolean> userRating) {
        this.userRating = userRating;
    }
}

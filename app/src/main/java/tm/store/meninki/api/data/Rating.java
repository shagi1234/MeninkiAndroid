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
}

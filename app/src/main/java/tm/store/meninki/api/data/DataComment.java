package tm.store.meninki.api.data;

import java.util.ArrayList;

public class DataComment {
    private int total;
    private ArrayList<DtoComment> comments;

    public ArrayList<DtoComment> getComments() {
        return comments;
    }

    public int getTotal() {
        return total;
    }

    public void setComments(ArrayList<DtoComment> comments) {
        this.comments = comments;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

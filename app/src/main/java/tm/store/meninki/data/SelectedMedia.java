package tm.store.meninki.data;

import java.util.ArrayList;

public class SelectedMedia {
    private static ArrayList<MediaLocal> arrayList;

    public static ArrayList<MediaLocal> getArrayList() {
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        return arrayList;
    }
}

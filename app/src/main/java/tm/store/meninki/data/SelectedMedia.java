package tm.store.meninki.data;

import java.util.ArrayList;

public class SelectedMedia {
    private static ArrayList<MediaLocal> productImageList;
    private static ArrayList<MediaLocal> optionImageList;

    public static ArrayList<MediaLocal> getOptionImageList() {
        if (optionImageList == null) {
            optionImageList = new ArrayList<>();
        }
        return optionImageList;
    }
}

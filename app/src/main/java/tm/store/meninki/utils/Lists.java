package tm.store.meninki.utils;

import tm.store.meninki.R;
import tm.store.meninki.data.CharactersDto;
import tm.store.meninki.data.ProductImageDto;

import java.util.ArrayList;

public class Lists {
    public static ArrayList<ProductImageDto> s;
    public static ArrayList<String> picks;
    public static ArrayList<String> colors;
    public static ArrayList<CharactersDto> personalCharacters;

    public static ArrayList<ProductImageDto> getS() {
        if (s == null) {
            s = new ArrayList<>();
        }
        return s;
    }

    public static ArrayList<String> getPicks() {
        if (picks == null) {
            picks = new ArrayList<>();
        }
        return picks;
    }

    public static ArrayList<String> getColors() {
        if (colors == null) {
           colors=new ArrayList<>();
        }
        return colors;
    }

    public static ArrayList<CharactersDto> getPersonalCharacters() {
        if (personalCharacters == null) {
            personalCharacters = new ArrayList<>();
        }
        return personalCharacters;
    }
}

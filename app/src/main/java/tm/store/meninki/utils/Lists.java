package tm.store.meninki.utils;

import tm.store.meninki.data.CharactersDto;
import tm.store.meninki.data.ProductImageDto;

import java.util.ArrayList;

public class Lists {
    public static CharactersDto personalCharacters;

    public static CharactersDto getPersonalCharacters() {
        if (personalCharacters == null) {
            personalCharacters = new CharactersDto();
        }
        return personalCharacters;
    }

    public static void setPersonalCharacters(CharactersDto personalCharacters) {
        Lists.personalCharacters = personalCharacters;
    }
}

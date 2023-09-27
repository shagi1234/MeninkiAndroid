package tm.store.meninki.firebase;

import android.content.Context;

public class FirebaseAuthPreferences {
    private static FirebaseAuthPreferences firebaseAuthPreferences;
    Context context;

    public FirebaseAuthPreferences(Context context) {
        this.context = context;
    }

    public static FirebaseAuthPreferences newInstance(Context context) {
        if (firebaseAuthPreferences == null) {
            firebaseAuthPreferences = new FirebaseAuthPreferences(context);
        }
        return firebaseAuthPreferences;
    }

    public void saveFirebaseToken(String s) {

    }
}

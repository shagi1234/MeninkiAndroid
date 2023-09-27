package tm.store.meninki.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;

public abstract class GetBitmapToUrl extends AsyncTask<String, Void, Bitmap> {

    public abstract void onSuccess(Bitmap bm);

    public abstract void error();

    private Context context;

    public GetBitmapToUrl(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(final Bitmap bm) {
        if (bm != null) {
            onSuccess(bm);
        } else {
            error();
        }
    }


    @Override
    protected Bitmap doInBackground(final String... args) {
        try {
            return Glide
                    .with(context)
                    .asBitmap()
                    .load(args[0])
                    .submit()
                    .get();
        } catch (Exception e) {
            Log.e("ErrorGetBitmap: ", e.getMessage());
            return null;
        }
    }


}

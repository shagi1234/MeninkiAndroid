package tm.store.meninki.utils;

import static android.content.Context.VIBRATOR_SERVICE;

import static tm.store.meninki.api.Network.BASE_URL;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import tm.store.meninki.R;
import tm.store.meninki.api.ApiClient;
import tm.store.meninki.api.services.ServiceCategory;
import tm.store.meninki.api.services.ServiceHome;
import tm.store.meninki.api.services.ServiceLogin;

public class StaticMethods {
    public static int statusBarHeight;
    public static int navigationBarHeight;
    private static String TAG = "Meninki";

    public static void logWrite(String msg) {
        Log.e(TAG, "logWrite: " + msg);
    }

    public static void logWrite(boolean msg) {
        Log.e(TAG, "logWrite: " + msg);
    }

    public static void logWrite(int msg) {
        Log.e(TAG, "logWrite: " + msg);
    }

    public static void setVibrate(Context context, long sec) {
        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(sec, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(sec);
        }
    }

    public static void cloudAnimStart(AppCompatImageView cloud) {
        Drawable drawable = cloud.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    public static ServiceLogin getApiLogin() {
        return (ServiceLogin) ApiClient.createRequest(ServiceLogin.class);
    }

    public static ServiceHome  getApiHome() {
        return (ServiceHome) ApiClient.createRequest(ServiceHome.class);
    }

    public static ServiceCategory getApiCategory() {
        return (ServiceCategory) ApiClient.createRequest(ServiceCategory.class);
    }

    public static String getTimeOrDayOrDate (Date date) {
        String defaultTimezone = TimeZone.getDefault().getID();
        DateTimeZone timeZone = DateTimeZone.forID(defaultTimezone);
        DateTime dateTimeUtc = DateTime.now().withZone(DateTimeZone.UTC);
        DateTime now = new DateTime(dateTimeUtc, timeZone);

        try {
            long difference = 0;
            if (date != null) {
                difference = Math.abs(now.toDate().getTime() - date.getTime());
            }
            long differenceMinutes = difference / (60 * 1000);
            long differenceHours = differenceMinutes / 60;
            long differenceDays = differenceHours / 24;
            if (differenceDays >= 7) {
                return convertDate(date.getTime() + "", "dd.MM");

            } else {
                if (differenceHours >= 24) {

                    LocalDate localDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    Locale locale = Locale.getDefault();  // Locale specifies the human language to use in determining day-of-week name (Tuesday in English versus Mardi in French).
                    DateTimeFormatter formatterOutput = DateTimeFormat.forPattern("E").withLocale(locale);
                    String output = formatterOutput.print(localDate); // 'E' is code for abbreviation of day-of-week name. See Joda-Time doc.

                    return output;
                } else {
                    return convertDate(date.getTime() + "", "kk:mm");
                }
            }


        } catch (Exception e) {
            return "";
        }
    }

    public static String getTime(Activity activity, Date newDate, Date oldDate) {
        try {
            long difference = 0;
            if (oldDate != null && newDate != null) {
                difference = Math.abs(oldDate.getTime() - newDate.getTime());
            }
            long differenceMinutes = difference / (60 * 1000);
            long differenceHours = differenceMinutes / 60;
            long differenceDays = differenceHours / 24;


            if (differenceMinutes <= 1) {
                return activity.getResources().getString(R.string.just_now);

            } else if (differenceDays <= 1) {

                SimpleDateFormat outputDf = new SimpleDateFormat("kk:mm");
                SimpleDateFormat inputDf = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss");
                inputDf.setTimeZone(TimeZone.getTimeZone("GMT"));
                return outputDf.format(oldDate);


            } else {
                SimpleDateFormat outputDf = new SimpleDateFormat("kk:mm' | 'dd.MM.yyyy");
                SimpleDateFormat inputDf = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss");
                inputDf.setTimeZone(TimeZone.getTimeZone("GMT"));

                return outputDf.format(oldDate);
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static void setBackgroundDrawable(Context context, View view, int color, int borderColor, int corner, boolean isOval, int border) {
        if (context == null) return;

        GradientDrawable shape = new GradientDrawable();
        if (isOval) {
            shape.setShape(GradientDrawable.OVAL);
        } else {
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(dpToPx(context, corner));
        }
        if (color != 0) {
            shape.setColor(context.getResources().getColor(color));
        } else {
            shape.setColor(context.getResources().getColor(R.color.color_transparent));
        }

        if (borderColor != 0) {
            shape.setStroke(dpToPx(border, context), context.getResources().getColor(borderColor));
        }
        view.setBackground(shape);

    }

    public static void setBackgroundDrawable(Context context, View view, String color, int borderColor, int corner, boolean isOval, int border) {
        if (context == null) return;

        GradientDrawable shape = new GradientDrawable();
        if (isOval) {
            shape.setShape(GradientDrawable.OVAL);
        } else {
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(dpToPx(context, corner));
        }
        if (!Objects.equals(color, "")) {
            shape.setColor(Color.parseColor(color));
        } else {
            shape.setColor(context.getResources().getColor(R.color.color_transparent));
        }

        if (borderColor != 0) {
            shape.setStroke(dpToPx(border, context), context.getResources().getColor(borderColor));
        }
        view.setBackground(shape);

    }

    public static void setBackgroundDrawable(Context context, View view, int color, int borderColor, int cornerLeftTop, int cornerRightTop, int cornerLeftBottom, int cornerRightBottom, boolean isOval, int border) {
        if (context == null) return;

        GradientDrawable shape = new GradientDrawable();
        if (isOval) {
            shape.setShape(GradientDrawable.OVAL);
        } else {
            shape.setShape(GradientDrawable.RECTANGLE);

            shape.setCornerRadii(new float[]{cornerLeftTop, cornerRightTop, 0, 0, 0, 0, cornerRightBottom, cornerLeftBottom});
        }
        if (color != 0) {
            shape.setColor(context.getResources().getColor(color));
        } else {
            shape.setColor(context.getResources().getColor(R.color.color_transparent));
        }

        if (borderColor != 0) {
            shape.setStroke(dpToPx(border, context), context.getResources().getColor(borderColor));
        }
        view.setBackground(shape);

    }

    public static void setMessageBackground(Context context, View view, int color, float[] corner) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(corner);
        shape.setColor(context.getResources().getColor(color));
        view.setBackground(shape);
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);


        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getEthernetMacAddress() {
        String macAddress = "Not able to read";
        try {
            List<NetworkInterface> allNetworkInterfaces = Collections.list(NetworkInterface
                    .getNetworkInterfaces());
            for (NetworkInterface nif : allNetworkInterfaces) {
                if (!nif.getName().equalsIgnoreCase("eth0"))
                    continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return macAddress;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                macAddress = res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return macAddress;
    }

    public static File screenshot(View view, Context context) {
        try {
            // Initialising the directory of storage
            String dirpath = context.getExternalFilesDir("Post_images") + "/";
            File file = new File(dirpath);

            if (!file.exists()) {
                boolean mkdir = file.mkdir();
            }

            // File name
            String path = dirpath + System.currentTimeMillis() + ".jpeg";

            Bitmap bitmap = getBitmapFromView(view);
            if (bitmap == null) return null;

            File image = new File(path);
            FileOutputStream outputStream = new FileOutputStream(image);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            outputStream.flush();
            outputStream.close();
            return image;

        } catch (IOException io) {
            Log.e("1234", "screenshot: " + io.getMessage());
            io.printStackTrace();
        }
        return null;
    }

    public static File getImgFileFromBitmap(Bitmap bitmap, Context context) {
        try {
            // Initialising the directory of storage
            String dirpath = context.getExternalFilesDir("Post_images") + "/";
            File file = new File(dirpath);

            if (!file.exists()) {
                boolean mkdir = file.mkdir();
            }

            // File name
            String path = dirpath + System.currentTimeMillis() + ".jpeg";

            if (bitmap == null) return null;

            File image = new File(path);
            FileOutputStream outputStream = new FileOutputStream(image);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            outputStream.flush();
            outputStream.close();
            return image;

        } catch (IOException io) {
            io.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        if (view.getMeasuredWidth() <= 0 || view.getMeasuredHeight() <= 0) return null;

        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public static void showNoConnection(View view, boolean isError) {
        if (view != null) {
            if (isError) {
                view.setVisibility(View.VISIBLE);
            } else view.setVisibility(View.GONE);
        }
    }

    public static int dpToPx(int dp, Context context) {
        if (context == null) return 0;

        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    public static int dpToPx(double dp, Context context) {
        if (context == null) return 0;

        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    public static int pxToDp(int px, final Context context) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static float dpToPx(Context context, int dp) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float dpToPxFloat(Context context, float dp) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float pxToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void setPaddingWithHandler(ViewGroup v, int l, int t, int r, int b) {

        try {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {

                v.setPadding(l, t, r, b);
                v.requestLayout();
            }
        } catch (Exception e) {
            Log.d("error", "setMargins: " + e.getMessage());
        }
    }

    public static void setMargins(ViewGroup v, int l, int t, int r, int b) {
        try {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                p.setMargins(l, t, r, b);
                v.requestLayout();
            } else {
                Log.d("SetMargins", "setMargins: ");
            }
        } catch (Exception e) {
            Log.d("SetMargins", "setMargins: " + e.getMessage());
        }
    }

    public static void setImageOrText(Context context, String username, String avatar, ImageView imageView, TextView tv, boolean clickable, Activity activity) {

        if (context == null || activity == null) return;

        if (avatar == null || avatar.equals("")) {
            imageView.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
            tv.setBackground(createGradientDrawable(context));

            try {
                tv.setText(getShortUserName(username));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        Glide.with(context)
                .load(BASE_URL + avatar)
                .error(createGradientDrawable(context))
                .placeholder(createGradientDrawable(context))
//                .addListener(new RequestListener<>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        imageView.setVisibility(View.INVISIBLE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        imageView.setVisibility(View.VISIBLE);
//                        tv.setVisibility(View.GONE);
//                        return false;
//                    }
//                })
                .into(imageView);

        imageView.setVisibility(View.VISIBLE);
        tv.setVisibility(View.INVISIBLE);

//        if (clickable) {
          /*  if (clickLay != null) {
                clickLay.setOnClickListener(v -> {
                    isOpenSearch = false;

                    if (PLayers.lastReelsPlayer != null) {
                        PLayers.lastReelsPlayer.pause();
                    }

                    if (isKeyboardOpen) {
                        hideSoftKeyboard(activity);
                        isKeyboardOpen = false;

                    } else {

                        clickLay.setEnabled(false);

                        handler.postDelayed(() -> clickLay.setEnabled(true), 200);

                        if (posterDTO.getType() == 1) {
                            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(posterDTO.getUUID(), TYPE_COMMUNITY_PROFILE));
                        } else {
                            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(posterDTO.getUUID(), TYPE_FRIEND_PROFILE));
                        }

                        if (ConstBottomSheets.bottomSheetProfile != null && ConstBottomSheets.bottomSheetProfile.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            ConstBottomSheets.bottomSheetProfile.setState(STATE_HIDDEN);
                        }
                    }
                });
            }*/

          /*  imageView.setOnClickListener(v -> {
                isOpenSearch = false;

                if (PLayers.lastReelsPlayer != null) {
                    PLayers.lastReelsPlayer.pause();
                }

                if (isKeyboardOpen) {
                    hideSoftKeyboard(activity);
                    isKeyboardOpen = false;
                } else {
                    imageView.setEnabled(false);
                    handler.postDelayed(() -> imageView.setEnabled(true), 200);

                    if (posterDTO.getType() == 1) {
                        addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(posterDTO.getUUID(), TYPE_COMMUNITY_PROFILE));
                    } else {
                        addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(posterDTO.getUUID(), TYPE_FRIEND_PROFILE));

                    }
                    if (ConstBottomSheets.bottomSheetProfile != null && ConstBottomSheets.bottomSheetProfile.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        ConstBottomSheets.bottomSheetProfile.setState(STATE_HIDDEN);
                    }
                }

            });*/

//            tv.setOnClickListener(v -> {
//                isOpenSearch = false;
//
//                if (PLayers.lastReelsPlayer != null) {
//                    PLayers.lastReelsPlayer.pause();
//                }

//                if (isKeyboardOpen) {
//                    hideSoftKeyboard(activity);
//                    isKeyboardOpen = false;
//                } else {
//                    tv.setEnabled(false);
//                    handler.postDelayed(() -> tv.setEnabled(true), 200);
//
//                    if (posterDTO.getType() == 1) {
//                        addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(posterDTO.getUUID(), TYPE_COMMUNITY_PROFILE));
//                    } else {
//                        addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(posterDTO.getUUID(), TYPE_FRIEND_PROFILE));
//                    }
//                    if (ConstBottomSheets.bottomSheetProfile != null && ConstBottomSheets.bottomSheetProfile.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//                        ConstBottomSheets.bottomSheetProfile.setState(STATE_HIDDEN);
//                    }
//                }

//            });
//        }
    }

    private static Drawable createGradientDrawable(Context context) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(4f);
        shape.setColor(context.getResources().getColor(R.color.neutral_dark));
        return shape;
    }


    public static void setMargins(View v, int l, int t, int r, int b) {
        try {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                p.setMargins(l, t, r, b);
                v.requestLayout();
            }
        } catch (Exception e) {
            Log.d("error", "setMargins: " + e.getMessage());
        }

    }

    public static void setPadding(View v, int l, int t, int r, int b) {
        try {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();

                v.setPadding(l, t, r, b);
                //p.setMargins(l, t, r, b);
                v.requestLayout();
            }
        } catch (Exception e) {
            Log.d("error", "setMargins: " + e.getMessage());
        }

    }

    public static int getWindowHeight(Activity activity) {
        if (activity == null) return 0;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getWindowWidth(Activity activity) {
        if (activity == null) return 0;
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

/*    public static void showToast(Activity activity, int str) {

        if (activity == null) return;

        Context applicationContext = activity.getApplicationContext();
        if (generalToast != null) {
            generalToast.cancel();
        }

        Toast toast;

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) activity.findViewById(R.id.toast_layout_root));
        setBackgroundDrawable(activity, layout.findViewById(R.id.background), R.color.colorRedCancel, 0, 5, false, 0);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(activity.getResources().getString(str));
        toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        generalToast = toast;

    }

    public static void showToastPrimary(Activity activity, int str) {

        if (activity == null) return;

        Context applicationContext = activity.getApplicationContext();
        if (generalToast != null) {
            generalToast.cancel();
        }

        Toast toast;

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout_primary, (ViewGroup) activity.findViewById(R.id.toast_layout_root));
        setBackgroundDrawable(activity, layout.findViewById(R.id.background), R.color.colorPrimary, 0, 5, false, 0);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(activity.getResources().getString(str));
        toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        generalToast = toast;

    }

    public static void showToastPrimaryBottom(Activity activity, String str) {

        if (activity == null) return;

        Context applicationContext = activity.getApplicationContext();
        if (generalToast != null) {
            generalToast.cancel();
        }

        Toast toast;

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout_primary, (ViewGroup) activity.findViewById(R.id.toast_layout_root));
        setBackgroundDrawable(activity, layout.findViewById(R.id.background), R.color.colorPrimary, 0, 5, false, 0);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(str);
        toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

        generalToast = toast;

    }*/

    public static void transparentStatusAndNavigation(Activity activity) {

        Window window = activity.getWindow();


        int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        visibility = visibility | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        window.getDecorView().setSystemUiVisibility(visibility);
        int windowManager = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        windowManager = windowManager | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        setWindowFlag(activity, windowManager, false);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        setStatusAndNavBarIconColor(activity, true);


    }

    private static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void setStatusAndNavBarIconColor(Activity activity, boolean setLight) {
        View view = activity.getWindow().getDecorView();
        if (setLight) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                view.setSystemUiVisibility(view.getSystemUiVisibility() | ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                view.setSystemUiVisibility(view.getSystemUiVisibility() | ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        }
    }

    public static void setLightStatusBar(Activity activity) {
        //        status bar texty gara renk etyar

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
//            activity.getWindow().setStatusBarColor(Color.GRAY); // optional
        }
    }

    public static void setClearLightStatusBar(Activity activity) {
//        status bar texty ak renk etyar

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
//            activity.getWindow().setStatusBarColor(Color.GREEN); // optional
        }
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(callingActivity);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

    public static void initSystemUIViewListeners(ViewGroup rootContainer) {

        rootContainer.setOnApplyWindowInsetsListener((v, windowInsets) -> {
            WindowInsets defaultInsets = v.onApplyWindowInsets(windowInsets);
            statusBarHeight = defaultInsets.getSystemWindowInsetTop();
            navigationBarHeight = defaultInsets.getSystemWindowInsetBottom();

            return defaultInsets.replaceSystemWindowInsets(
                    0, 0, 0, 0);
        });
    }

    public static void setNavBarIconsBlack(Activity activity) {
        if (activity == null ) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final int lFlags = activity.getWindow().getDecorView().getSystemUiVisibility();
            activity.getWindow().getDecorView().setSystemUiVisibility(lFlags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else {
            activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, R.color.black));
        }
    }

    public static void setNavBarIconsWhite(Activity activity) {
        if (activity == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Window window = activity.getWindow();
            final int lFlags = activity.getWindow().getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else {
            activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, R.color.color_transparent));
        }

    }

    public static void setNavBarIconsColor(Activity activity, Context context) {
        if (activity == null || context == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Window window = activity.getWindow();
            final int lFlags = activity.getWindow().getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else {
            activity.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.color_transparent));
        }

    }

    public static void clearLightStatusBar(Activity activity) {
//        status bar texty ak renk etyar

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
//            flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
//            activity.getWindow().getDecorView().setSystemUiVisibility(flags);

            final int lFlags = activity.getWindow().getDecorView().getSystemUiVisibility();
            activity.getWindow().getDecorView().setSystemUiVisibility(lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        }
    }

    public static void clickWithAnim(View v, @NonNull MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v,
                        "scaleX", 0.95f);
                ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                        "scaleY", 0.95f);
                scaleDownX.setDuration(100);
                scaleDownY.setDuration(100);
                AnimatorSet scaleDown = new AnimatorSet();
                scaleDown.play(scaleDownX).with(scaleDownY);
                scaleDown.start();
                break;

            case MotionEvent.ACTION_UP:
                ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(
                        v, "scaleX", 1f);
                ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(
                        v, "scaleY", 1f);
                scaleDownX2.setDuration(100);
                scaleDownY2.setDuration(100);
                AnimatorSet scaleDown2 = new AnimatorSet();
                scaleDown2.play(scaleDownX2).with(scaleDownY2);
                scaleDown2.start();
                break;
        }
    }

    public static void setMarginWithAnim(ViewGroup v, float fromMargin, float toMargin) {
        ViewGroup.MarginLayoutParams gd = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofFloat(fromMargin, toMargin);
        animator.setDuration(150)
                .addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    gd.setMargins(0, 0, 0, (int) value);
                    v.requestLayout();
                });
        animator.start();
    }

    public static void setMarginWithAnim(View v, float fromMargin, float toMargin) {
        ViewGroup.MarginLayoutParams gd = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofFloat(fromMargin, toMargin);
        animator.setDuration(150)
                .addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    gd.setMargins(0, 0, 0, (int) value);
                    v.requestLayout();
                });
        animator.start();
    }

    public static boolean hasNavBar(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            return resources.getBoolean(id);
        } else {    // Check for keys
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !hasMenuKey && !hasBackKey;
        }
    }

    public static int getHeightNavIndicator(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        } else return 0;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity == null) return;
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

            if (inputMethodManager.isAcceptingText()) {
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(),
                        0
                );
            }
        } catch (Exception e) {
            Log.d("error", "hideSoftKeyboard: " + e.getMessage());
        }

    }

    public static String getShortUserName(String user_name) {

        if (user_name == null || user_name.equals("")) return "";

        String avatar_character = user_name.trim();

        String[] segments = avatar_character.split(" ", -1);

        if (segments.length > 1) {
            if (segments[0].length() > 0 && segments[1].length() > 0)
                avatar_character = segments[0].charAt(0) + "" + segments[1].charAt(0);
        } else {
            avatar_character = avatar_character.charAt(0) + "";
        }
        return avatar_character.toUpperCase();
    }

    public static void animateViewVisibility(View view, View nextView) {
        view.animate()
                .alpha(0.0f)
                .setDuration(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.INVISIBLE);
                        nextView.animate()
                                .alpha(1.0f)
                                .setDuration(100)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        nextView.setVisibility(View.VISIBLE);

                                    }
                                });


                    }
                });
    }


    public static void vibrator(int vibrMilliseconds, Context context) {
        Vibrator vibrator;
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrMilliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(vibrMilliseconds);
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static void expand(final View v, int duration, int height) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.setVisibility(View.VISIBLE);
        v.getLayoutParams().height = 1;

        Animation a = new Animation() {


            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {


                if (interpolatedTime == 1) {
                    v.getLayoutParams().height = dpToPx(height, ((View) v.getParent()).getContext());
                } else {
                    v.getLayoutParams().height = (int) (targetHeight * interpolatedTime);
                }


                v.requestLayout();


            }


        };

        a.setDuration(duration);
        v.startAnimation(a);
    }

    public static void slideView(View view,
                                 int currentHeight,
                                 int newHeight) {

        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(currentHeight, newHeight)
                .setDuration(500);

        /* We use an update listener which listens to each tick
         * and manually updates the height of the view  */

        slideAnimator.addUpdateListener(animation1 -> {
            Integer value = (Integer) animation1.getAnimatedValue();
            view.getLayoutParams().height = value.intValue();
            view.requestLayout();
        });

        /*  We use an animationSet to play the animation  */

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.play(slideAnimator);
        animationSet.start();
    }

    public static void collapse(final View v, int duration) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();

                }
            }

            @Override
            public void setAnimationListener(AnimationListener listener) {
                super.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        v.setVisibility(View.GONE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(duration);
        v.startAnimation(a);
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().width = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().width = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(200);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialWidth = v.getMeasuredWidth();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().width = initialWidth - (int) (initialWidth * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(200);
        v.startAnimation(a);
    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    public static boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public static void openDetail(View layout, View v) {
        if (toggleArrow(v)) {
            expand(layout, 200);
        } else {
            collapse(layout, 200);
        }
    }

    public static void expand(final View v, int duration) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        // v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {


                    v.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                } else {


                    // v.getLayoutParams().height = (int) (targetHeight * interpolatedTime);
                }

                v.requestLayout();


            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        int ss = (int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density);
        if (ss > 250) ss = 250;
        a.setDuration(ss);
        v.startAnimation(a);

    }

    public static void slideUp(ViewGroup view, float f) {
        view.setTranslationY(f);
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        view.animate()
                .setDuration(150)
                .translationY(f - view.getHeight())
                .alpha(1.0f)
                .setListener(null);
    }

    public static void slideDown(ViewGroup view, float f) {
        view.animate()
                .translationY(f)
                .alpha(0.0f)
                .setDuration(150)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }

    public static void slidingX(ViewGroup view, float f) {
        view.animate()
                .setDuration(150)
                .translationX(f);
    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void animateHeight(final View view, float from, float to,
                                     int duration) {
        boolean expanding = to > from;

        ValueAnimator anim = ValueAnimator.ofInt((int) from, (int) to);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override//from  ww w. ja v  a 2s . c o m
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view
                        .getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (expanding) view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!expanding) view.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.setDuration(duration);
        anim.start();

    }
}

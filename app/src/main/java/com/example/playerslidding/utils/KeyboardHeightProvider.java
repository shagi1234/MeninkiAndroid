package com.example.playerslidding.utils;


import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.DisplayCutout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.core.view.DisplayCutoutCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class KeyboardHeightProvider extends PopupWindow implements OnApplyWindowInsetsListener {
    private View decorView;

    private DisplayMetrics metrics;

    private LinearLayout popupView;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    private Rect insets = new Rect(0, 0, 0, 0);

    public KeyboardHeightProvider(Context context, WindowManager windowManager, View decorView, KeyboardHeightListener listener) {
        super(context);
        this.decorView = decorView;

        metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        popupView = new LinearLayout(context);
        popupView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        globalLayoutListener = () -> {
            windowManager.getDefaultDisplay().getMetrics(metrics);

            int keyboardHeight = getKeyboardHeight(context);

            boolean screenLandscape = metrics.widthPixels > metrics.heightPixels;
            if (listener != null) {
                listener.onKeyboardHeightChanged(keyboardHeight, screenLandscape);
            }
        };

        setContentView(popupView);

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setWidth(0);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(0));

        ViewCompat.setOnApplyWindowInsetsListener(popupView, this);
    }

    public void start() {
        popupView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        decorView.post(() -> showAtLocation(decorView, Gravity.NO_GRAVITY, 0, 0));
    }

    @Override
    public void dismiss() {
        popupView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        super.dismiss();
    }

    @Override
    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        DisplayCutoutCompat cutoutCompat = insets.getDisplayCutout();
        if (cutoutCompat != null) {
            this.insets.set(cutoutCompat.getSafeInsetLeft(), cutoutCompat.getSafeInsetTop(), cutoutCompat.getSafeInsetRight(), cutoutCompat.getSafeInsetBottom());
        } else {
            this.insets.set(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
        }

        if (decorView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
            if (rootWindowInsets != null) {
                DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
                if (displayCutout != null) {
                    this.insets.set(displayCutout.getSafeInsetLeft(), displayCutout.getSafeInsetTop(), displayCutout.getSafeInsetRight(), displayCutout.getSafeInsetBottom());
                }
            }
        }

        return insets;
    }

    public int getKeyboardHeight(Context context) {
        Rect rect = new Rect();
        popupView.getWindowVisibleDisplayFrame(rect);

        int keyboardHeight = metrics.heightPixels - (rect.bottom - rect.top) - (insets.bottom - insets.top);
        int resourceID = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceID > 0) {
            keyboardHeight -= context.getResources().getDimensionPixelSize(resourceID);
        }
        if (keyboardHeight < 100) {
            keyboardHeight = 0;
        }

        return keyboardHeight;
    }

    public interface KeyboardHeightListener {
        void onKeyboardHeightChanged(int height, boolean isLandscape);
    }
}

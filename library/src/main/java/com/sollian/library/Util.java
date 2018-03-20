package com.sollian.library;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * @author sollian on 2018/3/20.
 */

public class Util {
    private static Application context;

    private static final DisplayMetrics sDisplayMetrics = new DisplayMetrics();
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static int sStatusBarHeight = -1;

    public static void init(Application context){
        Util.context = context;
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(sDisplayMetrics);
    }

    public static void showKeyboard(View vFocus) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null
//                && !imm.isActive()
                && vFocus != null) {
            imm.showSoftInput(vFocus, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideKeyboard(View vFocus) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null
                && imm.isActive()
                && vFocus != null) {
            imm.hideSoftInputFromWindow(vFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                sDisplayMetrics);
    }

    public static int getScreenHeight() {
        return sDisplayMetrics.heightPixels;
    }

    public static void setStatusBarTransparent(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void postDelayed2UI(Runnable r, long delayMillis) {
        handler.postDelayed(r, delayMillis);
    }

    public static int getStatusBarHeight() {
        if (sStatusBarHeight == -1) {
            int sbar = 0;
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                sbar = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                sStatusBarHeight = sbar;
            }

            try {
                //Use 25dp if no status bar height found
                if (sStatusBarHeight == 0) {
                    DisplayMetrics metrics = context
                            .getResources().getDisplayMetrics();
                    sStatusBarHeight = (int) (25 * metrics.scaledDensity);
                }
            } catch (Exception ignored) {
            }
        }

        return sStatusBarHeight;
    }
}

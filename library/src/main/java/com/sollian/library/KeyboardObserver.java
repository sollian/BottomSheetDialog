package com.sollian.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * @author sollian on 2018/3/6.
 */
class KeyboardObserver implements ViewTreeObserver.OnGlobalLayoutListener {
    /**
     * 增加一个高度门限，低于此高度认为是系统的NavigationBar
     */
    private static final int THRESHOLD = Util.dp2px(100);

    private static final int SCREEN_HEIGHT = Util.getScreenHeight();

    private static final int MIN_PANEL_HEIGHT = Util.dp2px(220);
    private static final int MAX_PANEL_HEIGHT = Util.dp2px(380);
    private static final int DEFAULT_PANEL_HEIGHT = Util.dp2px(300);

    private static final String PREF_NAME = "keyboardPref";
    private static final String KEY_PENEL_HEIGHT = "keyPanelHeight";

    private View vTarget;

    private int minBottom;
    private int maxBottom;

    private boolean isKeyboardShowing;
    private int keyboardHeight;
    private int panelHeight = DEFAULT_PANEL_HEIGHT;

    private OnKeyboardChangeListener listener;

    private SharedPreferences sp;

    void setTarget(View v, OnKeyboardChangeListener listener) {
        if (v == null) {
            if (vTarget != null) {
                vTarget.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
            this.listener = null;
            sp = null;
            return;
        }
        sp = v.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        vTarget = v;
        this.listener = listener;

        vTarget.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        int bottom = getBottomOnScreen(vTarget);

        boolean isShowing;
        if (bottom < SCREEN_HEIGHT - THRESHOLD) {
            isShowing = true;
            minBottom = bottom;
        } else {
            isShowing = false;
            maxBottom = bottom;
        }

        if (minBottom > 0 && maxBottom > 0) {
            keyboardHeight = maxBottom - minBottom;
        }

        if (isShowing) {
            int pHeight = getPanelHeight();
            if (pHeight != panelHeight) {
                panelHeight = pHeight;
                sp.edit().putInt(KEY_PENEL_HEIGHT, pHeight).apply();
                if (listener != null) {
                    listener.onPanelHeightChange(pHeight);
                }
            }
        }

        if (isShowing != isKeyboardShowing) {
            isKeyboardShowing = isShowing;
            if (listener != null) {
                listener.onVisibilityChange(isShowing);
            }
        }
    }

    private static int getBottomOnScreen(View view) {
        int bottom;
        if (Build.VERSION.SDK_INT >= 16 && view.getFitsSystemWindows()) {
            Rect out = new Rect();
            view.getWindowVisibleDisplayFrame(out);
            bottom = out.bottom;
        } else {
            int h = view.getHeight();
            int[] loc = new int[2];
            view.getLocationOnScreen(loc);
            bottom = loc[1] + h;
        }
        return bottom;
    }

    private int getPanelHeight() {
        if (keyboardHeight < MIN_PANEL_HEIGHT) {
            return MIN_PANEL_HEIGHT;
        } else if (keyboardHeight > MAX_PANEL_HEIGHT) {
            return MAX_PANEL_HEIGHT;
        } else {
            return keyboardHeight;
        }
    }

    int getDefaultPanelHeight() {
        return sp.getInt(KEY_PENEL_HEIGHT, DEFAULT_PANEL_HEIGHT);
    }
}

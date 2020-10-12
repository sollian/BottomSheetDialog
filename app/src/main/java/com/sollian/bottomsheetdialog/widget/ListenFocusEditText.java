package com.sollian.bottomsheetdialog.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author shouxianli on 2020/10/12.
 */
public class ListenFocusEditText extends EditText {

    private OnWindowFocusChangeListener windowFocusChangeListener;

    public ListenFocusEditText(Context context) {
        super(context);
    }

    public ListenFocusEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenFocusEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (windowFocusChangeListener != null) {
            windowFocusChangeListener.onWindowFocusChanged(hasWindowFocus);
        }
    }

    public void setWindowFocusChangeListener(
            OnWindowFocusChangeListener windowFocusChangeListener) {
        this.windowFocusChangeListener = windowFocusChangeListener;
    }

    public interface OnWindowFocusChangeListener {

        void onWindowFocusChanged(boolean hasFocus);
    }
}

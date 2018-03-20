package com.sollian.library;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author sollian on 2018/3/2.
 */
public class PanelFrameLayout extends FrameLayout {
    private boolean isShowing;
    private int panelHeight;

    public PanelFrameLayout(Context context) {
        super(context);
    }

    public PanelFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PanelFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,
                View.MeasureSpec.makeMeasureSpec(isShowing ? panelHeight : 0, View.MeasureSpec.EXACTLY));

    }

    public void setPanelHeight(int panelHeight) {
        this.panelHeight = panelHeight;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }
}

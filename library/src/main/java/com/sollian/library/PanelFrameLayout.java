package com.sollian.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 面板的容器
 *
 * @author sollian on 2018/3/2.
 */
public class PanelFrameLayout extends FrameLayout {

    private boolean isShowing;
    private int panelHeight;

    public PanelFrameLayout(Context context) {
        super(context);
    }

    public PanelFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PanelFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isShowing) {
            super.onMeasure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(panelHeight, MeasureSpec.EXACTLY));
        } else {
            setMeasuredDimension(getWidth(), 0);
        }

    }

    public void setPanelHeight(int panelHeight) {
        this.panelHeight = panelHeight;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    /**
     * 该方法添加View，preventRequestLayout==true时不会触发requestLayout调用
     */
    @Override
    public boolean addViewInLayout(View child,
            int index,
            ViewGroup.LayoutParams params,
            boolean preventRequestLayout) {
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }
}

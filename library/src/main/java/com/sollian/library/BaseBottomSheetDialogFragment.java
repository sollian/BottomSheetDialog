package com.sollian.library;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * @author sollian on 2018/2/3.
 */

public class BaseBottomSheetDialogFragment extends DialogFragment {
    private View vRoot;
    private DialogInterface.OnDismissListener dismissListener;
    private boolean isShowing;

    @CallSuper
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vRoot = view;
        vRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        slideToUp(view);
        isShowing = true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //需要在setContentView()之前设置
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //父类方法中调用了setContentView()
        super.onActivityCreated(savedInstanceState);

        //需要在setContentView()之后设置
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getDecorView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        dismiss();
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isShowing = false;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (isShowing) {
            return;
        }
        isShowing = true;
        if (TextUtils.isEmpty(tag)) {
            tag = getClass().getSimpleName();
        }

        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null) {
            try {
                super.show(manager, tag);
            } catch (Exception e) {
                e.printStackTrace();
                FragmentTransaction ft = manager.beginTransaction();
                ft.add(this, tag);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Deprecated
    @Override
    public final int show(FragmentTransaction transaction, String tag) {
        throw new UnsupportedOperationException("not support");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShowing = false;
        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void dismiss() {
        if (!isShowing) {
            return;
        }

        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }

        Util.hideKeyboard(dialog.getCurrentFocus());
        isShowing = false;
        slideToDown(vRoot);
    }

    @Override
    public void dismissAllowingStateLoss() {
        dismiss();
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    private void slideToUp(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(300);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.clearAnimation();
        view.startAnimation(slide);
    }

    private void slideToDown(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

        slide.setDuration(300);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.clearAnimation();
        view.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                BaseBottomSheetDialogFragment.super.dismissAllowingStateLoss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}

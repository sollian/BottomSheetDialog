package com.sollian.library;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * 适配性高，但ui交互欠友好
 *
 * @author sollian on 2018/3/6.
 */

public class PanelInputDialogFragment extends BaseBottomSheetDialogFragment {

    /**
     * 非法状态
     */
    protected static final int STATE_INVALID = -1;
    /**
     * panel和软键盘都隐藏
     */
    protected static final int STATE_DEFAULT = 0;
    protected static final int STATE_KEYBOARD = 1;
    protected static final int STATE_PANEL = 2;

    private int state;

    private KeyboardObserver keyboardObserver;

    private View vRoot;
    private PanelFrameLayout vPanel;
    private EditText vEdit;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
//            Util.setStatusBarTransparent(window);
//            vRoot.setFitsSystemWindows(true);

            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            window.setGravity(Gravity.TOP);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }

    protected void init(View root, PanelFrameLayout panel, EditText editText) {
        vRoot = root;
        vPanel = panel;
        vEdit = editText;

        state = STATE_DEFAULT;

        View.OnClickListener clickListener = new MyOnClickListener();
        vRoot.setOnClickListener(clickListener);
        vEdit.setOnClickListener(clickListener);

        keyboardObserver = new KeyboardObserver();
        keyboardObserver.setTarget(vRoot, new OnKeyboardChangeListener() {
            @Override
            public void onVisibilityChange(boolean isShowing) {
                if (isShowing) {
                    changeState(STATE_KEYBOARD);
                } else {
                    switch (state) {
                        case STATE_KEYBOARD:
                            changeState(STATE_DEFAULT);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onPanelHeightChange(int newHeight) {
                vPanel.setPanelHeight(newHeight);
            }
        });

        vPanel.setPanelHeight(keyboardObserver.getDefaultPanelHeight());
    }

    protected int getState() {
        return state;
    }

    protected void changeState(int newState) {
        int oldState = state;
        state = newState;
        beforeStateChange(oldState, newState);

        if (oldState != newState) {
            performChangeState(oldState, newState);
        }
    }

    private void performChangeState(int oldState, int newState) {
        switch (newState) {
            case STATE_PANEL:
                vPanel.setShowing(true);
                if (oldState == STATE_DEFAULT) {
                    vPanel.requestLayout();
                } else {
                    Util.hideKeyboard(vEdit);
                }
                break;
            case STATE_DEFAULT:
                vPanel.setShowing(false);
                Util.hideKeyboard(vEdit);
                break;
            case STATE_KEYBOARD:
                vPanel.setShowing(false);
                Util.showKeyboard(vEdit);
                break;
            default:
                break;
        }
    }

    protected void beforeStateChange(int oldState, int newState) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (keyboardObserver != null) {
            keyboardObserver.setTarget(null, null);
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == vRoot) {
                dismiss();
            } else if (v == vEdit) {
                changeState(STATE_KEYBOARD);
            }
        }
    }
}

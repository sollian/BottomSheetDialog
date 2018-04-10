package com.sollian.library;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * ui交互更加友好，但适配性可能不高
 *
 * @author sollian on 2018/3/6.
 */

public class PanelInputDialogFragment2 extends BaseBottomSheetDialogFragment {
    protected static final int STATE_DEFAULT = 0;
    protected static final int STATE_KEYBOARD = 1;
    protected static final int STATE_EMOJI = 2;

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
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                    | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    protected void init(View root, PanelFrameLayout panel, EditText editText) {
        vRoot = root;
        vPanel = panel;
        vEdit = editText;

        state = STATE_DEFAULT;

        vEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(STATE_KEYBOARD);
            }
        });

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

                Util.postDelayed2UI(new Runnable() {
                    @Override
                    public void run() {
                        updateWindowOffset(0);
                    }
                }, 0);
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

    protected void changeState(int state) {
        if (this.state == state) {
            return;
        }
        onStateChange(this.state, state);
        switch (state) {
            case STATE_EMOJI:
                vPanel.setShowing(true);
                if (this.state == STATE_KEYBOARD) {
                    pinWindow();
                    Util.hideKeyboard(vEdit);
                    Util.postDelayed2UI(new Runnable() {
                        @Override
                        public void run() {
                            vPanel.requestLayout();
                        }
                    }, 50);
                } else {
                    vPanel.requestLayout();
                }
                break;
            case STATE_DEFAULT:
                vPanel.setShowing(false);
                Util.hideKeyboard(vEdit);
                break;
            case STATE_KEYBOARD:
                vPanel.setShowing(false);
                if (this.state == STATE_EMOJI) {
                    pinWindow();
                    Util.postDelayed2UI(new Runnable() {
                        @Override
                        public void run() {
                            Util.showKeyboard(vEdit);
                        }
                    }, 0);
                } else {
                    Util.showKeyboard(vEdit);
                }
                break;
            default:
                break;
        }
        this.state = state;
    }

    protected void onStateChange(int oldState, int newState) {

    }

    private void pinWindow() {
        int[] loc = new int[2];
        vRoot.getLocationOnScreen(loc);
        updateWindowOffset(loc[1] - Util.getStatusBarHeight());
    }

    private void updateWindowOffset(int y) {
        Window window = getDialog().getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams attrs = window.getAttributes();
        if (attrs == null) {
            return;
        }
        attrs.gravity = y == 0 ? Gravity.BOTTOM : Gravity.TOP;
        attrs.y = y;
        window.setAttributes(attrs);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        keyboardObserver.setTarget(null, null);
    }
}

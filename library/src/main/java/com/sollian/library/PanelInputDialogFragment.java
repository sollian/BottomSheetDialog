package com.sollian.library;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * 适配性高，但ui交互欠友好
 * @author sollian on 2018/3/6.
 */

public class PanelInputDialogFragment extends BaseBottomSheetDialogFragment {
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
//            Util.setStatusBarTransparent(window);
//            vRoot.setFitsSystemWindows(true);

            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            window.setGravity(Gravity.TOP);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }

    protected void init(View root, PanelFrameLayout panel, EditText editText) {
        vRoot = root;
        vPanel = panel;
        vEdit = editText;

        state = STATE_DEFAULT;

        vRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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
                if (this.state == STATE_DEFAULT) {
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
        this.state = state;
    }

    protected void onStateChange(int oldState, int newState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        keyboardObserver.setTarget(null, null);
    }
}

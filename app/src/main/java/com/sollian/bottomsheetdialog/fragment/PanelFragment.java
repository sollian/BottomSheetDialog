package com.sollian.bottomsheetdialog.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.sollian.bottomsheetdialog.R;
import com.sollian.bottomsheetdialog.widget.ListenFocusEditText;
import com.sollian.bottomsheetdialog.widget.ListenFocusEditText.OnWindowFocusChangeListener;
import com.sollian.library.PanelFrameLayout;
import com.sollian.library.PanelInputDialogFragment;
import com.sollian.library.Util;

/**
 * @author sollian on 2018/3/20.
 */

public class PanelFragment extends PanelInputDialogFragment implements View.OnClickListener {

    private static final int PANEL_NONE = 0;
    private static final int PANEL_EMOTION = 1;
    private static final int PANEL_GIF = 2;

    private int curPanel = PANEL_NONE;

    private Button vOpenPanelEmotion;
    private Button vOpenPanelGif;
    private ListenFocusEditText vEdit;

    private PanelFrameLayout vPanelHolder;

    /**
     * 上次软键盘状态
     */
    private int lastState = STATE_KEYBOARD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vEdit = view.findViewById(R.id.edit);
        vOpenPanelEmotion = view.findViewById(R.id.open_panel_emotion);
        vOpenPanelGif = view.findViewById(R.id.open_panel_gif);
        vPanelHolder = view.findViewById(R.id.panel);

        init(view, vPanelHolder, vEdit);

        vOpenPanelEmotion.setOnClickListener(this);
        vOpenPanelGif.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lastState == STATE_KEYBOARD) {
            /*
            这种方式弹出软键盘最为保险，直接post在android 11上有问题
            ViewRootImpl#handleWindowFocusChanged在调用View#dispatchWindowFocusChanged后，
            会调用onPostWindowFocus方法为InputMethodManager(android 11为ImeFocusController)设置mServedView
            只有mServedView==vEdit时，才会调起软键盘
             */
            vEdit.setWindowFocusChangeListener(new OnWindowFocusChangeListener() {
                @Override
                public void onWindowFocusChanged(boolean hasFocus) {
                    if (hasFocus) {
                        vEdit.setWindowFocusChangeListener(null);
                        vEdit.post(new Runnable() {
                            @Override
                            public void run() {
                                Util.showKeyboard(getDialog().getWindow().getCurrentFocus());
                            }
                        });
                    }
                }
            });
            //api >= 18 可以直接使用下面的方式
//            vEdit.getViewTreeObserver()
//                    .addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
//                        @Override
//                        public void onWindowFocusChanged(boolean hasFocus) {
//                            if (hasFocus) {
//                                vEdit.getViewTreeObserver().removeOnWindowFocusChangeListener(this);
//                                vEdit.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Util.showKeyboard(getDialog().getWindow().getCurrentFocus());
//                                    }
//                                });
//                            }
//                        }
//                    });
            vEdit.requestFocus();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        lastState = getState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_panel_emotion:
                openPanelEmotion();
                break;
            case R.id.open_panel_gif:
                openPanelGif();
                break;
            default:
                break;
        }
    }

    private void openPanelEmotion() {
        if (getState() == STATE_PANEL && curPanel == PANEL_EMOTION) {
            changeState(STATE_KEYBOARD);
        } else {
            curPanel = PANEL_EMOTION;
            changeState(STATE_PANEL);
        }
    }

    private void openPanelGif() {
        if (getState() == STATE_PANEL && curPanel == PANEL_GIF) {
            changeState(STATE_KEYBOARD);
        } else {
            curPanel = PANEL_GIF;
            changeState(STATE_PANEL);
        }
    }

    @Override
    protected void beforeStateChange(int oldState, int newState) {
        if (newState != STATE_PANEL) {
            curPanel = PANEL_NONE;
        } else {
            showPanel(oldState == STATE_KEYBOARD);
        }

        vOpenPanelEmotion.setText(curPanel == PANEL_EMOTION ? "打开软键盘" : "打开表情面板");
        vOpenPanelGif.setText(curPanel == PANEL_GIF ? "打开软键盘" : "打开Gif面板");
    }

    private void showPanel(boolean isKeyboardShowing) {
        View panel = getActivePanel();

        if (panel != null) {
            int childCount = vPanelHolder.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = vPanelHolder.getChildAt(i);
                if (child != panel) {
                    /*
                    此处不要用GONE
                    GONE会触发requestLayout，导致在软键盘消失之前展示Panel
                     */
                    child.setVisibility(View.INVISIBLE);
                } else {
                    child.setVisibility(View.VISIBLE);
                }
            }

            addViewToPanel(panel, isKeyboardShowing);
        }
    }

    private View getActivePanel() {
        View panel = null;
        switch (curPanel) {
            case PANEL_EMOTION:
                panel = genPanel("表情面板");
                break;
            case PANEL_GIF:
                panel = genPanel("Gif面板");
                break;
            default:
                break;
        }
        return panel;
    }

    private void addViewToPanel(View view, boolean isKeyboardShowing) {
        if (view.getParent() == null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    Util.getScreenWidth(getActivity()),
                    FrameLayout.LayoutParams.MATCH_PARENT);
            if (isKeyboardShowing) {
                //不会触发requestLayout，软键盘隐藏时，自动触发layout
                vPanelHolder.addViewInLayout(view, 0, lp, true);
            } else {
                vPanelHolder.addView(view, lp);
            }
        }
    }

    private View genPanel(String desc) {
        TextView vText = new TextView(getActivity());
        vText.setText(desc);
        vText.setTextSize(20);
        return vText;
    }
}

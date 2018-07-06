package com.sollian.bottomsheetdialog.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sollian.bottomsheetdialog.R;
import com.sollian.library.PanelFrameLayout;
import com.sollian.library.PanelInputDialogFragment;
import com.sollian.library.PanelInputDialogFragment2;

/**
 * @author sollian on 2018/3/20.
 */

public class PanelFragment extends PanelInputDialogFragment implements View.OnClickListener {
    private Button vBt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText vEdit = view.findViewById(R.id.edit);
        vBt = view.findViewById(R.id.btn);
        PanelFrameLayout vPanel = view.findViewById(R.id.panel);

        init(view, vPanel, vEdit);

        vBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (getState()) {
            case STATE_EMOJI:
                changeState(STATE_KEYBOARD);
                break;
            case STATE_DEFAULT:
            case STATE_KEYBOARD:
                changeState(STATE_EMOJI);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStateChange(int oldState, int newState) {
        switch (newState) {
            case STATE_DEFAULT:
                vBt.setText("打开面板");
                break;
            case STATE_EMOJI:
                vBt.setText("打开软键盘");
                break;
            case STATE_KEYBOARD:
                vBt.setText("打开面板");
                break;
            default:
                break;
        }
    }
}

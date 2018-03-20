package com.sollian.bottomsheetdialog;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.sollian.bottomsheetdialog.fragment.CommonFragment;
import com.sollian.bottomsheetdialog.fragment.PanelFragment;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCommon(View view) {
        new CommonFragment().show(getSupportFragmentManager(), null);
    }

    public void openPanel(View view) {
        new PanelFragment().show(getSupportFragmentManager(), null);
    }
}

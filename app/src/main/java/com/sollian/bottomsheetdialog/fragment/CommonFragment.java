package com.sollian.bottomsheetdialog.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sollian.bottomsheetdialog.R;
import com.sollian.library.BaseBottomSheetDialogFragment;

/**
 * @author sollian on 2018/3/20.
 */

public class CommonFragment extends BaseBottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common, container, false);
    }
}

package com.lhalcyon.dapp.ui;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.lhalcyon.dapp.R;
import com.lhalcyon.dapp.databinding.DialogProgressBinding;
import com.lhalcyon.dapp.ui.base.BaseDialog;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/4/30
 * Brief Desc :
 * </pre>
 */
public class ProgressDialog extends BaseDialog<DialogProgressBinding> {

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        if(window != null){
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.0f;

            window.setAttributes(windowParams);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_progress;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isTouchOutside() {
        return false;
    }

    /**
     * 子类设置dialog宽度,默认屏幕宽度75%
     *
     * @return
     */
    protected int getDialogWidth() {
        return (int) (mScreenWidth * 0.25);
    }

    /**
     * 子类设置dialog高度,默认WRAP_CONTENT
     *
     * @return
     */
    protected int getDialogHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }
}

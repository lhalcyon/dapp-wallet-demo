package com.lhalcyon.dapp.ui.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lhalcyon.dapp.util.qumi.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/4/16
 * Brief Desc :
 * </pre>
 */
public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity {


    protected Context mContext;

    protected B mBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mBinding = DataBindingUtil.setContentView(this, layoutId());

        initView(savedInstanceState);


        // 状态栏设置
        if (isLightStatusTheme()) {
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        } else {
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        }
        // 事件消息订阅
        if (isRegisterEvent() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // force portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected boolean isRegisterEvent() {
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEvent()){
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * @return 是否启用状态栏深色文字图标
     */
    private boolean isLightStatusTheme() {
        return false;
    }

    protected abstract void initView(Bundle savedInstanceState);


    protected abstract int layoutId();


}

package com.lhalcyon.dapp.ui.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.lhalcyon.dapp.util.qumi.QMUIDisplayHelper;


/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/4/30
 * Brief Desc :
 * </pre>
 */
public abstract class BaseDialog<T extends ViewDataBinding> extends DialogFragment {

    protected Context mContext;
    protected T mBinding;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected static String TAG;
    protected OnDestroyListener mOnDestroyListener;

    public void setOnDestroyListener(OnDestroyListener onDestroyListener) {
        mOnDestroyListener = onDestroyListener;
    }

    public interface OnDestroyListener {
        void onDestroy();

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    /**
     * 获取子类资源
     *
     * @return
     */
    protected abstract int layoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TAG = getClass().getSimpleName();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(isTouchOutside());
        mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false);
        getScreenSize();
        getBundleExtras(getArguments());
        initView(savedInstanceState);

        return mBinding.getRoot();
    }

    /**
     * 获取Intent传递参数
     *
     * @param arguments
     */
    protected void getBundleExtras(Bundle arguments) {
    }

    private void getScreenSize() {
        int[] screenSize = QMUIDisplayHelper.getRealScreenSize(mContext);
        mScreenWidth = screenSize[0];
        mScreenHeight = screenSize[1];
    }


    /**
     * 点击dialog外侧dismiss
     *
     * @return
     */
    protected boolean isTouchOutside() {
        return true;
    }

    /**
     * 子类设置dialog宽度,默认屏幕宽度75%
     *
     * @return
     */
    protected int getDialogWidth() {
        return (int) (mScreenWidth * 0.75);
    }

    /**
     * 子类设置dialog高度,默认WRAP_CONTENT
     *
     * @return
     */
    protected int getDialogHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }


    /**
     * 初始化View控件
     */
    protected abstract void initView(Bundle savedInstanceState);


    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();

        //设置dialog宽度为屏幕75%,高度随内容扩充
        if (window != null) {
            window.setLayout(getDialogWidth(), getDialogHeight());
            //去掉dialog原有白色背景
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(gravity());
        }
    }

    public BaseDialog self(){
        return this;
    }

    public void show(AppCompatActivity activity){
        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .add(this,this.getTag())
                .commitAllowingStateLoss();
    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

    protected int gravity(){
        return Gravity.CENTER;
    }

    @Override
    public void onDestroy() {
        if(mOnDestroyListener != null){
            mOnDestroyListener.onDestroy();
        }
        super.onDestroy();
    }
}

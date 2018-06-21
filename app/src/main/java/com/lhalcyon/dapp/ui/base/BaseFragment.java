package com.lhalcyon.dapp.ui.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/4/16
 * Brief Desc :
 * </pre>
 */
public abstract class BaseFragment<B extends ViewDataBinding> extends Fragment implements FragmentUserVisibleController.UserVisibleCallback{

    protected Context mContext;

    protected B mBinding;

    protected static String TAG;


    private FragmentUserVisibleController mUserVisibleController = new FragmentUserVisibleController(this,this);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TAG = this.getClass().getSimpleName();
        mBinding = DataBindingUtil.inflate(inflater,layoutId(),container,false);

        initView(savedInstanceState);
        // 事件消息订阅
        if (isRegisterEvent()&& !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mUserVisibleController.addOnUserVisibleListener(mOnUserVisibleListener);
        return mBinding.getRoot();
    }

    private FragmentUserVisibleController.OnUserVisibleListener mOnUserVisibleListener = new FragmentUserVisibleController.OnUserVisibleListener() {
        @Override
        public void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
            if (isVisibleToUser) {

            }
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserVisibleController.activityCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserVisibleController.resume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mUserVisibleController.pause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mUserVisibleController.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public boolean isWaitingShowToUser() {
        return mUserVisibleController.isWaitingShowToUser();
    }

    @Override
    public void setWaitingShowToUser(boolean waitingShowToUser) {
        mUserVisibleController.setWaitingShowToUser(waitingShowToUser);
    }

    @Override
    public boolean isVisibleToUser() {
        return mUserVisibleController.isVisibleToUser();
    }

    @Override
    public void callSuperSetUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUserVisibleController.removeOnUserVisibleListener(mOnUserVisibleListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEvent()){
            EventBus.getDefault().unregister(this);
        }
    }

    protected abstract void initView(Bundle savedInstanceState);


    protected abstract int layoutId();




    /**
     * 仿onResume,子类重写此方法即可
     */
    @Override
    public void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
    }

    /**
     *
     * @return 是否注册Event消息
     */
    protected boolean isRegisterEvent(){
        return false;
    }
}

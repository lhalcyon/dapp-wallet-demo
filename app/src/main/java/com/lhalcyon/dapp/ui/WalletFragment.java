package com.lhalcyon.dapp.ui;

import android.databinding.ObservableField;
import android.os.Bundle;

import com.lhalcyon.dapp.R;
import com.lhalcyon.dapp.databinding.FragmentWalletBinding;
import com.lhalcyon.dapp.manager.WalletManager;
import com.lhalcyon.dapp.model.HLWallet;
import com.lhalcyon.dapp.model.event.SwitchWalletEvent;
import com.lhalcyon.dapp.ui.base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/19
 * Brief Desc :
 * </pre>
 */
public class WalletFragment extends BaseFragment<FragmentWalletBinding> {


    public ObservableField<String> currentAddress = new ObservableField<>();


    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding.setVm(this);
    }

    @Override
    public void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibleToUserChanged(isVisibleToUser, invokeInResumeOrPause);
        if (isVisibleToUser){
            HLWallet currentWallet = WalletManager.shared().getCurrentWallet(mContext);
            currentAddress.set(currentWallet.getAddress());
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SwitchWalletEvent event){
        HLWallet currentWallet = WalletManager.shared().getCurrentWallet(getContext());
        currentAddress.set(currentWallet.getAddress());
        //todo other switch thing
    }

    @Override
    protected boolean isRegisterEvent() {
        return true;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_wallet;
    }
}

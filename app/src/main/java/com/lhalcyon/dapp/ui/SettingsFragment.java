package com.lhalcyon.dapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lhalcyon.dapp.R;
import com.lhalcyon.dapp.bindlite.Perform;
import com.lhalcyon.dapp.databinding.FragmentSettingsBinding;
import com.lhalcyon.dapp.ui.base.BaseFragment;
import com.lhalcyon.dapp.util.SnackBarUtil;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/19
 * Brief Desc :
 * </pre>
 */
public class SettingsFragment extends BaseFragment<FragmentSettingsBinding> {

    public final Perform<View> call = new Perform<>(view -> {
        switch (view.getId()) {
            case R.id.btn_wallet_manage:
                startActivity(new Intent(mContext,InitWalletActivity.class));
                break;
            case R.id.btn_record:
                SnackBarUtil.show(mBinding.getRoot(),"holder for now :)");
                break;
            default:
                break;
        }
    });

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding.setCall(call);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_settings;
    }


}

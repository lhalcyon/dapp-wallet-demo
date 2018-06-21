package com.lhalcyon.dapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lhalcyon.dapp.R;
import com.lhalcyon.dapp.bindlite.Perform;
import com.lhalcyon.dapp.databinding.ActivityInitWalletBinding;
import com.lhalcyon.dapp.ui.base.BaseActivity;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/19
 * Brief Desc :
 * </pre>
 */
public class InitWalletActivity extends BaseActivity<ActivityInitWalletBinding> {


    public final Perform<View> call = new Perform<>(view -> {
        switch (view.getId()) {
            case R.id.btn_create:
                goCreateWallet();
                break;
            case R.id.btn_unlock:
                break;
            default:
                break;
        }
    });

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding.setCall(call);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white);
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        mBinding.toolbar.setTitle("");
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_init_wallet;
    }

    private void goCreateWallet() {
        startActivity(new Intent(mContext,CreateWalletActivity.class));
    }

}

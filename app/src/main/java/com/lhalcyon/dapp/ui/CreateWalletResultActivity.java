package com.lhalcyon.dapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lhalcyon.dapp.R;
import com.lhalcyon.dapp.bindlite.Perform;
import com.lhalcyon.dapp.config.Tag;
import com.lhalcyon.dapp.databinding.ActivityCreateWalletResultBinding;
import com.lhalcyon.dapp.ui.base.BaseActivity;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/21
 * Brief Desc :
 * </pre>
 */
public class CreateWalletResultActivity extends BaseActivity<ActivityCreateWalletResultBinding> {

    public final Perform<View> call = new Perform<>(view -> {
        switch (view.getId()) {
            case R.id.btn_ok:
                done();
                break;
        }
    });

    private void done() {
        startActivity(new Intent(mContext,MainActivity.class).putExtra(Tag.INDEX,1));
        finish();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String mnemonics = getIntent().getStringExtra(Tag.DATA);
        mBinding.setCall(call);
        mBinding.setMnemonics(mnemonics);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_create_wallet_result;
    }
}

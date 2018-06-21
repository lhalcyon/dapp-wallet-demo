package com.lhalcyon.dapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lhalcyon.dapp.R;
import com.lhalcyon.dapp.bindlite.Perform;
import com.lhalcyon.dapp.config.Tag;
import com.lhalcyon.dapp.databinding.ActivityCreateWalletBinding;
import com.lhalcyon.dapp.manager.InitWalletManager;
import com.lhalcyon.dapp.model.HLWallet;
import com.lhalcyon.dapp.stuff.HLError;
import com.lhalcyon.dapp.stuff.HLSubscriber;
import com.lhalcyon.dapp.stuff.ScheduleCompat;
import com.lhalcyon.dapp.ui.base.BaseActivity;
import com.lhalcyon.dapp.util.SnackBarUtil;

import io.reactivex.Flowable;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/21
 * Brief Desc :
 * </pre>
 */
public class CreateWalletActivity extends BaseActivity<ActivityCreateWalletBinding> {

    String mnemonics;

    public final Perform<View> call = new Perform<>(view -> {
        switch (view.getId()) {
            case R.id.btn_create:
                createWallet();
                break;
            default:
                break;
        }
    });

    private void createWallet() {
        String password = mBinding.etPassword.getText().toString().trim();
        String repassword = mBinding.etRePassword.getText().toString().trim();

        Flowable
                .just(password)
                .filter(o -> validateInput(password,repassword))
                .map(s -> {
                    mnemonics = InitWalletManager.shared().generateMnemonics();
                    return mnemonics;
                })
                .flatMap(s -> InitWalletManager.shared().generateWallet(mContext,password,s))
                .compose(ScheduleCompat.apply())
                .subscribe(new HLSubscriber<HLWallet>(mContext,true) {
                    @Override
                    protected void success(HLWallet data) {
                        startActivity(new Intent(mContext,CreateWalletResultActivity.class).putExtra(Tag.DATA,mnemonics));
                        finish();
                    }

                    @Override
                    protected void failure(HLError error) {
                        SnackBarUtil.show(mBinding.getRoot(),error.getMessage());
                    }
                });
    }

    private boolean validateInput(String password, String repassword) {
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)){
            SnackBarUtil.showInMain(mBinding.btnCreate,"please input password");
            return false;
        }
        if (!TextUtils.equals(password,repassword)){
            SnackBarUtil.showInMain(mBinding.getRoot(),"password does not match");
            return false;
        }
        return true;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding.setCall(call);

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white);
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        mBinding.toolbar.setTitle("Create Wallet");
    }



    @Override
    protected int layoutId() {
        return R.layout.activity_create_wallet;
    }
}

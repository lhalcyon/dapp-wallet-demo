package com.lhalcyon.dapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lhalcyon.dapp.R;
import com.lhalcyon.dapp.bindlite.Perform;
import com.lhalcyon.dapp.databinding.ActivityInitWalletBinding;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/19
 * Brief Desc :
 * </pre>
 */
public class InitWalletActivity extends AppCompatActivity {

    ActivityInitWalletBinding mBinding;

    public final Perform<View> call = new Perform<>(view -> {
        switch (view.getId()) {
            case R.id.btn_create:
                Toast.makeText(getApplicationContext(), "create", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_unlock:
                break;
            default:
                break;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_init_wallet);
        mBinding.setCall(call);
    }
}

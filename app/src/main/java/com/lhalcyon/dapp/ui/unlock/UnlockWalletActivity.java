package com.lhalcyon.dapp.ui.unlock;

import android.os.Bundle;

import com.lhalcyon.dapp.FragmentAdapter;
import com.lhalcyon.dapp.R;
import com.lhalcyon.dapp.databinding.ActivityUnlockWalletBinding;
import com.lhalcyon.dapp.ui.base.BaseActivity;
import com.lhalcyon.dapp.ui.base.BaseFragment;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/21
 * Brief Desc :
 * </pre>
 */
public class UnlockWalletActivity extends BaseActivity<ActivityUnlockWalletBinding> {


    @Override
    protected void initView(Bundle savedInstanceState) {
        List<BaseFragment> fragments = Arrays.asList(
                new UnlockMnemonicFragment(),
                new UnlockKeystoreFragment(),
                new UnlockPrivateKeyFragment()
        );
        List<String> labels = Arrays.asList("Mnemonics","Keystore","Private Key");
        mBinding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragments,labels));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_unlock_wallet;
    }
}

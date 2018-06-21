package com.lhalcyon.dapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lhalcyon.dapp.FragmentAdapter;
import com.lhalcyon.dapp.R;
import com.lhalcyon.dapp.config.Tag;
import com.lhalcyon.dapp.databinding.ActivityMainBinding;
import com.lhalcyon.dapp.manager.WalletManager;
import com.lhalcyon.dapp.model.HLWallet;
import com.lhalcyon.dapp.model.event.SwitchWalletEvent;
import com.lhalcyon.dapp.ui.base.BaseActivity;
import com.lhalcyon.dapp.util.HLWalletUtil;
import com.lhalcyon.dapp.util.qumi.QMUIStatusBarHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private List<HLWallet> mWallets = new ArrayList<>();

    private List<Integer> navs = Arrays.asList(
            R.id.item_market,
            R.id.item_wallet,
            R.id.item_settings
    );

    @Override
    protected void initView(Bundle savedInstanceState) {
        QMUIStatusBarHelper.translucent(this);

        WalletManager.shared().loadWallets(mContext);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_settings_white);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.w("setNavigationOnClickListener");
            }
        });
        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        List<Fragment> fragments = Arrays.asList(new MarketFragment(), new WalletFragment(), new SettingsFragment());
        mBinding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments));
        syncNavigationWallets();
        mBinding.bnv.setOnNavigationItemSelectedListener(item -> {
            int index = -1;
            HLWallet currentWallet = WalletManager.shared().getCurrentWallet(mContext);
            boolean needInitWallet = currentWallet == null;
            switch (item.getItemId()) {
                case R.id.item_market:
                    index = 0;
                    break;
                case R.id.item_wallet:
                    index = needInitWallet ? -2 : 1;
                    break;
                case R.id.item_settings:
                    index = 2;
                    break;
            }
            if (index != -1) {
                mBinding.viewPager.setCurrentItem(index, false);
            }
            if (index == -2) {
                startActivity(new Intent(MainActivity.this, InitWalletActivity.class));
            }
            mBinding.toolbar.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
            return index != -2;
        });
        mBinding.bnv.setSelectedItemId(navs.get(0));
        mBinding.navView.setNavigationItemSelectedListener(item -> {
            int order = item.getOrder();
            WalletManager.shared().switchCurrentWallet(mContext, mWallets.get(order));
            mBinding.drawerLayout.closeDrawer(Gravity.END);
            return true;
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected boolean isRegisterEvent() {
        return true;
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SwitchWalletEvent event) {
        syncNavigationWallets();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void syncNavigationWallets() {
        List<HLWallet> wallets = WalletManager.shared().getWallets();
        HLWallet currentWallet = WalletManager.shared().getCurrentWallet(mContext);
        if (wallets.size() == 0) {
            return;
        }
        Menu menu = mBinding.navView.getMenu();
        menu.clear();
        mWallets.clear();
        mWallets.addAll(wallets);
        for (int i = 0; i < wallets.size(); i++) {
            HLWallet hlWallet = wallets.get(i);
            mBinding.navView
                    .getMenu()
                    .add(R.id.wallet_group, i, i, HLWalletUtil.shortAddress(hlWallet.getAddress()))
                    .setIcon(R.drawable.ic_wallet)
                    .setChecked(hlWallet.getAddress().equalsIgnoreCase(currentWallet.getAddress()));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = intent.getIntExtra(Tag.INDEX, -1);
        if (index >= 0 && index < navs.size()) {
            mBinding.bnv.setSelectedItemId(navs.get(index));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.item_drawer:
                mBinding.drawerLayout.openDrawer(Gravity.END);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wallet, menu);
        return true;
    }
}

package com.lhalcyon.dapp.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lhalcyon.dapp.FragmentAdapter;
import com.lhalcyon.dapp.R;
import com.lhalcyon.dapp.databinding.ActivityMainBinding;
import com.lhalcyon.dapp.manager.WalletManager;
import com.lhalcyon.dapp.util.qumi.QMUIStatusBarHelper;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(mBinding.toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.setLogo(R.drawable.ic_settings_white);
            supportActionBar.setTitle("");
        }


        List<Fragment> fragments = Arrays.asList(new MarketFragment(), new WalletFragment(), new SettingsFragment());
        mBinding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments));
        mBinding.bnv.setOnNavigationItemSelectedListener(item -> {
            int index = -1;
            boolean needInitWallet = WalletManager.shared().isWalletEmpty();
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
                startActivity(new Intent(MainActivity.this,InitWalletActivity.class));
            }
            return !needInitWallet;
        });
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.toolbar.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.viewPager.setCurrentItem(0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.item_drawer:
                mBinding.drawerLayout.openDrawer(Gravity.RIGHT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wallet,menu);
        return true;
    }
}



package com.lhalcyon.dapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lhalcyon.dapp.ui.base.BaseFragment;

import java.util.List;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/4/17
 * Brief Desc :
 * </pre>
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mFragmentList;
    private List<String> mIndicatorLabelList;

    public FragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm);
        this.mFragmentList = fragmentList;
    }

    public FragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<String> indicatorLabelList) {
        super(fm);
        mFragmentList = fragmentList;
        mIndicatorLabelList = indicatorLabelList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mIndicatorLabelList == null ? super.getPageTitle(position):mIndicatorLabelList.get(position);
    }
}

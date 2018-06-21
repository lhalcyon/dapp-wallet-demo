package com.lhalcyon.dapp.manager;


import android.content.Context;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhalcyon.dapp.config.Tag;
import com.lhalcyon.dapp.model.HLWallet;
import com.lhalcyon.dapp.model.WalletStore;
import com.lhalcyon.dapp.model.event.SwitchWalletEvent;
import com.lhalcyon.dapp.util.ACache;
import com.lhalcyon.dapp.util.Singleton;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class WalletManager {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private HashMap<String, HLWallet> mHLWalletHashMap = new LinkedHashMap<>();
    private HLWallet mCurrentWallet;

    public void saveWallet(Context context, HLWallet wallet) {
        updateWallet(context, wallet);
        switchCurrentWallet(context, wallet);
    }

    public void updateWallet(Context context, HLWallet wallet) {
        mHLWalletHashMap.put(wallet.getAddress(), wallet);
        updateWallets(context);
    }

    public void updateWallets(Context context) {
        WalletStore walletStore = new WalletStore();
        walletStore.wallets = mHLWalletHashMap;
        String json = Singleton.gson().toJson(walletStore, WalletStore.class);
        ACache.get(context).put(Tag.WALLETS, json);
    }

    public void switchCurrentWallet(Context context, @NonNull HLWallet item) {
        if (mCurrentWallet != null) {
            mCurrentWallet.isCurrent = false;
        }
        mCurrentWallet = item;
        item.isCurrent = true;
        ACache.get(context).put(Tag.CURRENT_ADDRESS, item.getAddress());
        EventBus.getDefault().post(new SwitchWalletEvent());
    }

    public HLWallet getCurrentWallet(Context context) {
        if (mCurrentWallet == null) {
            //if not current wallet, try to load wallet files
            loadWallets(context);
            //no wallets, return null
            if (mHLWalletHashMap.isEmpty()) {
                return null;
            } else {
                //if get wallets, try to get user prefer wallet
                String address = ACache.get(context).getAsString(Tag.CURRENT_ADDRESS);
                if (address.length() > 0) {
                    //get user prefer wallet
                    switchCurrentWallet(context, mHLWalletHashMap.get(address));
                } else {
                    // if not prefer, get the first wallet
                    Iterator<String> iterator = mHLWalletHashMap.keySet().iterator();
                    switchCurrentWallet(context, mHLWalletHashMap.get(iterator.next()));

                }
            }
        }
        return mCurrentWallet;
    }

    public List<HLWallet> getWallets() {
        return new ArrayList<>(mHLWalletHashMap.values());
    }

    public void loadWallets(Context context) {
        try {
            String jsonString = ACache.get(context).getAsString(Tag.WALLETS);
            if (jsonString != null) {
                WalletStore store = objectMapper.readValue(jsonString, WalletStore.class);
                if (store != null) {
                    mHLWalletHashMap.clear();
                    mHLWalletHashMap.putAll(store.wallets);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ---------------- singleton stuff --------------------------
    public static WalletManager shared() {
        return WalletManager.Holder.singleton;
    }

    private WalletManager() {

    }

    private static class Holder {

        private static final WalletManager singleton = new WalletManager();

    }
}

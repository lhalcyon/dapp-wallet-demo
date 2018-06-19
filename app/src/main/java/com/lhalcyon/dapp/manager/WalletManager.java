package com.lhalcyon.dapp.manager;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhalcyon.dapp.model.HLWallet;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class WalletManager {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private HashMap<String, HLWallet> mHLWalletHashMap = new LinkedHashMap<>();
    private HLWallet mCurrentWallet;



    public boolean isWalletEmpty(){
        return mHLWalletHashMap.keySet().size() == 0;
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

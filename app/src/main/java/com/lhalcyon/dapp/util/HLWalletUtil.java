package com.lhalcyon.dapp.util;

import org.web3j.crypto.WalletUtils;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/21
 * Brief Desc :
 * </pre>
 */
public class HLWalletUtil {

    public static String shortAddress(String address){
        String shortOfAddress = "unknown";
        if (WalletUtils.isValidAddress(address)){
            int length = address.length();
            shortOfAddress = address.substring(0,10) + "..." + address.substring(length-10,length);
        }
        return shortOfAddress;
    }
}

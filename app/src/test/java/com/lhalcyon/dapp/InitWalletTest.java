package com.lhalcyon.dapp;

import com.lhalcyon.dapp.manager.InitWalletManager;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.junit.Before;
import org.junit.Test;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/19
 * Brief Desc :
 * </pre>
 */
public class InitWalletTest {

    String mnemonics = "memory stadium teach smart build orbit eyebrow baby trouble symptom expose tomato";

    @Before
    public void setup(){
        PrettyFormatStrategy strategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodCount(2)
                .tag("halcyon")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(strategy));
    }

    @Test
    public void testCreateWallet() {
        InitWalletManager.shared().generateKeyPair(mnemonics);
    }
}

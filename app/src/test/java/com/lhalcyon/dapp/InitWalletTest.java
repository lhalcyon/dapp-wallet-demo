package com.lhalcyon.dapp;

import com.lhalcyon.dapp.manager.InitWalletManager;
import com.lhalcyon.dapp.model.HLWallet;

import org.junit.Test;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/19
 * Brief Desc :
 * </pre>
 */
public class InitWalletTest {

    String mnemonics = "memory stadium teach smart build orbit eyebrow baby trouble symptom expose tomato";

    @Test
    public void testCreateWallet() {
        Flowable<String> flowable = Flowable.just(mnemonics);
        flowable.flatMap(new Function<String, Flowable<HLWallet>>() {
            @Override
            public Flowable<HLWallet> apply(String s) throws Exception {
                return InitWalletManager.shared().generateWallet("123", s);
            }
        }).subscribe(new Consumer<HLWallet>() {
            @Override
            public void accept(HLWallet hlWallet) throws Exception {
                System.out.println("success");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("failure");
            }
        });
    }
}

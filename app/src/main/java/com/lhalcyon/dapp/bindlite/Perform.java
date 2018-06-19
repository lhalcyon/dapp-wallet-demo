package com.lhalcyon.dapp.bindlite;

import io.reactivex.functions.Consumer;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/4/23
 * Brief Desc :
 * </pre>
 */
public class Perform<T> {

    private Consumer<T> mConsumer;

    public Perform(Consumer<T> consumer) {
        mConsumer = consumer;
    }

    public void execute(T t) {
        try {
            this.mConsumer.accept(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.lhalcyon.dapp.util;

import com.google.gson.Gson;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/21
 * Brief Desc :
 * </pre>
 */
public class Singleton {

    public static Gson gson(){
        return GsonHolder.singleton;
    }


    private static class GsonHolder {

        private static final Gson singleton = new Gson();
    }
}

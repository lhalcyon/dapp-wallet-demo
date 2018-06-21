package com.lhalcyon.dapp.util;

import android.support.design.widget.Snackbar;
import android.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/21
 * Brief Desc :
 * </pre>
 */
public class SnackBarUtil {

    public static void show(View view,String content){
        Snackbar.make(view,content,Snackbar.LENGTH_LONG).show();
    }

    public static void showInMain(View view ,String content){
        Flowable.just(content)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> show(view,s));
    }
}

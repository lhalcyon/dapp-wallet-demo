package com.lhalcyon.dapp.bindlite;

import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/4/23
 * Brief Desc :
 * </pre>
 */
public class RxBind {

    // 默认按钮防手抖时间(ms)
    public static final long THROTTLE_FIRST = 500;

    public static Disposable click(View view, Consumer<View> action) {
        return click(view,action,THROTTLE_FIRST);
    }

    public static Disposable click(View view, Consumer<View> action, long delay){
        return clicks(view)
                .throttleFirst(delay, TimeUnit.MILLISECONDS)
                .subscribe(action);
    }


    private static Flowable<View> clicks(@NonNull View view){
        return Flowable.create(new ViewClickOnSubscribe(view), BackpressureStrategy.ERROR);
    }
}

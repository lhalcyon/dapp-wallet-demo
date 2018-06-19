package com.lhalcyon.dapp.bindlite;

import android.view.View;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/4/23
 * Brief Desc :
 * </pre>
 */
public class ViewClickOnSubscribe implements FlowableOnSubscribe<View> {


    private final View view;

    public ViewClickOnSubscribe(View view) {
        this.view = view;

    }

    @Override
    public void subscribe(FlowableEmitter<View> emitter) throws Exception {
        verifyMainThread();
        View.OnClickListener listener = v -> {
            if (!emitter.isCancelled()){
                emitter.onNext(v);
            }
        };
        view.setOnClickListener(listener);
        emitter.setCancellable(() -> view.setOnClickListener(null));
    }

}

package com.lhalcyon.dapp.stuff;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.lhalcyon.dapp.ui.ProgressDialog;

import org.reactivestreams.Subscription;

import io.reactivex.FlowableSubscriber;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018-4-17
 * Brief Desc :
 * </pre>
 */
public abstract class HLSubscriber<T> implements FlowableSubscriber<T> {

    private boolean needLoading = false;

    private ProgressDialog mDialog;

    private Context mContext;

    private Subscription s;

    private boolean autoDismiss = true;


    public HLSubscriber() {
    }


    public HLSubscriber(Context context, boolean needLoading) {
        this.needLoading = needLoading;
        mContext = context;
    }


    public void dispose() {
        try {
            if (s != null) {
                s.cancel();
                s = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract protected void success(T data);

    abstract protected void failure(HLError error);

    protected void done(boolean isSuccess) {
        if (autoDismiss) {
            dismiss();
        }
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Integer.MAX_VALUE);
        this.s = s;
        if (needLoading) {
            mDialog = new ProgressDialog();
            mDialog.setCancelable(true);
            mDialog.setOnDestroyListener(this::dispose);
            if (mContext instanceof AppCompatActivity){
                AppCompatActivity activity = (AppCompatActivity) mContext;
                activity
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .add(mDialog, mDialog.getTag())
                        .commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onNext(T t) {
        try {
            success(t);
            done(true);
        } catch (Exception e) {
            e.printStackTrace();
            onError(new HLError(ReplyCode.invokeException,e));

        }
    }

    @Override
    public void onError(Throwable e) {
        HLError error = new HLError(ReplyCode.failure,e);
        failure(error);
        done(false);
    }

    private void dismiss() {
        if (mDialog != null) {
            try {
                mDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onComplete() {
        this.done(true);
    }
}

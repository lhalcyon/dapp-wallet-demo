package com.lhalcyon.dapp.bindlite;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/4/23
 * Brief Desc :
 * </pre>
 */
public class ViewBindingAdapter {

    /**
     * 有delay的click
     * @param view 视图
     * @param click 点击事件
     */
    @BindingAdapter({"click"})
    public static void click(View view, final Perform<View> click) {
        view.setClickable(true);
        RxBind.click(view, v -> {
            if(click != null){
                click.execute(v);
            }
        });
    }
}

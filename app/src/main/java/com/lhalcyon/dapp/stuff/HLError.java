package com.lhalcyon.dapp.stuff;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018-4-17
 * Brief Desc :
 * </pre>
 */
public class HLError extends Throwable {

    public int code;

    public Throwable e;


    public HLError() {
    }

    public HLError(int code, Throwable e) {
        this.code = code;
        this.e = e;
    }

    public String getMessage() {
        if (e != null) {
            return e.getMessage();
        }
        return "unknown error";
    }


}

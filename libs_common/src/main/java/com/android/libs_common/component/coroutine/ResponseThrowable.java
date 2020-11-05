package com.android.libs_common.component.coroutine;

/**
 * Created by goldze on 2017/5/11.
 */

public class ResponseThrowable extends Exception {
    public int code;
    public String message;

    public ResponseThrowable(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ResponseThrowable(String msg, int code) {
        super(msg);
        this.message=msg;
        this.code = code;
    }
}

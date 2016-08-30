package com.milton.common.eventbus;

/**
 * Created by milton on 16/8/25.
 */
public class BaseEvent {
    protected int mType;
    protected String mResult;
    protected boolean mSuccess;

    public BaseEvent(int type, String result, boolean success) {
        mType = type;
        mResult = result;
        mSuccess = success;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        this.mResult = result;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        this.mSuccess = success;
    }
}

package com.dji.gsdemo.gmapsteste.app;

public interface RunnableCallback<T> {
    void onComplete(T result);
    void onError(Exception e);
}

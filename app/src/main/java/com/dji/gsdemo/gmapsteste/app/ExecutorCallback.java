package com.dji.gsdemo.gmapsteste.app;

public interface ExecutorCallback<T> {
    void onComplete(T result);
    void onError(Exception e);
}

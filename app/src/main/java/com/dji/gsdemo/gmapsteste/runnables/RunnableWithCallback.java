package com.dji.gsdemo.gmapsteste.runnables;

import android.os.Handler;

import com.dji.gsdemo.gmapsteste.app.RunnableCallback;


public abstract class RunnableWithCallback<T, K> implements Runnable{
    private final T input;
    private final Handler handler;
    private final RunnableCallback<K> callback;
    private boolean completed;

    public RunnableWithCallback(T input, Handler handler, RunnableCallback<K> callback) {
        this.input = input;
        this.handler = handler;
        this.callback = callback;
    }

    public T getInput() {
        return input;
    }

    public Handler getHandler() {
        return handler;
    }

    public RunnableCallback<K> getCallback() {
        return callback;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        this.completed = true;
    }
}

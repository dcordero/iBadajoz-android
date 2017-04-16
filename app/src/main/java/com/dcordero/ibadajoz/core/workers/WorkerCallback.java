package com.dcordero.ibadajoz.core.workers;


public abstract class WorkerCallback {

    public abstract void success(Object responseObject);
    public abstract void failure();
}
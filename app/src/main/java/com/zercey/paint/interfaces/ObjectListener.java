package com.zercey.paint.interfaces;

public interface ObjectListener<T> {

    void onGetObject(T o);

    void onFail(Exception e);
}

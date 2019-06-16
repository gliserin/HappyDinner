package org.odhs.happydinner.listener;

import retrofit2.HttpException;

public interface ApiCallback<T> {

    void onSuccess(T value);
    void onFail(Throwable t);
}

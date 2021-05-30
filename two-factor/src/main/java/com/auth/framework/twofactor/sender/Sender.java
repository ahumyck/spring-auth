package com.auth.framework.twofactor.sender;

public interface Sender<T> {

    void send(T code);

}

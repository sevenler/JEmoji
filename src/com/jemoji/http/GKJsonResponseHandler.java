package com.jemoji.http;

/**
 * Created by benz on 13-7-19.
 * Block from HTTP Interface
 * Require Jackson.jar
 */
public interface GKJsonResponseHandler {

    /**
     * Block
     * @param json Map or List
     * @param error an error object, null for no error
     */
    public void onResponse(int code, Object json, Throwable error);
}

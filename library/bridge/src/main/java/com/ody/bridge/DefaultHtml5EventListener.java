package com.ody.bridge;

public class DefaultHtml5EventListener implements Html5EventListener {

    String TAG = "DefaultHtml5EventListener";

    @Override
    public void handler(String data, CallBackFunction function) {
        if (function != null) {
            function.onCallBack("DefaultHtml5EventListener response data");
        }
    }

}

package com.ody.bridge;


public interface WebViewJavascriptBridge {

    public void sendEventToHtml5(String data);

    public void sendEventToHtml5(String data, CallBackFunction responseCallback);
}

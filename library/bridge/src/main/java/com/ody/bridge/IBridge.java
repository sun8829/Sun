package com.ody.bridge;


public interface IBridge {

    void sendDataToHtml5(String data);

    void sendDataToHtml5(String data, CallBackFunction responseCallback);

    void sendEventToHtml5(String handlerName);

    void sendEventToHtml5(String handlerName, CallBackFunction callBack);

    void sendEventToHtml5(String handlerName, String data, CallBackFunction callBack);
}

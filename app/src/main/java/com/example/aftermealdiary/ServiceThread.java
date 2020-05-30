package com.example.aftermealdiary;

import java.util.logging.Handler;

public class ServiceThread extends Thread {

    Handler handler;
    boolean isRun;


    public ServiceThread(Handler handler) {
        this.handler = handler;
    }


}

package com;

import com.payservice.ZgYdHttpRequest;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ZgYdHttpRequest httpRequest = new ZgYdHttpRequest();
        ZgYdHttpRequest.do10086Post();
        httpRequest.do10086Buy();
        httpRequest.doBuyAjax();
    }
}

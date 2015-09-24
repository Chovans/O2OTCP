package com.dotnar.test;

import com.dotnar.bank.service.BankPayService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author chovans on 15/9/8.
 */
public class HttpTest {
    public static void main(String[] arg) throws Exception {
        BankPayService.prePay1("{}");
    }
}

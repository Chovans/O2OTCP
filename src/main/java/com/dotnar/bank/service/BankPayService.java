package com.dotnar.bank.service;

import cmb.netpayment.Settle;
import com.dotnar.bean.bank.PrePayRequest;
import com.dotnar.bean.mongo.MongoResult;
import com.dotnar.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author chovans on 15/9/11.
 */
@Service
public class BankPayService {

    /**
     * BranchID:	商户开户分行号，请咨询开户的招商银行分支机构；
     * CoNo:		商户号，6位数字，由银行在商户开户时确定；
     * BillNo:		定单号，6位或10位数字，由商户系统生成，一天内不能重复；
     * Amount:		定单总金额，格式为：xxxx.xx元；
     * Date:		交易日期，格式：YYYYMMDD。
     *
     * @return
     */
    static int iRet = 0;
    static Settle settle;
    public static String prePay1(String json) {
        MongoResult mongoResult = new MongoResult();
        PrePayRequest prePayRequest = JsonUtil.parseObject(json, PrePayRequest.class);
         settle = new Settle();
         iRet = settle.SetOptions("payment.ebank.cmbchina.com");
        if (iRet == 0)
        {
            System.out.println("SetOptions ok");
        }
        else
        {
            System.out.println(settle.GetLastErr(iRet));
            System.out.println(settle.GetLastErr(iRet));
            return null;
        }

        iRet = settle.LoginC("0755","000107","888888");
        if (iRet == 0)
        {
            System.out.println("LoginC ok");
        }
        else
        {
            System.out.println(settle.GetLastErr(iRet));
            return null;
        }
        testQueryUnsettledOrder();

        settle.Logout();

//        testVerifySign();
        return JsonUtil.toJSONString(mongoResult);
    }

    private static void testQueryUnsettledOrder()
    {
        StringBuffer strbuf = new StringBuffer();
        iRet = settle.QueryUnsettledOrder(strbuf);
        if (iRet == 0)
        {
            System.out.println("QueryUnsettledOrder ok");
            System.out.println(strbuf.toString());
        }
        else
        {
            System.out.println(settle.GetLastErr(iRet));
        }
    }

}

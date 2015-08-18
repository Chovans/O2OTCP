package com.dotnar.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotnar.bean.git.GitProject;
import com.dotnar.contant.TCPConfig;
import com.dotnar.contant.WXPayConfigure;
import com.dotnar.util.GitUtil;
import hprose.client.HproseTcpClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * payRequestJson
 * Created by chovans on 15/7/13.
 */
public class WXPayTest {
    public static void main(String[] args) throws Exception {
//        final HproseTcpClient client = new HproseTcpClient(WXPayConfigure.WXPAY);
//        try {
//            String json = "{" +
//                    "                'appid': 'wxcf74f930098faee1'," +
//                    "                    'mch_id': '1233472902'," +
//                    "                    'device_info': 'device_info'," +
//                    "                    'body': 'test body'," +
//                    "                    'detail': 'test detail'," +
//                    "                    'attach': 'test attach'," +
//                    "                    'out_trade_no': 'test-out-trade-no-1'," +
//                    "                    'fee_type': ''," +
//                    "                    'total_fee': '1'," +
//                    "                    'spbill_create_ip': '127.0.0.0'," +
//                    "                    'goods_tag': 'test-goods-tag'," +
//                    "                    'trade_type': 'JSAPI'," +
//                    "                    'product_id': 'test-product-id'," +
//                    "                    'openid': 'o_QrEjjIkQ9URa1YnZ8gWi6G2AWs'" +
//                    "            }";
//            String key = "HXCU71nGNU5O4hvQlGSXEuMEMTWQs0HW";
//            Object object = client.invoke("payRequestJson", new Object[]{json,key});
//            System.out.println(object);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        String url = "https://github.com/Chovans/O2OTCP.git";
//        Pattern pattern = Pattern.compile("\\w*\\.git");
//        Matcher matcher = pattern.matcher(url);
//        System.out.println(url.replace(matcher.replaceAll(""),"").split("\\.")[0]);

        List<GitProject> gitProjects = new ArrayList<>();
        GitProject gitProject1 = new GitProject();
        gitProject1.setName("p1");
        gitProject1.setParentTemplateName("p2");
        GitProject gitProject2 = new GitProject();
        gitProject2.setName("p2");
        gitProject2.setParentTemplateName("p3");
        GitProject gitProject3 = new GitProject();
        gitProject3.setName("p3");
        gitProject3.setParentTemplateName("p4");
        GitProject gitProject4 = new GitProject();
        gitProject4.setName("p4");
        gitProject4.setParentTemplateName("p5");
        GitProject gitProject5 = new GitProject();
        gitProject5.setName("p5");
        gitProject5.setParentTemplateName("p6");
        GitProject gitProject6 = new GitProject();
        gitProject6.setName("p7");
        gitProject6.setParentTemplateName("");
        gitProjects.add(gitProject1);
        gitProjects.add(gitProject2);
        gitProjects.add(gitProject3);
        gitProjects.add(gitProject4);
        gitProjects.add(gitProject5);
        gitProjects.add(gitProject6);

        List<String> ps = new ArrayList<>();
        ps.add(gitProject1.getName());

        findByName(gitProjects,ps,gitProject1);

        File file = new File("/usr/local/gitDepot/javaGitTest/package.json");
        JSONObject obj = JSON.parseObject(FileUtils.readFileToString(file));

        System.out.println(obj.toJSONString());

    }

    public static void findByName(List<GitProject> gitProjects,List<String> sortedGitProjects,GitProject gp){
        for(GitProject gitProject:gitProjects){
            if(gitProject.getName().equals(gp.getParentTemplateName())){
                if(sortedGitProjects.size() == 1)
                    sortedGitProjects.add(gp.getParentTemplateName());
                sortedGitProjects.add(gitProject.getParentTemplateName());
                findByName(gitProjects,sortedGitProjects,gitProject);
            }
        }
    }

}

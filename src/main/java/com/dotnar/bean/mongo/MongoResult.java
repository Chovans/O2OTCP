package com.dotnar.bean.mongo;

/**
 * @author chovans on 15/8/22.
 */

import java.io.Serializable;

/**
 * it's result
 */
public class MongoResult implements Serializable{
    private String result="success";
    private String msg="ok";
    private Object content=null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}

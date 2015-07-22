package com.dotnar.exception;

/**
 * 抛出异常
 * Created by chovans on 15/7/20.
 */
public class WXPayException extends Exception {

   public WXPayException(String message){
       this.errmsg = message;
   }
    private String errmsg;
    private Boolean sys_error = true;

    @Override
    public String getMessage() {
        return errmsg;
    }

    public void setMessage(String message) {
        this.errmsg = message;
    }
}

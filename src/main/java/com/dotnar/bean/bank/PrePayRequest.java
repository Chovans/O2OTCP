package com.dotnar.bean.bank;

import java.math.BigDecimal;

/**
 * @author chovans on 15/9/11.
 */
public class PrePayRequest {
    /**
     * BranchID:	商户开户分行号，请咨询开户的招商银行分支机构；
     CoNo:		商户号，6位数字，由银行在商户开户时确定；
     BillNo:		定单号，6位或10位数字，由商户系统生成，一天内不能重复；
     Amount:		定单总金额，格式为：xxxx.xx元；
     Date:		交易日期，格式：YYYYMMDD。
     */

    private String BranchID;
    private String CoNo;
    private String BillNo;
    private BigDecimal Amount;
    private String Date;

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public String getCoNo() {
        return CoNo;
    }

    public void setCoNo(String coNo) {
        CoNo = coNo;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}

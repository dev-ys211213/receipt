package kr.co.ubcn.receipt.model;

import lombok.Data;

@Data
public class ReceiptInfo {
    //public String tableName;
    public String tranDate;
    public String tranTime;
    public String tranSeqNo;
    public String terminalId;
    public String transactionNo;
    public String cardNo;
    public Integer totalAmt;
    public String businessNo;
    public String issuerName;
    public String pdColumn;
    public String pgMerchNbr;
    public String address;
    public String addressDetail;
    public String presentName;
    public String tel;
    public Integer tax;
    public String merchAntName;
    public String authNo;
    public String insDt;
    public String cancelDate;
    public String reversalType;




}

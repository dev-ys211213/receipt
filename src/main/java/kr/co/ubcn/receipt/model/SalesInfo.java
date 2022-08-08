package kr.co.ubcn.receipt.model;

import lombok.Data;

@Data
public class SalesInfo {
    public String tranDate;
    public String tranTime;
    public String terminalId;
    public String transactionNo;
    public Integer organizationSeq;
    public String payStep;
    public String cancelDate;
}

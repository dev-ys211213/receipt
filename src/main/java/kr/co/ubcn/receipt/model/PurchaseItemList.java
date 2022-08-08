package kr.co.ubcn.receipt.model;

import lombok.Data;

@Data
public class PurchaseItemList {
    //public Integer itemCount;
    public String productName;
    public Integer productPrice;
    public Integer qty;
}

package kr.co.ubcn.receipt.mapper.vmms;

import kr.co.ubcn.receipt.model.PurchaseItemList;
import kr.co.ubcn.receipt.model.SalesInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemMapper {
    //public PurchaseItemInfo selectColumnToProductInfo(Map<String, Object> map);
    public List<PurchaseItemList> selectPurchaseItemList(Map<String, Object> map);
    public List<PurchaseItemList> selectSalesItemList(Map<String, Object> map);
    public SalesInfo selectSalesInfo(Map<String, Object> map);
}

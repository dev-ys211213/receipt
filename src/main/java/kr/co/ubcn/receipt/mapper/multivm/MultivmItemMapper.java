package kr.co.ubcn.receipt.mapper.multivm;

import kr.co.ubcn.receipt.model.PurchaseItemList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MultivmItemMapper {
    //public PurchaseItemInfo selectColumnToProductInfo(Map<String, Object> map);
    public List<PurchaseItemList> selectMultivmSalesItemList(Map<String, Object> map);
    //public SalesInfo selectSalesInfo(Map<String, Object> map);
}

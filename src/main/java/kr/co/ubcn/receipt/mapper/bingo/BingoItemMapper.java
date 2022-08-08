package kr.co.ubcn.receipt.mapper.bingo;

import kr.co.ubcn.receipt.model.PurchaseItemList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BingoItemMapper {
    //public PurchaseItemInfo selectColumnToProductInfo(Map<String, Object> map);
    public List<PurchaseItemList> selectBingoSalesItemList(Map<String, Object> map);
    //public SalesInfo selectSalesInfo(Map<String, Object> map);
}

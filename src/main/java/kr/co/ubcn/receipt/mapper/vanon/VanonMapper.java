package kr.co.ubcn.receipt.mapper.vanon;

import kr.co.ubcn.receipt.model.ReceiptInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface VanonMapper {
    public ReceiptInfo selectReceiptInfo(Map<String, Object> map);
}

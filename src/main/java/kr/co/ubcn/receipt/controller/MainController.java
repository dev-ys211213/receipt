package kr.co.ubcn.receipt.controller;

import kr.co.ubcn.receipt.mapper.bingo.BingoItemMapper;
import kr.co.ubcn.receipt.mapper.multivm.MultivmItemMapper;
import kr.co.ubcn.receipt.mapper.vanon.VanonMapper;
import kr.co.ubcn.receipt.mapper.vmms.ItemMapper;
import kr.co.ubcn.receipt.model.BingoSalesItemList;
import kr.co.ubcn.receipt.model.PurchaseItemList;
import kr.co.ubcn.receipt.model.ReceiptInfo;
import kr.co.ubcn.receipt.model.SalesInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {

    @Autowired
    ItemMapper itemMapper;
    @Autowired
    VanonMapper vanonMapper;
    @Autowired
    BingoItemMapper bingoItemMapper;
    @Autowired
    MultivmItemMapper multivmItemMapper;


    Logger logger = LoggerFactory.getLogger(MainController.class);

    /**
     * 신용내역 전자영수증을 조회한다.
     *
     * @param : tranDate, tranTime, tranSeqNo, terminalId, selectType
     * @return 전자영수증 정보, 상품 정보
     */
    //@GetMapping("/")
    @RequestMapping(value = "/", method = {RequestMethod.GET}) //, headers = ("Content-Type=application/json;charset=UTF-8")
    public ModelAndView root(HttpServletRequest request
            ,@RequestHeader("json") String json
            //,@RequestHeader("json1") ArrayList<JSONObject> json1
            //,@RequestParam ArrayList<JSONObject> param1
            //,@RequestBody ArrayList<JSONObject> param1
            , @RequestParam Map<String, Object> param){//, @CookieValue("cookie") String cookie){
        System.out.println("param: "+param);
       //System.out.println("param1: "+param1);
        System.out.println("json: "+json);
        //System.out.println("json1: "+json1);
        ModelAndView mav = new ModelAndView();
        //String tableName = "TBLTLF"+param.get("tranDate").toString();
        param.put("tableName", "TBLTLF"+param.get("tranDate").toString());
        ReceiptInfo receiptInfo = vanonMapper.selectReceiptInfo(param);
        param.put("transactionNo",receiptInfo.getTransactionNo());

        if(receiptInfo.getReversalType()!=null||receiptInfo.getCancelDate()!=null){
            logger.info("전자영수증 조회시도 실패(결제취소 건) : {} ", receiptInfo);
            ModelAndView error = new ModelAndView();
            error.addObject("message","결제 취소된 건은 영수증을 출력할 수 없습니다.")
                    .setViewName("error");
            return error;
        }

        if(param.get("type")==null){
            param.put("type","vmms");
        }

        logger.info("전자영수증 조회시도 성공 건 : {} ", receiptInfo);
        int total = 0;
        List<PurchaseItemList> productList = new ArrayList<>();

        switch ((param.get("type")).toString()){
            case "bingo":
                param.put("authNo", receiptInfo.getAuthNo());
                productList = bingoItemMapper.selectBingoSalesItemList(param);
                logger.info("[tId: {},tranNo: {}] bingo상품 조회 : {} ", receiptInfo.getTerminalId(),receiptInfo.getTransactionNo(),productList);
                break;
            case "bingo24":
                if(param.get("itemCount")==null){
                    logger.info("전자영수증 조회시도 실패(파라미터 이상) : {} ", receiptInfo);
                    ModelAndView error = new ModelAndView();
                    error.addObject("message","요청 파라미터를 확인 바랍니다.\n※ bingo24 타입 선택시 itemCount 필수")
                            .setViewName("error");
                    return error;
                }
                for(int i=0; i<= Integer.parseInt(param.get("itemCount").toString())-1; i++){
                    PurchaseItemList product = new PurchaseItemList();
                    if(param.get("productList"+(i+1))!=null) {
                        String[] str = param.get("productList" + (i + 1)).toString().split("\\|");
                        product.setProductName(str[0]);
                        product.setQty(Integer.parseInt(str[1]));
                        product.setProductPrice(Integer.parseInt(str[2]));
                        total += product.getProductPrice() * product.getQty();
                        productList.add(product);
                    }
                }
                logger.info("[tId: {},tranNo: {}] bingo24상품 조회 : {} ", receiptInfo.getTerminalId(),receiptInfo.getTransactionNo(),productList);
                break;
            case "multivm":
                productList = multivmItemMapper.selectMultivmSalesItemList(param);
                logger.info("[tId: {},tranNo: {}] multivm상품 조회 : {} ", receiptInfo.getTerminalId(),receiptInfo.getTransactionNo(),productList);
                break;
            case "vmms": default:
                System.out.println("vmms)PD_COLUMN 문자열길이: "+receiptInfo.getPdColumn().length());
                if(receiptInfo.getPdColumn().length()>=23){
                    System.out.println("#장바구니 상품) "+receiptInfo.getPdColumn().length());
                    productList = itemMapper.selectPurchaseItemList(param);
                }else {
                    System.out.println("#일반 상품) " + receiptInfo.getPdColumn().length());
                    productList = itemMapper.selectSalesItemList(param);
                }

                logger.info("[tId: {},tranNo: {}] vmms상품 조회 : {} ", receiptInfo.getTerminalId(),receiptInfo.getTransactionNo(),productList.toString());
                /*for (PurchaseItemList list : productList) {
                    total +=list.getQty() * list.getProductPrice();
                }*/
                break;
        }
        if(!(param.get("type")).toString().equals("bingo24")){
            for (PurchaseItemList list : productList) {
                total +=list.getQty() * list.getProductPrice();
            }
            System.out.println("total계산: "+total);
        }
        if(receiptInfo.getTotalAmt()!=total){
            PurchaseItemList product = new PurchaseItemList();
            product.setProductName("미등록 상품");
            product.setQty(1);
            product.setProductPrice(receiptInfo.getTotalAmt()-total);
            productList.add(product);
        }

        mav.addObject("receiptInfo",receiptInfo)
                .addObject("productList",productList)
                .setViewName("index");

        return mav;
    }

}

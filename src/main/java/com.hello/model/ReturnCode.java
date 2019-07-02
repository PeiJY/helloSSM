package com.hello.model;

import java.util.HashMap;
import java.util.Map;

public class ReturnCode {
    private static Map<String,String> ReturnCodeMap = new HashMap<String, String>();
    private static final ReturnCode instance = new ReturnCode();
    private ReturnCode(){
                ReturnCodeMap.put("Success","1000");
                ReturnCodeMap.put("ZeroResultNumber","2001");
                ReturnCodeMap.put("IncorrectUserInfo","3001");
                ReturnCodeMap.put("UnlinkedAccount","3002");
                ReturnCodeMap.put("IncorrectBindingCode","3003");
                ReturnCodeMap.put("UnauthorizedOperation","3004");
                ReturnCodeMap.put("UnloginUser","3005");
                ReturnCodeMap.put("UnexistedUsername","4001");
                ReturnCodeMap.put("UnexistedDommodity","4002");
                ReturnCodeMap.put("UnexistedOrder","4003");
                ReturnCodeMap.put("UnexistedPicture","4004");
                ReturnCodeMap.put("LostPicture","4005");
                ReturnCodeMap.put("DuplicatedUsername","5001");
                ReturnCodeMap.put("DuplicatedEmail","5002");
                ReturnCodeMap.put("DuplicatedSubscription","5003");
                ReturnCodeMap.put("IllegalDommoditylInfo","6001");
                ReturnCodeMap.put("CantBuyOwnDommodity","6002");
                ReturnCodeMap.put("CantCancelUnprocessingOrder","6003");
                ReturnCodeMap.put("CantCancelOrderByCreator","6004");
                ReturnCodeMap.put("CantConfirmCanceledOrder","6005");
                ReturnCodeMap.put("IncorrectLoginInfo","6006");
                ReturnCodeMap.put("CantUploadEmptyPicture","6007");
                ReturnCodeMap.put("UnknownReason","7000");
    }

    public static Map<String,String> getReturnCodeMap(){
        return instance.ReturnCodeMap;
    }

}

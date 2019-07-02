package com.hello.controller;

import com.hello.model.Order;

import com.hello.model.ReturnCode;
import com.hello.model.User;
import com.hello.service.IDommodityService;
import com.hello.service.IOrderService;
import com.hello.service.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * author Pei Jiyuan
 * datetime 2019/4/27
 * desc
 */

@Controller
@RequestMapping("/order")
public class OrderController {
    @Resource
    @Autowired
    private IUserService userService;
    @Resource
    @Autowired
    private IDommodityService dommodityService;
    @Resource
    @Autowired
    private IOrderService orderService;

    //private Map<String,String> ReturnCodeMap = new UserController().ReturnCodeMap;

    public Map<String,String> ReturnCodeMap = ReturnCode.getReturnCodeMap();


    @RequestMapping ("create")
    @ResponseBody
    public String create(@RequestParam long temporaryid, @RequestParam String type,
                         @RequestParam long dommodityid,@RequestParam long number){
        JSONObject jsonObject = new JSONObject();

        /** login verify */
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        int code = this.orderService.create(user,type,dommodityid,number);
        switch (code) {
            case -2:
                jsonObject.put("returncode", ReturnCodeMap.get("UnexistedDommodity"));
                break;
            case -3:
                jsonObject.put("returncode", ReturnCodeMap.get("CantBuyOwnDommodity"));
                break;
            default:
                jsonObject.put("returncode", ReturnCodeMap.get("Success"));
                jsonObject.put("dommodityid",String.valueOf(code));
        }
        return jsonObject.toString();
    }

    @RequestMapping("cancel")//TODO change to "remove"
    @ResponseBody
    public String cancel(@RequestParam long temporaryid,@RequestParam long orderid){
        JSONObject jsonObject = new JSONObject();

        /** login verify */
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        int code = this.orderService.cancel(user,orderid);
        switch (code) {
            case 0:
                jsonObject.put("returncode", ReturnCodeMap.get("Success"));
                break;
            case -2:
                jsonObject.put("returncode", ReturnCodeMap.get("UnexistedOrder"));
                break;
            case -3:
                jsonObject.put("returncode", ReturnCodeMap.get("UnauthorizedOperation"));
                break;
            case -4:
                jsonObject.put("returncode",ReturnCodeMap.get("CantCancelUnprocessingOrder"));
                break;
            default:
                jsonObject.put("returncode", ReturnCodeMap.get("UnknownReason"));
        }
        return jsonObject.toString();
    }

    @RequestMapping("undocancel")
    @ResponseBody
    public String undocancel(@RequestParam long temporaryid,@RequestParam long orderid){

        /** login verify */
        JSONObject jsonObject = new JSONObject();
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        int code = this.orderService.undocancel(user,orderid);
        switch (code) {
            case 0:
                jsonObject.put("returncode", ReturnCodeMap.get("Success"));
                break;
            case -2:
                jsonObject.put("returncode", ReturnCodeMap.get("UnexistedOrder"));
                break;
            case -3:
                jsonObject.put("returncode", ReturnCodeMap.get("UnauthorizedOperation"));
                break;
            case -4:
                jsonObject.put("returncode",ReturnCodeMap.get("CantCancelUnprocessingOrder"));
                break;
            default:
                jsonObject.put("returncode", ReturnCodeMap.get("UnknownReason"));
        }
        return jsonObject.toString();
    }

    @RequestMapping("confirm")
    @ResponseBody
    public String confirm(@RequestParam long temporaryid,@RequestParam long orderid){

        /** login verify */
        JSONObject jsonObject = new JSONObject();
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        int code = this.orderService.confirm(user,orderid);
        switch (code) {
            case 0:
                jsonObject.put("returncode", ReturnCodeMap.get("Success"));
                break;
            case -2:
                jsonObject.put("returncode", ReturnCodeMap.get("UnexistedOrder"));
                break;
            case -3:
                jsonObject.put("returncode", ReturnCodeMap.get("UnauthorizedOperation"));
                break;
            case -4:
                jsonObject.put("returncode",ReturnCodeMap.get("CantCancelOrderByCreator"));
                break;
            case -5:
                jsonObject.put("returncode",ReturnCodeMap.get("CantConfirmCanceledOrder"));
                break;
            default:
                jsonObject.put("returncode", ReturnCodeMap.get("UnknownReason"));
        }
        return jsonObject.toString();
    }

    @RequestMapping("personalSelect")
    @ResponseBody
    public String personalSelect(@RequestParam long temporaryid){

        /** login verify */
        JSONObject jsonObject = new JSONObject();
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        Order[] orders = this.orderService.findall(user);
        if(orders.length == 0){
            jsonObject.put("returncode",ReturnCodeMap.get("ZeroResultNumber"));
            return jsonObject.toString();
        }
        jsonObject.put("returncode",ReturnCodeMap.get("Success"));
        JSONArray orderJSONArr = new JSONArray();
        for(int i = 0; i<orders.length; i++){
            if(orders[i]==null)break;
            String buyername = userService.findUser(orders[i].getBuyerid()).getUsername();
            String sellername = userService.findUser(orders[i].getSellerid()).getUsername();
            orderJSONArr.add(orders[i].toString(buyername,sellername));
        }
        jsonObject.put("orders",orderJSONArr);
        return jsonObject.toString();
    }

    @RequestMapping("getorder")
    @ResponseBody
    public String getorder(@RequestParam long temporaryid, @RequestParam long orderid){

        /** login verify */
        JSONObject jsonObject = new JSONObject();
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        Order order = this.orderService.getorder(user,orderid);
        if(order == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnexistedOrder"));
            return jsonObject.toString();
        }
        if(order.getBuyerid() == -1){
            jsonObject.put("returncode",ReturnCodeMap.get("UnauthorizedOperation"));
            return jsonObject.toString();
        }
        String result = "";
        String buyername = userService.findUser(order.getBuyerid()).getUsername();
        String sellername = userService.findUser(order.getSellerid()).getUsername();
        result+=order.toString(buyername,sellername);
        jsonObject.put("returncode",ReturnCodeMap.get("Success"));
        jsonObject.put("orders",result);
        return jsonObject.toString();
    }

}

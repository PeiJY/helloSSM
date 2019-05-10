package com.hello.controller;

import com.hello.model.Order;

import com.hello.service.IDommodityService;
import com.hello.service.IOrderService;
import com.hello.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * authod Pei Jiyuan
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


    @RequestMapping ("create")
    @ResponseBody
    public String create(@RequestParam long temporaryid,
                      @RequestParam String type,
                         @RequestParam long dommodityid,@RequestParam long number){
        long code = this.orderService.create(temporaryid,type,dommodityid,number);
        if(code >0)return "{\"returncode\":\"200\"}";
        else return "{\"returncode\":\"201\",\"reasoncode\":"+String.valueOf(code)+"}";
    }

    @RequestMapping("cancel")
    @ResponseBody
    public String cancel(@RequestParam long temporaryid,@RequestParam long orderid){
        int code = this.orderService.cancel(temporaryid,orderid);
        if(code >0)return "{\"returncode\":\"200\"}";
        else return "{\"returncode\":\"201\"}";
    }

    @RequestMapping("undocancel")
    @ResponseBody
    public String undocancel(@RequestParam long temporaryid,@RequestParam long orderid){
        int code = this.orderService.undocancel(temporaryid,orderid);
        if(code >0)return "{\"returncode\":\"200\"}";
        else return "{\"returncode\":\"201\"}";
    }

    @RequestMapping("confirm")
    @ResponseBody
    public String confirm(@RequestParam long temporaryid,@RequestParam long orderid){
        int code = this.orderService.confirm(temporaryid,orderid);
        if(code >0)return "{\"returncode\":\"200\"}";
        else return "{\"returncode\":\"201\"}";
    }

    @RequestMapping("findall")
    @ResponseBody
    public String findall(@RequestParam long temporaryid){
        Order[] orders = this.orderService.findall(temporaryid);
        String result =  "{\"returncode\":\"200\",\"orders\":[";
        if(orders.length==0)return "{\"returncode\":\"201\"}";
        String dou = "";
        for(int i=0;i<orders.length;i++){
            if(orders[i]==null)break;
            String buyername = userService.findUser(orders[i].getBuyerid()).getUsername();
            String sellername = userService.findUser(orders[i].getSellerid()).getUsername();
            result+=dou+orders[i].returnMassage(buyername,sellername);
            dou = ",";
        }
        result +="]}";
        return result;
    }



    @RequestMapping("getorder")
    @ResponseBody
    public String getorder(@RequestParam long temporaryid,@RequestParam long orderid){
        Order order = this.orderService.getorder(temporaryid,orderid);
        if(order==null)return "{\"returncode\":\"201\"}";//用户不存在，订单不存在，订单不属于用户
        String result =  "{\"returncode\":\"200\",\"orders\":";
        String buyername = userService.findUser(order.getBuyerid()).getUsername();
        String sellername = userService.findUser(order.getSellerid()).getUsername();
        result+=order.returnMassage(buyername,sellername);
        result+="}";
        return result;
    }

}

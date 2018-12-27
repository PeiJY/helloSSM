package com.hello.controller;


import com.hello.model.Dommodity;
import com.hello.model.DommodityTpye;
import com.hello.model.Status;
import com.hello.model.User;
import com.hello.service.IDommodityService;
import com.hello.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.Date;


@Controller
@RequestMapping("/dommodity")
public class DommodityController {
    @Resource
    @Autowired
    private IDommodityService dommodityService;
    @Autowired
    private IUserService userService;


    @RequestMapping ("createDommodity")
    @ResponseBody
    public String create(@RequestParam String temporaryid,@RequestParam String name,
                         @RequestParam String description,@RequestParam String paytype,
                         @RequestParam String status, @RequestParam String type,
                         @RequestParam String putawaytime, @RequestParam String availabletime,
                         @RequestParam String price,@RequestParam String address,@RequestParam String operation) {
        long ltemporaryid = Long.parseLong(temporaryid);
        long lprice = Long.parseLong(price);

        String[] tlt = type.split(",");
        DommodityTpye[] dtype = new DommodityTpye[tlt.length];
        for(int i=0;i<tlt.length;i++){
            dtype[i]=DommodityTpye.valueOf(tlt[i]);
        }
        Status dstatus = Status.valueOf(status);
        System.out.println("Successfully create");
        long result = this.dommodityService.insertDommodity(name, description,ltemporaryid, dstatus,paytype, dtype, putawaytime, availabletime,lprice,address,operation);
        if(result >= 0) return"{\"returncode\":\"200\",\"dommodityid\":\""+String.valueOf(result)+"\"}";
        else return "{\"returncode\":\"201\"}";
    }

    @RequestMapping ("statusChange")
    @ResponseBody
    public String statusChange(@RequestParam long dommodityid,@RequestParam long temporaryid,@RequestParam String status) {
        Status s = Status.valueOf(status);
        if(temporaryid==0)return "{\"returncode\":\"201\"}";
        System.out.println("disable");
        int result = this.dommodityService.statusChange(dommodityid,temporaryid,s);
        switch (result){
            case 0: return "{\"returncode\":\"200\"}";
            default:return "{\"returncode\":\"201\"}";
        }
    }

    @RequestMapping ("report")
    @ResponseBody
    public String report(@RequestParam long dommodityid,@RequestParam long temporaryid,@RequestParam String reason) {
        if(temporaryid==0)return "{\"returncode\":\"201\"}";
        int result = this.dommodityService.report(dommodityid,temporaryid,reason);
        if(result >=0)return "{\"returncode\":\"200\"}";
        else return "{\"returncode\":\"201\"}";

    }

    @RequestMapping ("selectAll")
    @ResponseBody
    public String selectAll(){
        Object[][] all = this.dommodityService.selectAll();
        System.out.println(all.length);
        String result = "";
        String dou = "";
        if(all.length==0)return"{\"returncode\":\"201\"}";
        result += "{\"returncode\":\"200\",";
        result += "\"dommoditynumber\":\"";
        result += String.valueOf(all.length) + "\",\"dommoditylist\":[";
        for(int i=0;i<all.length;i++){
            result += dou+((Dommodity)all[i][0]).Message(((User)all[i][1]).getUsername());
            //result += dou+sendgenerater.sendcommodityGenerate((Dommodity)all[i][0]).toString();
            dou=",";
        }
        result += "]}";
        return result;
    }

    @RequestMapping ("personalSelect")
    @ResponseBody
    public String personalDommodity(@RequestParam long temporaryid){
        String result="";
        User user = userService.findUserByTemporaryid(temporaryid);
        String conditions = "where ownerid =" + String.valueOf(user.getId());
        result+=conditionalSelete(conditions,"","");
        return result;
    }


    @RequestMapping ("individualSelect")
    @ResponseBody
    public String individualSelect(@RequestParam String username){
        String result="";
        User user = userService.findUserByUsername(username);
        long userid = user.getId();
        String conditions = "where ownerid =" + String.valueOf(user.getId()+" and status = \"SELLING\"");
        result+=conditionalSelete(conditions,"order by putawaytime desc","");
        return result;
    }

    @RequestMapping ("conditionalSelect")
    @ResponseBody
    public String conditionalSelete(@RequestParam String conditions,@RequestParam String orders,@RequestParam String number){
        Object[][] all = this.dommodityService.conditionalSelete(conditions,orders,number);
        System.out.println(all.length);
        String result = "";
        String dou = "";
        if(all.length==0)return"{\"returncode\":\"200\",\"dommoditynumber\":\"0\"}";
        result += "{\"returncode\":\"200\",";
        result += "\"dommoditynumber\":\"";
        result += String.valueOf(all.length) + "\",\"dommoditylist\":[";
        for(int i=0;i<all.length;i++){
            result += dou+((Dommodity)all[i][0]).Message(((User)all[i][1]).getUsername());
            //result += dou+sendgenerater.sendcommodityGenerate((Dommodity)all[i][0]).toString();
            dou=",";
        }
        result += "]}";
        return result;
    }

    @RequestMapping ("modifyDommodity")
    @ResponseBody
    public String modify(@RequestParam String temporaryid,@RequestParam String name,
                         @RequestParam String description,
                         @RequestParam String status, @RequestParam String type, @RequestParam String paytype,
                         @RequestParam String putawaytime, @RequestParam String availabletime,@RequestParam String price,
                         @RequestParam String address,@RequestParam String operation) {
        long ltemporaryid = Long.parseLong(temporaryid);
        long lprice = Long.parseLong(price);

        String[] tlt = type.split(",");
        DommodityTpye[] dtype = new DommodityTpye[tlt.length];
        for(int i=0;i<tlt.length;i++){
            dtype[i]=DommodityTpye.valueOf(tlt[i]);
        }
        //DommodityTpye dtype = DommodityTpye.valueOf(type);
        Status dstatus = Status.valueOf(status);
        System.out.println("Successfully modify receive");
        int result = this.dommodityService.modityDommodity(name, description,ltemporaryid, dstatus, paytype,dtype, putawaytime, availabletime,lprice,address,operation);
        if(result ==0) return"{\"returncode\":\"201\"}";
        else return "{\"returncode\":\"200\"}";
    }

    @RequestMapping ("subscribe")
    @ResponseBody
    public String subscribe(@RequestParam long dommodityid,@RequestParam long temporaryid){
        if(temporaryid==0)return "{\"returncode\":\"201\"}";
        Date now = new Date( );
        int result = this.dommodityService.subscribe(dommodityid,temporaryid,now.toString());
        switch (result){
            case 0: return "{\"returncode\":\"200\"}";
            default:return "{\"returncode\":\"201\"}";
        }

    }
}
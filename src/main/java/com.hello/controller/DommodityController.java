package com.hello.controller;


import com.hello.model.*;
import com.hello.service.IDommodityService;
import com.hello.service.ISearchService;
import com.hello.service.IUserService;
import com.hello.service.impl.SearchServiceImpl;
import com.hello.service.impl.Word2VEC;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.json.JsonObject;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import static java.lang.Integer.max;

/**
 * authod Pei Jiyuan
 * datetime 2019/4/28
 * desc
 */

@Controller
@RequestMapping("/dommodity")
public class DommodityController {
    @Resource
    @Autowired
    private IDommodityService dommodityService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ISearchService searchService ;

    static int resultNum = 10;
    static double mimScore = 0.1;

    // TODO add load mode interface for administer

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

    @RequestMapping ("getAllType")
    @ResponseBody
    public String getAllType() {
        String result = "{\"returncode\":\"200\",\"types\":[";
        String dou = "";
        for (DommodityTpye e : DommodityTpye.values()) {
           result += dou + "\"" +e.toString() +"\"";
           dou =",";
        }
        result += "]}";
        return result;
    }
/*
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
*/
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
        // TODO add search keyword into word2vec model
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

    /**
     * 根据已有的woed2vec训练结果，对搜索的关键词进行语义上的相似推荐.
     *
     * @param queryWord 查询的关键词.
     * @return 自动补全，自动纠错，相关和相似搜素推荐.
     */
    @RequestMapping ("searchRecommend")
    @ResponseBody
    public Object searchRecom(@RequestParam String queryWord){
        JSONObject jsonObject = new JSONObject();
        String complete = searchService.autoComplete(queryWord,mimScore);
        String correct = searchService.correct(queryWord,mimScore,queryWord.length()-1);
        String similar = "{";
        String relative = "{";
        String dou = "";


        Set<WordEntry> similaySet = searchService.findTopCloseWithSameCharLimit(queryWord,resultNum,max(queryWord.length()-3,0),mimScore);
        Set<WordEntry> relativeSet =searchService.findTopClose(queryWord,resultNum,mimScore);

        Iterator<WordEntry> simIt = similaySet.iterator();
        Iterator<WordEntry> relaIt = relativeSet.iterator();
        int i=0;
        while(simIt.hasNext()){
            similar = similar +dou+ simIt.next().getName();
            dou = ",";
        }
        similar = similar+"}";
        dou = "";
        while(relaIt.hasNext()){
            relative = dou + relative + relaIt.next().getName();
            dou = ",";
        }
        relative = relative + "}";
        jsonObject.put("returncode","200");
        jsonObject.put("correct",correct);
        jsonObject.put("complete",complete);
        jsonObject.put("similar",similar);
        jsonObject.put("relative",relative);
        jsonObject.put("mapsize",String.valueOf(searchService.getMapSize()));
        return jsonObject;
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
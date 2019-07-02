package com.hello.controller;


import com.hello.model.*;
import com.hello.service.IDommodityService;
import com.hello.service.ISearchService;
import com.hello.service.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.*;

/**
 * author Pei Jiyuan
 * datetime 2019/6/27
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
    static double minScore = 0.1;

    //private Map<String,String> ReturnCodeMap = new UserController().ReturnCodeMap;

    public Map<String,String> ReturnCodeMap = ReturnCode.getReturnCodeMap();

    // TODO add load mode interface for administer

    @RequestMapping ("createDommodity")
    @ResponseBody
    public String create(@RequestParam String temporaryid,@RequestParam String name,
                         @RequestParam String description,@RequestParam String paytype,
                         @RequestParam String status, @RequestParam String type,
                         @RequestParam String putawaytime, @RequestParam String availabletime,
                         @RequestParam String price,@RequestParam String address,@RequestParam String operation) {

        /** convert all String type data into corresponding type */
        long ltemporaryid = Long.parseLong(temporaryid);
        JSONObject jsonObject = new JSONObject();

        /** login verify */
        User user = userService.findUserByTemporaryid(ltemporaryid);
        if(ltemporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        long lprice = Long.parseLong(price);
        String[] typeArr = type.split(",");
        DommodityTpye[] dtype = new DommodityTpye[typeArr.length];
        for(int i = 0; i < typeArr.length; i ++){
            dtype[i] = DommodityTpye.valueOf(typeArr[i]);
        }
        Status dstatus = Status.valueOf(status);

        /** create dommodity */
        long result = this.dommodityService.insertDommodity(name, description, user, dstatus, paytype,
                dtype, putawaytime, availabletime,lprice,address,operation);
        if(result >= 0) {
            jsonObject.put("returncode",ReturnCodeMap.get("Success"));
            jsonObject.put("dommodityid",String.valueOf(result));
            return jsonObject.toString();
        }
        if(result == -2) {
            jsonObject.put("returncode",ReturnCodeMap.get("IllegalDommoditylInfo"));
            return jsonObject.toString();
        }
        jsonObject.put("returncode",ReturnCodeMap.get("UnknownReason"));
        return jsonObject.toString();
    }

    @RequestMapping ("statusChange")
    @ResponseBody
    public String statusChange(@RequestParam long dommodityid,
                               @RequestParam long temporaryid,
                               @RequestParam String status) {
        Status s = Status.valueOf(status);
        JSONObject jsonObject = new JSONObject();

        /** login verify */
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        int result = this.dommodityService.statusChange(dommodityid,user,s);
        if(result == -2){
            jsonObject.put("returncode",ReturnCodeMap.get("UnexistedDommodity"));
            return jsonObject.toString();
        }
        if(result == -3){
            jsonObject.put("returncode",ReturnCodeMap.get("UnauthorizedOperation"));
            return jsonObject.toString();
        }
        jsonObject.put("returncode",ReturnCodeMap.get("UnknownReason"));
        return jsonObject.toString();
    }

    @RequestMapping ("report")
    @ResponseBody
    public String report(@RequestParam long dommodityid,@RequestParam long temporaryid,@RequestParam String reason) {
        JSONObject jsonObject = new JSONObject();

        /** login verify */
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        int result = this.dommodityService.report(dommodityid,user,reason);
        if(result >= 0){
            jsonObject.put("returncode",ReturnCodeMap.get("Success"));
            return jsonObject.toString();
        }
        if(result == -2){
            jsonObject.put("returncode",ReturnCodeMap.get("UnexistedDommodity"));
            return jsonObject.toString();
        }
        jsonObject.put("returncode",ReturnCodeMap.get("UnknownReason"));
        return jsonObject.toString();
    }

    @RequestMapping ("getAllType")
    @ResponseBody
    public String getAllType() {

        JSONArray allType = new JSONArray();
        for (DommodityTpye e : DommodityTpye.values()) {
            allType.add(e.toString());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("returncode",ReturnCodeMap.get("Success"));
        jsonObject.put("types",allType);
        return jsonObject.toString();
    }

    @RequestMapping ("personalSelect")
    @ResponseBody
    public String personalSelect(@RequestParam long temporaryid){
        String result = "";
        User user = userService.findUserByTemporaryid(temporaryid);
        JSONObject jsonObject = new JSONObject();
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }
        String conditions = "where ownerid =" + String.valueOf(user.getId());
        result += conditionalSelete(conditions,"","");
        return result;
    }


    @RequestMapping ("individualSelect")
    @ResponseBody
    public String personalSelectSellig(@RequestParam String username){
        String result="";
        User user = userService.findLinkedUserByUsername(username);
        JSONObject jsonObject = new JSONObject();
        if(user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnexistedUsername"));
            return jsonObject.toString();
        }
        String conditions = "where ownerid =" + String.valueOf(user.getId()+" and status = \"SELLING\"");
        result += conditionalSelete(conditions,"order by putawaytime desc","");
        return result;
    }

    @RequestMapping ("conditionalSelect")
    @ResponseBody
    public String conditionalSelete(@RequestParam String conditions,
                                    @RequestParam String orders,
                                    @RequestParam String number){
        // TODO add search keyword into word2vec model
        Object[][] all = this.dommodityService.conditionalSelete(conditions,orders,number);
        JSONArray allDommodityJSONArr = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        if(all == null || all.length == 0){
            jsonObject.put("returncode",ReturnCodeMap.get("Success"));
            jsonObject.put("dommoditynumber","0");
            return jsonObject.toString();
        }
        for(int i=0;i<all.length;i++){
            allDommodityJSONArr.add(((Dommodity)all[i][0]).toString(((User)all[i][1]).getUsername()));
        }
        jsonObject.put("returncode",ReturnCodeMap.get("Success"));
        jsonObject.put("dommoditynomber",String.valueOf(all.length));
        jsonObject.put("dommoditylist",allDommodityJSONArr);
        return jsonObject.toString();
    }

    /**
     * generate the searching recommendation (relative, similar, autocorrect, autocomplete) for the query key word,
     * according the training result of w2v and the tiretree of all existed words.
     *
     * @param queryWord the query key word
     * @return 4 searching recommendation (relative, similar, autocorrect, autocomplete)
     */
    //TODO add word frequency into calculatioon
    @RequestMapping ("searchRecommend")
    @ResponseBody
    public Object searchRecom(@RequestParam String queryWord){


        Date date = new Date();// used to calculate time cost
        long time1 = date.getTime();

        System.out.println("---------- searchRecommend, queryWord is "+queryWord+" , time stamp is "+ String.valueOf(time1) + " ----------");
        String correct = "";
        JSONArray similarJSONArr = new JSONArray();
        JSONArray relativeJSONArr = new JSONArray();
        String complete = "";
        if(!searchService.exist(queryWord)) {// if the queryWord is not exist in the lexicon, just autoCorrect
            correct = searchService.autoCorrect(queryWord);
            System.out.println("the result of correct is " + correct);
        }
        else{ // means that the queryWord is existed in lexicon
            Set<WordEntry> closestWordSet = searchService.findClosestWordSet(queryWord, resultNum);
            date = new Date();
            long time2 = date.getTime();
            System.out.printf("time cost on calculResultSet: %d\n",time2-time1);
            System.out.println("key word is in the lexicon");

            complete = searchService.autoComplete(queryWord);
            Set<WordEntry> similaySet = searchService.filterClosestWordsWithEditDistanceLimit
                    (closestWordSet, queryWord, 5, minScore, queryWord.length()/2);
            Set<WordEntry> relativeSet =searchService.filterClosestWords(closestWordSet, queryWord,5, minScore);
            Iterator<WordEntry> simIt = similaySet.iterator();
            Iterator<WordEntry> relaIt = relativeSet.iterator();
            while(simIt.hasNext()){
                similarJSONArr.add(simIt.next().getName());
            }
            while(relaIt.hasNext()){
                relativeJSONArr.add(relaIt.next().getName());
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("returncode",ReturnCodeMap.get("Success"));
        jsonObject.put("correct",correct);
        jsonObject.put("complete",complete);
        jsonObject.put("similar",similarJSONArr);
        jsonObject.put("relative",relativeJSONArr);
        System.out.println("-------------------------------------------------------------------------------------");
        return jsonObject;
    }

    @RequestMapping ("modifyDommodity")
    @ResponseBody
    public String modify(@RequestParam String temporaryid,@RequestParam String name,
                         @RequestParam String description, @RequestParam String status,
                         @RequestParam String type, @RequestParam String paytype,
                         @RequestParam String putawaytime, @RequestParam String availabletime,
                         @RequestParam String price, @RequestParam String address,@RequestParam String operation) {
        JSONObject jsonObject = new JSONObject();

        /** login verify */
        long ltemporaryid = Long.parseLong(temporaryid);
        User user = userService.findUserByTemporaryid(ltemporaryid);
        if(ltemporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        /** type convert */
        long lprice = Long.parseLong(price);
        String[] tlt = type.split(",");
        DommodityTpye[] dtype = new DommodityTpye[tlt.length];
        for(int i=0;i<tlt.length;i++){
            dtype[i]=DommodityTpye.valueOf(tlt[i]);
        }
        Status dstatus = Status.valueOf(status);

        int result = this.dommodityService.modityDommodity(name, description,user, dstatus, paytype,dtype, putawaytime, availabletime,lprice,address,operation);

        if(result == 0){
            jsonObject.put("returncode",ReturnCodeMap.get("Success"));
            return jsonObject.toString();
        }
        if(result == -1){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }
        if(result == -2){
            jsonObject.put("returncode",ReturnCodeMap.get("UnexistedDommodity"));
            return jsonObject.toString();
        }
        jsonObject.put("returncode",ReturnCodeMap.get("UnknownReason"));
        return jsonObject.toString();
    }

    @RequestMapping ("subscribe")
    @ResponseBody
    public String subscribe(@RequestParam long dommodityid,@RequestParam long temporaryid){
        JSONObject jsonObject = new JSONObject();

        /** login verify */
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        Date now = new Date( );
        int result = this.dommodityService.subscribe(dommodityid, user, now.toString());
        if(result >= 0){
            jsonObject.put("returncode",ReturnCodeMap.get("Success"));
            return jsonObject.toString();
        }
        if(result == -1){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }
        if(result == -2){
            jsonObject.put("returncode",ReturnCodeMap.get("UnexistedDommodity"));
            return jsonObject.toString();
        }
        jsonObject.put("returncode",ReturnCodeMap.get("UnknownReason"));
        return jsonObject.toString();
    }
}
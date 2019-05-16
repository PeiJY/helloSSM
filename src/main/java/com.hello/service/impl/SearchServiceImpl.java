package com.hello.service.impl;

import com.hello.model.WordEntry;
import com.hello.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * authod Pei Jiyuan
 * datetime 2019/4/28
 *
 * function implement of intellgient seaching, based on word2vec word distance
 */

@Service
public class SearchServiceImpl implements ISearchService {

    //private String modelPath = "G:\\DesktopDir\\course\\Junior2\\Project\\word2vec_java_test\\src\\vectors.bin";
    //private String modelPath = "F:\\text\\helloSSM\\src\\main\\java\\com.hello\\service\\vectors.bin";
    private String modelPath = "G:\\DesktopDir\\Projects\\SUSTechFM\\vector";

    //private String modelPath ="/var/lib/tomcat8/vectors.bin";
    private Word2VEC w2v= new Word2VEC(modelPath);


    @Override
    public int getMapSize(){
        return w2v.getMapSize();
    }


    /**
     * reload the trained model of word2vec
     * @param path path of the result model of trainning by word2vec
     */
    @Override
    public void loadModel(String path){
        try {
            w2v.loadTxtModel(path);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Set<WordEntry> calculDisSet(String queryWord, int top){

        /** topNsize limit the size of result set */
        int topNsize = w2v.getTopNSize();
        w2v.setTopNSize(top);

        /** calculate the result set by word2vec */
        Set<WordEntry> calculResultSet = w2v.distance(queryWord);
        //System.out.printf("result size is %d\n",calculResultSet.size());

        /** recover to the origin topNsize */
        w2v.setTopNSize(topNsize);
        return calculResultSet;

    }


    /**
     * auto correct the query keyword, the corrected result is the lowest dist word.
     *
     * @param queryWord the keyword of query.
     * @return the correcting result. If there is no suitable result, return "".
     */
    @Override
    public String correct (Set<WordEntry> calculResultSet ,String queryWord ,double scoreLimit,int editDistanceLimit ){// 暂时不考虑字符匹配和出现频率
        //System.out.println(w2v.getWordMap().toString());
        Set<WordEntry> resultSet = find(calculResultSet,queryWord,scoreLimit,2,editDistanceLimit);
        Iterator<WordEntry> it = resultSet.iterator();
        if(it.hasNext()) {
            WordEntry a = it.next();
            System.out.println(a.getScore());
            return a.getName();
        }
        return "";
    }

    /**
     * find out the top N closest word of the keyword, which all score bigger than socreLimit
     *
     * @param queryWord the keyword of query
     * @param scoreLimit the lowest score
     * @return a set of WordEntry, which is each result word and its score
     */
    @Override
    public Set<WordEntry> findTopClose(Set<WordEntry> calculResultSet ,String queryWord,int num, double scoreLimit){
        Set<WordEntry> resultSet = find(calculResultSet,queryWord,scoreLimit,num,999);
        return resultSet;
    }

    /**
     * find out the top N closest word of the keyword,
     * which all score bigger than socreLimit and
     * edit distance smaller than editDistanceLimit
     *
     * @param queryWord the keyword of query
     * @param editDistanceLimit maximum edit distance
     * @param scoreLimit the lowest score
     * @return a set of WordEntry, which is each result word and its score
     */
    @Override
    public Set<WordEntry> findTopCloseWithSameCharLimit(Set<WordEntry> calculResultSet  ,String queryWord, int num,int editDistanceLimit , double scoreLimit){
        Set<WordEntry> resultSet = find(calculResultSet,queryWord,scoreLimit,num,editDistanceLimit);
        return resultSet;
    }

    /**
     * auto complete the keyword, the complete is the word which is most close to the keyword and socre large than socrelimit
     *
     * @param queryWord the keyword of query
     * @param scoreLimit the lowest score
     * @return a set of WordEntry, which is each result word and its score
     */
    @Override
    public String autoComplete( Set<WordEntry> calculResultSet,String queryWord,double scoreLimit,int editDistanceLimit){
        // TODO dictionary tree for string match

        Set<WordEntry> resultSet = find(calculResultSet,queryWord,scoreLimit,2,editDistanceLimit);
        Iterator<WordEntry> it = resultSet.iterator();
        String result = "";
        if(it.hasNext())
            result  = it.next().getName();
        return result;
    }


    /**
     * find the close words under conditions
     *
     * @param queryWord the keyword of query
     * @param top 'N' the limit of number of result
     * @param editDistanceLimit maximum edit distance
     * @param scoreLimit the lowest score(low socre means high vector distance)
     * @return a set of WordEntry, which is each result word and its score
     */
    private Set<WordEntry> find (Set<WordEntry> calculResultSet , String queryWord,double scoreLimit, int top, int editDistanceLimit) {

        System.out.printf("queryWord is %s\n",queryWord);
        int count = 0;
        /** filtration according scoreLimit, editDistanceLimit */
        Set<WordEntry> resultSet = new HashSet<>();
        Iterator<WordEntry> it = calculResultSet.iterator();

        while(it.hasNext() && count < top){
            WordEntry entry = it.next();
            //System.out.printf("item is %s\n",entry.getName());
            if(entry.getScore()<scoreLimit)
                continue;
            if(calculEditDis(queryWord,entry.getName())>editDistanceLimit)
                continue;
            resultSet.add(entry);
            System.out.printf("result add %s\n",entry.getName());
            count += 1;
        }
        return resultSet;
    }


    /**
     * find the min edit distance of 2 strings.
     *
     *     作者：qq_42151769
     *     来源：CSDN
     *     原文：https://blog.csdn.net/qq_42151769/article/details/84134308
     *
     * @param word1 string1
     * @param word2 string2
     * @return the last element of the metrix, which is the min edic distance
     */
    public int calculEditDis(String word1, String word2) {

        if (word1.length() == 0 || word2.length() == 0) {
            return word1.length() == 0 ? word2.length() : word1.length();
        }
        //初始化矩阵
        int[][] arr = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 0; i <= word1.length(); i++) {
            arr[i][0] = i;
        }
        for (int j = 0; j <= word2.length(); j++) {
            arr[0][j] = j;
        }

        for (int i = 1; i <= word1.length(); i++) {
            for (int j = 1; j <= word2.length(); j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    //相等时temp为0
                    arr[i][j] = arr[i - 1][j - 1];
                } else {
                    //不相等时,temp为1
                    int replace = arr[i - 1][j - 1] + 1;
                    int insert = arr[i - 1][j] + 1;
                    int delete = arr[i][j - 1] + 1;
                    int min = Math.min(replace, insert);
                    min = Math.min(min, delete);
                    arr[i][j] = min;
                }
            }
        }
        System.out.printf("edit dis  %s %s %d\n",word1,word2,arr[word1.length()][word2.length()]);
        return arr[word1.length()][word2.length()];
    }

}

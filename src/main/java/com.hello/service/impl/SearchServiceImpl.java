package com.hello.service.impl;

import com.hello.model.WordEntry;
import com.hello.service.ISearchService;
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
    private String modelPath = "C:\\Users\\Jiyuan Pei\\Desktop\\vector";
    //private String modelPath ="/var/lib/tomcat8/vectors.bin";
    private Word2VEC w2v = new Word2VEC(modelPath);


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

    /**
     * auto correct the query keyword, the corrected result is the lowest dist word.
     *
     * @param queryWord the keyword of query.
     * @return the correcting result. If there is no suitable result, return "".
     */
    @Override
    public String correct (String queryWord ,double scoreLimit,int sameCharLimit ){// 暂时不考虑字符匹配和出现频率
        //System.out.println(w2v.getWordMap().toString());
        Set<WordEntry> resultSet = find(queryWord,scoreLimit,2,sameCharLimit);
        Iterator<WordEntry> it = resultSet.iterator();
        if(it.hasNext())
            return it.next().getName();
        return "";
    }

    /**
     * find out the top N closest word of the keyword, which all score bigger than socreLimit
     *
     * @param queryWord the keyword of query
     * @param top 'N' the limit of number of result
     * @param scoreLimit the lowest score
     * @return a set of WordEntry, which is each result word and its score
     */
    @Override
    public Set<WordEntry> findTopClose(String queryWord, int top, double scoreLimit){
        Set<WordEntry> resultSet = find(queryWord,scoreLimit,2,0);
        return resultSet;
    }

    /**
     * find out the top N closest word of the keyword,
     * which all score bigger than socreLimit and
     * edit distance smaller than sameCharLimit
     *
     * @param queryWord the keyword of query
     * @param top 'N' the limit of number of result
     * @param sameCharLimit maximum edit distance
     * @param scoreLimit the lowest score
     * @return a set of WordEntry, which is each result word and its score
     */
    @Override
    public Set<WordEntry> findTopCloseWithSameCharLimit(String queryWord, int top, int sameCharLimit , double scoreLimit){
        Set<WordEntry> resultSet = find(queryWord,scoreLimit,top,sameCharLimit);
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
    public String autoComplete(String queryWord,double scoreLimit){
        // TODO dictionary tree for string match

        Set<WordEntry> resultSet = find(queryWord,scoreLimit,2,queryWord.length());
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
     * @param sameCharLimit maximum edit distance
     * @param scoreLimit the lowest score
     * @return a set of WordEntry, which is each result word and its score
     */
    private Set<WordEntry> find (String queryWord,double scoreLimit, int top, int sameCharLimit) {
        System.out.printf("queryWord is %s\n",queryWord);
        Set<WordEntry> resultSet = new HashSet<>();
        int topNsize = w2v.getTopNSize();
        w2v.setTopNSize(top);
        Set<WordEntry> calculResultSet = w2v.distance(queryWord);
        System.out.printf("result size is %d\n",calculResultSet.size());
        Iterator<WordEntry> it = calculResultSet.iterator();
        w2v.setTopNSize(topNsize);
        while(it.hasNext()){
            WordEntry entry = it.next();
            //System.out.printf("item is %s\n",entry.getName());
            if(entry.getScore()<scoreLimit)
                break;
            if(sameCharCounter(queryWord,entry.getName())<sameCharLimit)
                break;
            resultSet.add(entry);
            System.out.printf("result add %s\n",entry.getName());
        }
        return resultSet;
    }


    /**
     * calcul the similrity of 2 words
     *
     * @param str1 word1
     * @param str2 word2
     * @return length of word1 minus the edit distance of these two words
     */
    private int sameCharCounter(String str1,String str2) {
        //计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][] dif = new int[len1 + 1][len2 + 1];
        //赋初值，步骤B。
        for (int a = 0; a <= len1; a++) {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] = a;
        }
        //计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                        dif[i - 1][j] + 1);
            }
        }
        return min(str1.length()-dif[len1][len2],str2.length()-dif[len1][len2]);
    }

    //得到最小值
    private static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }
}

package com.hello.service.impl;

import com.hello.model.WordEntry;
import com.hello.service.ISearchService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * author Pei Jiyuan
 * date 2019/6/27
 *
 * function implement of intelligent searching, based on word2vec, trietree and word edit distance
 */

@Service
public class SearchServiceImpl implements ISearchService {

    // paths for local test
    private String modelPath = "G:\\DesktopDir\\Projects\\SUSTechFM\\vector";

    // paths for running on LCH's servicer
    //private String modelPath ="/var/lib/tomcat8/vectors.bin";

    private Word2VEC w2v= new Word2VEC(modelPath);

    /** get the total number of words in the map (corpus) */
    @Override
    public int getMapSize(){
        return w2v.getMapSize();
    }

    /** find if the word exist in the corpus or not */
    @Override
    public boolean exist (String queryWord){
        return w2v.has(queryWord);
    }

    /**
     * reload the trained model of word2vec
     *
     * @param path path of the result model of training by word2vec
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
     *  find the closest N words of the key word in the corpus
     *
     * @param queryWord the key word
     * @param num N
     * @return the set of closest words and their word vectors
     */
    @Override
    public Set<WordEntry> findClosestWordSet(String queryWord, int num){

        /** backup of topNsize of origin w2c object */
        int topNsize = w2v.getTopNSize();
        w2v.setTopNSize(num);

        /** calculate the result set by word2vec */
        Set<WordEntry> calculResultSet = w2v.distance(queryWord);

        /** recover to the origin topNsize */
        w2v.setTopNSize(topNsize);

        return calculResultSet;
    }

    /**
     * auto correct the query key word.
     *
     * @param queryWord the key word of query.
     * @return the correcting result. If there is no suitable result, return the origin key word..
     */
    @Override
    public String autoCorrect (String queryWord){
        int queryWordLength = queryWord.length();
        int end = queryWordLength -1;
        boolean resultFound = false;
        String correctResult = "";
        String subQueryWord; // is all possible prefix of queryWord
        String[] similarWords;// all words which's prefix is the subQueryWord

        /**
         * From long prefix to short prefix, recurrently take the prefix substring(subQueryWord) of queryWord as the
         * key word, and search the existed word in tiretree(curpos) which's prefix is subQueryWord. As long as the
         * searching result is not empty, take the second element of searching result as the correcting result (as the
         * first element is always the subQueryWord it self)
         *
         * TODO improve the correcting logic
         */
        for(int i = 0; i < queryWordLength; i ++){
            subQueryWord = queryWord.substring(0, end - i);
            similarWords = w2v.findWordsByPrefix(subQueryWord);
            if(similarWords != null && similarWords.length > 1) {
                resultFound = true;
                correctResult = similarWords[1];// similarWords[0] is prefix it self (subQueryWord)
                break;
            }
        }
        if(resultFound) return correctResult;

        /**
         *  when there is no corresting result, return the origin queryWord, which means that it is unable to
         *  correct this word, and the client will handle this return value (origin queryWord) and show to the user
         */
        return queryWord;
    }

    /**
     * find out the closest words of the keyword in the corpus, if the results number more than a specific N, just
     * return the num N closest results.
     *
     * @param queryWord the keyword of query
     * @param scoreLimit the lowest score (represent the largest word vector distance)
     * @param num the specific number N
     * @param closestWordSet the searching result of closest word in corpus
     * @return a set of WordEntry, which is each result word and its score
     */
    @Override
    public Set<WordEntry> filterClosestWords(Set<WordEntry> closestWordSet,
                                             String queryWord,
                                             int num,
                                             double scoreLimit){
        /** to record the time cost */
        Date date = new Date();
        long time1 = date.getTime();
        Set<WordEntry> resultSet = filter(closestWordSet, queryWord, num, scoreLimit, 999);
        date = new Date();
        long time2 = date.getTime();
        System.out.printf("time cost on relative: %d\n",time2-time1);
        return resultSet;
    }

    /**
     * find out the num N closest word of the keyword, which all score bigger than socreLimit and edit distance
     * smaller than editDistanceLimit
     *
     * @param closestWordSet the searching result of closest word in corpus
     * @param queryWord the keyword of query
     * @param num the specific number N
     * @param editDistanceLimit maximum edit distance
     * @param scoreLimit the lowest score(max word vector distance allowed)
     * @return a set of WordEntry, which is each result word and its score
     */
    @Override
    public Set<WordEntry> filterClosestWordsWithEditDistanceLimit(Set<WordEntry> closestWordSet,
                                                                  String queryWord,
                                                                  int num,
                                                                  double scoreLimit,
                                                                  int editDistanceLimit){
        /** to record the time cost */
        Date date = new Date();
        long time1 = date.getTime();
        Set<WordEntry> resultSet = filter(closestWordSet, queryWord, num, scoreLimit, editDistanceLimit);
        date = new Date();
        long time2 = date.getTime();
        System.out.printf("time cost on similar: %d\n",time2-time1);
        return resultSet;
    }

    /**
     * auto complete the key word by searching in trietree
     */
    @Override
    public String autoComplete(String queryWord){
        /** to record time cost */
        Date date = new Date();
        long time1 = date.getTime();
        
        String[] words = w2v.findWordsByPrefix(queryWord);
        
        date = new Date();
        long time2 = date.getTime();
        System.out.printf("time cost on search trie tree: %d\n",time2-time1);

        System.out.printf("the number of words which has prefix as the queryWord is : %d\n",words.length);
        
        /** find the closest word */
        float minDistance = w2v.compare2(queryWord,words[0]);
        String minDistanceWord = words[0];
        float distance;
        for(int i = 1; i < words.length; i ++){
            distance = w2v.compare2(queryWord,words[i]);
            if(distance < minDistance && !queryWord.equals(words[i])){
                minDistance = distance;
                minDistanceWord = words[i];
            }
        }

        date = new Date();
        time2 = date.getTime();
        System.out.printf("time cost on completing: %d\n",time2-time1);
        return minDistanceWord;
    }

    /**
     * filter the closest words according to the constraint
     */
    private Set<WordEntry> filter (Set<WordEntry> closestWordSet,
                                   String queryWord,
                                   int num,
                                   double scoreLimit,
                                   int editDistanceLimit) {
        int count = 0;
        Set<WordEntry> resultSet = new HashSet<>();
        Iterator<WordEntry> it = closestWordSet.iterator();
        while(it.hasNext() && count < num){
            WordEntry entry = it.next();
            if(entry.getScore()<scoreLimit)
                continue;
            if(calculEditDis(queryWord,entry.getName()) > editDistanceLimit)
                continue;
            resultSet.add(entry);
            count += 1;
        }
        return resultSet;
    }

    /**
     * find the min edit distance of 2 strings.
     */
    public int calculEditDis(String word1, String word2) {
        if (word1.length() == 0 || word2.length() == 0) {
            return word1.length() == 0 ? word2.length() : word1.length();
        }
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
                    arr[i][j] = arr[i - 1][j - 1];
                } else {
                    int replace = arr[i - 1][j - 1] + 1;
                    int insert = arr[i - 1][j] + 1;
                    int delete = arr[i][j - 1] + 1;
                    int min = Math.min(replace, insert);
                    min = Math.min(min, delete);
                    arr[i][j] = min;
                }
            }
        }
        return arr[word1.length()][word2.length()];
    }
}

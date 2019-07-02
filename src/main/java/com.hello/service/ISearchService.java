package com.hello.service;

import com.hello.model.WordEntry;

import java.util.Set;

/**
 * author Pei Jiyuan
 * date 2019/6/27
 */

public interface ISearchService {

    public void loadModel(String path);

    public Set<WordEntry> findClosestWordSet(String queryWord, int top);

    public String autoCorrect (String queryWord );

    public boolean exist (String queryWord);

    public Set<WordEntry> filterClosestWords(Set<WordEntry> calculResultSet,
                                             String queryWord,
                                             int num,
                                             double scoreLimit);

    public Set<WordEntry> filterClosestWordsWithEditDistanceLimit(Set<WordEntry> calculResultSet,
                                                                  String queryWord,
                                                                  int num,
                                                                  double scoreLimit,
                                                                  int editDistanceLimit);

    public String autoComplete(String queryWord);

    public int getMapSize();
}

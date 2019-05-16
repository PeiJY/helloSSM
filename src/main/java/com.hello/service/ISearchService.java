package com.hello.service;

import com.hello.model.User;
import com.hello.model.WordEntry;

import java.util.Set;

/**
 * authod Pei Jiyuan
 * datetime 2019/4/27
 * desc
 */

public interface ISearchService {

    public void loadModel(String path);

    public Set<WordEntry> calculDisSet(String queryWord, int top);

    public String correct (Set<WordEntry> calculResultSet,String queryWord ,double scoreLimit,int editDistanceLimit );


    public Set<WordEntry> findTopClose(Set<WordEntry> calculResultSet,String queryWord, int num,double scoreLimit);


    public Set<WordEntry> findTopCloseWithSameCharLimit(Set<WordEntry> calculResultSet,String queryWord,int num,  int editDistanceLimit, double scoreLimit);


    public String autoComplete(Set<WordEntry> calculResultSet,String queryWord, double scoreLimit,int editDistanceLimit);

    public int getMapSize();
}

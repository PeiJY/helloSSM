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

    public String correct (String queryWord ,double scoreLimit,int sameCharLimit );


    public Set<WordEntry> findTopClose(String queryWord, int top, double scoreLimit);


    public Set<WordEntry> findTopCloseWithSameCharLimit(String queryWord, int top, int sameCharLimit, double scoreLimit);


    public String autoComplete(String queryWord, double scoreLimit);

    public int getMapSize();
}

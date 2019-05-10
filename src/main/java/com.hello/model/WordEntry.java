package com.hello.model;
/**
 *  clone from https://github.com/NLPchina/Word2VEC_java.git
 *
 *  date 2019/4/27
 */
public class WordEntry implements Comparable<WordEntry> {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    private String name;
    private float score;

    public WordEntry(String name, float score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return this.name + "\t" + score;
    }

    @Override
    public int compareTo(WordEntry o) {
        if (this.score < o.score) {
            return 1;
        } else {
            return -1;
        }
    }


}
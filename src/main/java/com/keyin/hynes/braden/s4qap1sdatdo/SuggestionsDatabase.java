package com.keyin.hynes.braden.s4qap1sdatdo;
import java.util.HashMap;
import java.util.Map;
public class SuggestionsDatabase {
    private Map<String,Integer> wordMap;
    public SuggestionsDatabase() {
        this.wordMap = new HashMap<String, Integer>();
    }
    public Map<String, Integer> getWordMap() {
        return wordMap;
    }
    public void setWordMap(Map<String, Integer> wordMap) {
        this.wordMap = wordMap;
    }
}
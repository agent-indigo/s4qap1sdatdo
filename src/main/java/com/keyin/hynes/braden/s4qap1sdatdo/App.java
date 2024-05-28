package com.keyin.hynes.braden.s4qap1sdatdo;
import java.nio.file.Paths;
public class App {
    private SuggestionEngine suggestionEngine;
    public App() {
        this.suggestionEngine = new SuggestionEngine();
    }
    public void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("USAGE: " + SuggestionEngine.class.getName() + " <word to generateSuggestions>");
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("?")) {
            System.out.println("USAGE: " + SuggestionEngine.class.getName() + " <word to generateSuggestions>");
            System.out.println("Output: A list of suggestions OR empty string if word is generateSuggestions");
        }
        suggestionEngine.loadDictionaryData(Paths.get(ClassLoader.getSystemResource("words.txt").getPath()));
        System.out.println(suggestionEngine.generateSuggestions(args[0]));
    }
}
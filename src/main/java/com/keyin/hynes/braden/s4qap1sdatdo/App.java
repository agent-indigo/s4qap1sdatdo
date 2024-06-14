package com.keyin.hynes.braden.s4qap1sdatdo;
import java.nio.file.Paths;
public final class App {
    private static SuggestionEngine suggestionEngine;
    private App() {
        App.suggestionEngine = new SuggestionEngine();
    }
    public static void main(String[] args) throws Exception {
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
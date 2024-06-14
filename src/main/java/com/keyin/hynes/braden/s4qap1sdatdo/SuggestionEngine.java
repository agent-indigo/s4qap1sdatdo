package com.keyin.hynes.braden.s4qap1sdatdo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
public final class SuggestionEngine {
    private SuggestionsDatabase wordSuggestionDB;
    private Stream<String> deletes;
    private Stream<String> replaces;
    private Stream<String> inserts;
    private Stream<String> transposes;
    private Stream<String> e1;
    private Stream<String> e2;
    private Stream<String> suggestions;
    private Map<String, Long> collectedSuggestions;
    public SuggestionEngine() {
        this.wordSuggestionDB = new SuggestionsDatabase();
    }
    /**
     *   Based on algorithm from http://norvig.com/spell-correct.html
     *   Specifically the second part which describes how to create a Candidate Model in order to determine a list of
     *   words that the user may be trying to input.
     */
    private Stream<String> wordEdits(final String word) {
        this.deletes = IntStream.range(0, word.length()).mapToObj(i -> word.substring(0, i) + word.substring(i + 1));
        this.replaces = IntStream.range(0, word.length()).mapToObj(i -> i).flatMap(i -> "abcdefghijklmnopqrstuvwxyz".chars().mapToObj(c -> word.substring(0, i) + (char) c + word.substring(i + 1)));
        this.inserts = IntStream.range(0, word.length() + 1).mapToObj(i -> i).flatMap(i -> "abcdefghijklmnopqrstuvwxyz".chars().mapToObj(c -> word.substring(0, i) + (char) c + word.substring(i)));
        this.transposes = IntStream.range(0, word.length() -1).mapToObj(i -> word.substring(0, i) + word.substring(i + 1, i + 2) + word.charAt(i) + word.substring(i + 2));
        return Stream.of(deletes, replaces, inserts, transposes).flatMap(x -> x);
    }
    /**
     * Look up the passed in word in the Map of words loaded from the source file.
     */
    private Stream<String> known(Stream<String> words) {
        return words.filter(word -> getWordSuggestionDB().containsKey(word));
    }
    /**
     * Load a list of words into memory from the given Path, converting all words to lower casea and file is assumed to
     * be dilimited by '\n'.
     * @param dictionaryFile the Path to the file to be loaded
     * @throws IOException a any file loading problems
     */
    public void loadDictionaryData(Path dictionaryFile) throws IOException {
        Stream.of(new String(Files.readAllBytes(dictionaryFile)).toLowerCase().split("\\n")).forEach(word -> getWordSuggestionDB().compute(word, (k, v) -> v == null ? 1 : v + 1));
    }
    /**
     * Will generate a list of suggested corrections, limited by the top 10 most likely for the given word.
     * @param word the word to find correction suggestions for
     * @return a String of words delimited by '\n' or an empty string if word is correct
     */
    public String generateSuggestions(String word) {
        if (getWordSuggestionDB().containsKey(word)) return "";
        this.e1 = known(wordEdits(word));
        this.e2 = known(wordEdits(word).map(w2 -> wordEdits(w2)).flatMap(x -> x));
        this.suggestions = Stream.concat(e1, e2);
        this.collectedSuggestions = suggestions.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return collectedSuggestions
                .keySet()
                .stream()
                .sorted(Comparator.comparing(collectedSuggestions::get)
                .reversed()
                .thenComparing(Collator.getInstance()))
                .limit(10) // limit to top 10 suggestions to keep list consumable
                .collect(Collectors.joining("\n"));
    }
    public Map<String, Integer> getWordSuggestionDB() {
        return wordSuggestionDB.getWordMap();
    }
    public void setWordSuggestionDB(SuggestionsDatabase wordSuggestionDB) {
        this.wordSuggestionDB = wordSuggestionDB;
    }
}
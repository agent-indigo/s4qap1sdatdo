package com.keyin.hynes.braden.s4qap1sdatdo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
@ExtendWith(MockitoExtension.class)
public class SuggestionEngineTest {
    private SuggestionEngine suggestionEngine;
    private Map<String,Integer> testWordMap;
    @Mock
    private SuggestionsDatabase mockSuggestionDB;
    public SuggestionEngineTest() {
        this.suggestionEngine = new SuggestionEngine();
        this.testWordMap = new HashMap<String, Integer>();
    }
    // submissions
    @Test
    public void testNonAlpha() throws Exception {
        suggestionEngine.loadDictionaryData(Paths.get(ClassLoader.getSystemResource("words.txt").getPath()));
        Assertions.assertEquals("", suggestionEngine.generateSuggestions("1234"));
    }
    @Test
    public void testNull() {
        Assertions.assertThrows(NullPointerException.class, () -> suggestionEngine.generateSuggestions(null));
    }
    @Test
    public void testPartial() throws Exception {
        suggestionEngine.loadDictionaryData(Paths.get(ClassLoader.getSystemResource("words.txt").getPath()));
        System.out.println(suggestionEngine.generateSuggestions("ello"));
        Assertions.assertTrue(suggestionEngine.generateSuggestions("ello").contains("cello"));
    }
    // givens
    @Test
    public void testTypo() throws Exception {
        suggestionEngine.loadDictionaryData(Paths.get(ClassLoader.getSystemResource("words.txt").getPath()));
        Assertions.assertTrue(suggestionEngine.generateSuggestions("hellw").contains("hello"));
    }
    @Test
    public void testCorrect() throws Exception {
        suggestionEngine.loadDictionaryData(Paths.get(ClassLoader.getSystemResource("words.txt").getPath()));
        Assertions.assertFalse(suggestionEngine.generateSuggestions("hello").contains("hello"));
    }
    @Test
    public void mockPartial() {
        testWordMap.put("test", 1);
        Mockito.when(mockSuggestionDB.getWordMap()).thenReturn(testWordMap);
        suggestionEngine.setWordSuggestionDB(mockSuggestionDB);
        Assertions.assertFalse(suggestionEngine.generateSuggestions("test").contains("test"));
        Assertions.assertTrue(suggestionEngine.generateSuggestions("tes").contains("test"));
    }
}

package ua.edu.ucu.autocomplete;

import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import ua.edu.ucu.tries.RWayTrie;
import ua.edu.ucu.tries.Trie;

/**
 *
 * @author Andrii_Rodionov
 */
public class PrefixMatchesITTest {

    private PrefixMatches pm;

    // @Before
    // public void init() {
    //     pm = new PrefixMatches(new RWayTrie());
    //     pm.load("abc", "abce", "abcd", "abcde", "abcdef");
    // }

    @Test
    public void testLoad()
    {
        PrefixMatches matcher = new PrefixMatches(new RWayTrie());
        matcher.load("Hello", "World", "This is hello world");
        matcher.load("Even more loading");
        matcher.load("OnlyOne");
        matcher.load("Multiple   spaces   and even  tabs");
        String[] expected = {"hello", "world", "this", "is", "hello", "world", "even", "more", "loading", "onlyone", "multiple", "spaces", "and", "even", "tabs"};
        //assertArrayEquals(expected, matcher.getTrie().toArray());
    }

    @Test
    public void testWordsWithPrefix_String() {
        String pref = "ab";

        Iterable<String> result = pm.wordsWithPrefix(pref);

        String[] expResult = {"abc", "abce", "abcd", "abcde", "abcdef"};

        assertThat(result, containsInAnyOrder(expResult));
    }

    @Test
    public void testWordsWithPrefix_String_and_K() {
        String pref = "abc";
        int k = 3;

        Iterable<String> result = pm.wordsWithPrefix(pref, k);

        String[] expResult = {"abc", "abce", "abcd", "abcde"};

        assertThat(result, containsInAnyOrder(expResult));
    }

}

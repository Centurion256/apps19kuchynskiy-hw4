
package ua.edu.ucu.autocomplete;

import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import ua.edu.ucu.tries.RWayTrie;
import ua.edu.ucu.tries.RWayTrie.TrieNode;
import ua.edu.ucu.tries.Trie;
import ua.edu.ucu.tries.Tuple;

public class RWayTrieTest
{
    @Test
    public void testWordsEmpty()
    {
        RWayTrie rv = new RWayTrie();
        Iterable<String> iter = rv.words();
        String[] expResult = {};
        assertThat(iter, containsInAnyOrder(expResult));
    }
    @Test
    public void testWordsIterator()
    {
        RWayTrie rv = new RWayTrie();
        Iterable<String> iterable = rv.words();
        Iterator<String> iter = iterable.iterator();
        String[] expResult = {"Hello", "World"};
        int i = 0;
        while (iter.hasNext())
        {
            assertEquals(iter.next(), expResult[i++]);
        }

    }

    @Test
    public void testWordsIteratorNoNext()
    {
        RWayTrie rv = new RWayTrie();
        Iterable<String> iterable = rv.words();
        Iterator<String> iter = iterable.iterator();
        String[] expResult = {"Hello", "World"};
        int i = 0;
        while (iter.hasNext())
        {
            assertEquals(iter.next(), expResult[i++]);
        }
        assertEquals(iter.hasNext(), false);

    }
    @Test
    public void testWordsIterable()
    {
        RWayTrie rv = new RWayTrie();
        Iterable<String> iterable = rv.words();
        String[] expResult = {"Hello", "World"};
        int i = 0;
        for (String string : iterable) 
        {
            assertEquals(string, expResult[i++]);  
        }

    }
    @Test
    public void testReadWord()
    {
        RWayTrie rv = new RWayTrie();
        rv.add(new Tuple("hello", 5));
        TrieNode ptr = rv.find("hello");
        String result = rv.readWord(ptr, 5);
        assertEquals("hello", result);
    }
    @Test
    public void testWords()
    {
        RWayTrie rv = new RWayTrie();
        rv.add(new Tuple("hello", 5));
        rv.add(new Tuple("world", 5));
        //TrieNode ptr = rv.find("hello");
        Iterable<String> result = rv.words();
        int i = 0;
        String[] expected = {"hello", "world"};
        for (String word : result)
        {
            System.out.printf("actual:%s\n", word);
            assertEquals(expected[i], word);
            i++;
        }
        //List<String> expected = new ArrayList<String>(Arrays.asList("hello", "world"));
        //assertEquals(result, containsInAnyOrder(expected));
    }
    @Test
    public void testDelete()
    {
        RWayTrie rv = new RWayTrie();
        rv.add(new Tuple("hello", 5));
        rv.add(new Tuple("world", 5));
        rv.add(new Tuple("sea", 3));
        rv.add(new Tuple("seashells", 9));
        assertTrue("sea has been added", rv.contains("sea") == true);
        rv.delete("sea");
        assertTrue("sea has been removed", rv.contains("sea") == false);
        Iterable<String> result = rv.words();
        List<String> expected = new ArrayList<String>(Arrays.asList("hello", "world", "seashells"));
        assertEquals(expected.toString(), result.toString());
    }
    @Test
    public void testPrefixWords()
    {
        RWayTrie rv = new RWayTrie();
        rv.add(new Tuple("hello", 5));
        rv.add(new Tuple("world", 5));
        rv.add(new Tuple("sea", 3));
        rv.add(new Tuple("seashells", 9));
        Iterable<String> result = rv.wordsWithPrefix("sea");
        List<String> expected = new ArrayList<>(Arrays.asList("sea", "seashells"));
        assertEquals(expected.toString(), result.toString());
    }
    @Test
    public void testNoPrefixWords()
    {
        RWayTrie rv = new RWayTrie();
        rv.add(new Tuple("hello", 5));
        rv.add(new Tuple("world", 5));
        rv.add(new Tuple("sea", 3));
        rv.add(new Tuple("seashells", 9));
        Iterable<String> result = rv.wordsWithPrefix("none");
        List<String> expected = new ArrayList<String>();
        assertEquals(expected, result);
    }

    @Test
    public void testSize()
    {
        RWayTrie rv = new RWayTrie();
        rv.add(new Tuple("hello", 5));
        rv.add(new Tuple("world", 5));
        //TrieNode ptr = rv.find("hello");
        int result = rv.size();
        assertEquals(2, result);
    }
}

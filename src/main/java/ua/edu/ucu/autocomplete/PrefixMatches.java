package ua.edu.ucu.autocomplete;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.edu.ucu.tries.Trie;
import ua.edu.ucu.tries.Tuple;

/**
 *
 * @author andrii
 */
public class PrefixMatches {

    private Trie trie;

    public PrefixMatches(Trie trie) 
    {
        this.trie = trie;
    }

    public int load(String... strings) 
    {
        int count = 0;
        for (String current : strings)
        {
            current = current.toLowerCase();
            String[] currentStrings = current.split("\\s+?");
            for (String elem : currentStrings)
            {
                if (elem.matches("^[a-z]{3,}$"))
                {
                    this.trie.add(new Tuple(elem, elem.length()));
                    count++;
                }
            }

            // if (current.matches("^[a-z]{3,}$"))
            // {
            //     if (current.matches("\\s{1,}"))
            //     {
            //         Pattern space = Pattern.compile("[a-z]+?\\s[a-z]+?"); 
            //         Matcher stringArr = space.matcher(current);
            //         String currentString = stringArr.find();
            //     }
            //     this.trie.add(new Tuple(current, current.length()));
            // }
        }
        return count;       
    }

    public boolean contains(String word) 
    {
        return trie.contains(word);
    }

    public boolean delete(String word) 
    {
        return trie.delete(word);
    }

    public Iterable<String> wordsWithPrefix(String pref) {
        throw new UnsupportedOperationException("Not supported yet.");        
    }

    public Iterable<String> wordsWithPrefix(String pref, int k) {
        throw new UnsupportedOperationException("Not supported yet.");        
    }

    public int size() 
    {
        return trie.size();
    }
}

package ua.edu.ucu.autocomplete;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
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

    public class SmartIterator implements Iterator<String>
    {
        Iterator<String> allWords;
        Set<Integer> lengths;
        int amount;
        String pointer;

        public SmartIterator(Iterator<String> allWords, int k)
        {
            this.allWords = allWords;
            this.lengths = new HashSet<>();
            this.amount = k;
            this.pointer = null;
            moveToNext();
        }
        private String moveToNext()
        {
            if (allWords.hasNext() && amount > 0)
            {
                String word = allWords.next();
                int wordlen = word.length();
                if (!lengths.contains(wordlen))
                {
                    amount--;
                    lengths.add(wordlen);
                }
                pointer = word;
                return pointer;
            }
            pointer = null;
            return pointer;
        }
        public String next()
        {
            String currentString = pointer;
            moveToNext();
            return currentString;
        }
        public boolean hasNext()
        {
            return pointer != null;
        }

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
            
            //A faster approach, using Matcher and Patterns: 
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

    public Iterable<String> wordsWithPrefix(String pref) 
    {
        return trie.wordsWithPrefix(pref);     
    }

    public Iterable<String> wordsWithPrefix(String pref, int k) 
    {
        return new Iterable<String>()
        {
            @Override
            public Iterator<String> iterator() 
            {
                return new SmartIterator(trie.wordsWithPrefix(pref).iterator(), k);
            }
            @Override
            public String toString()
            {
                StringJoiner result = new StringJoiner(", ", "[", "]");
                this.forEach(result::add);
                return result.toString();
            }
        };
    }

    public int size() 
    {
        return trie.size();
    }
}

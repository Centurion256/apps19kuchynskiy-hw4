package ua.edu.ucu.tries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import ua.edu.ucu.utils.collections.Queue;

public class RWayTrie implements Trie, Serializable{
    
    private static final long serialVersionUID = 1005553152697567957L;

    public final static int ALPHABET = 26;
    
    public class TrieNode implements Serializable
    {
        private static final long serialVersionUID = 1L;
        public int weight = 0;
        private TrieNode[] children = new TrieNode[ALPHABET];
        private TrieNode parent = null;
        
        public TrieNode(TrieNode parent)
        {
            this.parent = parent;
        }
        
        public int getWeight() 
        {
            return weight;
        }
        
        public void setWeight(int weight) 
        {
            this.weight = weight;
        }  
    } 
    private TrieNode root = new TrieNode(null);
    
    public class TrieIterator implements Iterator<String>
    {
        TrieNode currentPtr;
        Queue bfsQueue;

        public TrieIterator(TrieNode root)
        {
            this.currentPtr = root;
            this.bfsQueue = new Queue();
            bfsQueue.enqueue(currentPtr);
            moveToNext();
        }

        // //Return a pointer to
        // private TrieNode findNext()
        // {
                //To be implemented.
        // }

        //Change the pointer to the next word
        private TrieNode moveToNext()
        {
            while(!(bfsQueue.isEmpty()))
            {
                currentPtr = (TrieNode) bfsQueue.dequeue();
                for (int i = 0; i < ALPHABET; i++)
                {
                    if (currentPtr.children[i] != null)
                    {
                        bfsQueue.enqueue(currentPtr.children[i]);
                    }
                }
                if (currentPtr.weight != 0)
                {

                    return currentPtr;
                }
            }
            currentPtr = null;
            return currentPtr;
        }

        @Override
        public String next()
        {
            //TrieNode tempPtr = moveToNext();
            if (currentPtr == null)
            {
                return null;
            }
            String currentString = readWord(currentPtr, currentPtr.weight);
            moveToNext();
            return currentString;
        
        }
        public boolean hasNext()
        {
            return this.currentPtr != null;
        }

    }

    @Override
    public void add(Tuple t) 
    {
        int currentIndex;
        TrieNode currentPtr = root;
        for (int i = 0; i < t.weight; i++)
        {
            currentIndex = t.term.charAt(i) - 'a';
            if (currentPtr.children[currentIndex] == null)
            {
                currentPtr.children[currentIndex] = new TrieNode(currentPtr);
            }
            currentPtr = currentPtr.children[currentIndex];
        }
        currentPtr.setWeight(t.weight);
    }
    public TrieNode find(String word, Predicate predicate)
    {
        int currentIndex;
        TrieNode currentPtr = root;
        for (int i = 0; i < word.length(); i++)
        {
            currentIndex = word.charAt(i) - 'a';
            currentPtr = currentPtr.children[currentIndex];
            if (currentPtr == null)
            {
                return null;
            }
        }
        //return currentPtr;
        return predicate.test(currentPtr) ? currentPtr : null;
    }
    public TrieNode find(String word)
    {
        return find(word, (n) -> ((TrieNode) n).weight != 0);
    }

    @Override
    public boolean contains(String word) 
    {
        return find(word) != null;
    }

    @Override
    public boolean delete(String word) 
    {
        TrieNode deletionPtr = find(word);
        if (deletionPtr == null)
        {
            return false;
        }
        deletionPtr.setWeight(0); //A node with 0 weight is not a terminal one
        //which means that we don't have to delete all nodes.
        //It would still be useful to clean up unnecessary nodes to optimize BST.
        cleanUp(deletionPtr);
        return true;
    }
    //Clears all redundant nodes (i.e. nodes that do not eventually end in a word) along a sequence
    private void cleanUp(TrieNode node) 
    {
        TrieNode ptr = node.parent;
        while ((ptr.weight == 0) && (ptr.parent != null))
        {
            ptr = ptr.parent;
        }
        if (!rootWords(ptr).iterator().hasNext())
        {
            for (int i = 0; i < ALPHABET; i++)
            {
            ptr.children[i] = null;
            }
        }

        //Older version:
        // if (!rootWords(node).iterator().hasNext())
        // {
        //     TrieNode ptr = node.parent;
        //     ptr.children[prev_node] //we need to find it somehow
        //     while (ptr.weight == 0)
        //     {
                
        //     }
        // }
    }
    @Override
    public Iterable<String> words() 
    {
        return rootWords(root);
    }
    
    public Iterable<String> rootWords(TrieNode root) 
    {

        return new Iterable<String>()
        {
            @Override
            public Iterator<String> iterator() 
            {
                return new TrieIterator(root);
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

    public String readWord(TrieNode pointer, int length)
    {
        List<Character> word = new ArrayList<>();
        while (length > 0)
        {
            if (pointer == null)
            {
                throw new IndexOutOfBoundsException("Not a valid word.");
            }
            //find the node's index
            TrieNode parentPointer = pointer.parent;
            for (int i = 0; i < ALPHABET; i++)
            {
                if (parentPointer.children[i] == pointer)
                {
                    //we found the index of our node
                    word.add(0, (char) ('a' + i));
                    break;
                }
            }
            pointer = parentPointer;
            length--;
        }
        return word.toString().replaceAll("[\\[\\t,\\s\\]]", "");
    }

    @Override
    public Iterable<String> wordsWithPrefix(String s) 
    {
        TrieNode prefixRoot = find(s, (n) -> true);
        if (prefixRoot == null)
        {
            return new ArrayList<String>();
        }
        return rootWords(prefixRoot);
    }

    @Override
    public int size() 
    {
        //the second argument disables parallel streams.
        return (int) StreamSupport.stream(words().spliterator(), false).count();
    }

}

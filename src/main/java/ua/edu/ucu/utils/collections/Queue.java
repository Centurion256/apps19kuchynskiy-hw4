package ua.edu.ucu.utils.collections;

import ua.edu.ucu.utils.collections.immutable.ImmutableLinkedList;
import ua.edu.ucu.utils.collections.immutable.ImmutableList;

public class Queue 
{
    private ImmutableLinkedList values;

    public Queue()
    {
        this.values = new ImmutableLinkedList();
    }
    public void enqueue(Object e)
    {
        values = values.addLast(e);
    }
    public Object peek() throws IndexOutOfBoundsException
    {
        return this.values.getFirst();
    }
    public Object dequeue()
    {
        Object firstElem = this.peek();
        this.values = this.values.removeFirst();
        return firstElem;
    }
    public boolean isEmpty()
    {
        try
        {
            this.peek();
            return false;
        } 
        catch (IndexOutOfBoundsException e)
        {
            return true;
        }
    }
}

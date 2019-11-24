package ua.edu.ucu.utils.collections.immutable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.StringJoiner;

public final class ImmutableLinkedList implements ImmutableList, Serializable
{

      private static final long serialVersionUID = 1037109004649311186L;
      private Node<Object> head;
      private int size;

      private static class Node<T> implements Serializable
      {
            private static final long serialVersionUID = -5940419144294972450L;
            protected Node<T> next = null;
            protected T data;
            
            public Node(T data, Node<T> nextNode)  
            {
                  this.data = data;
                  this.next = nextNode;
            }
      }

      public ImmutableLinkedList()
      {
            this.size = 0;
            this.head = null; 
      }

      public ImmutableLinkedList(Object[] source)
      {
            this.size = 0;
            if (source.length != 0)
            {     
                  this.head = new Node<>(source[0], null);
                  
                  Node<Object> ptr = this.head;
                  this.size++;
                  while (this.size < source.length)
                  {
                        ptr.next = new Node<>(source[this.size], null);
                        ptr = ptr.next;
                        this.size++;
                  }
            }
      }

      private ImmutableLinkedList copyOf()
      {
            try 
            {      
                  ByteArrayOutputStream exportedData = new ByteArrayOutputStream();
                  ObjectOutputStream exportedObject = new ObjectOutputStream(exportedData);
                  
                  exportedObject.writeObject(this); //Serialize an instance of Immutable list.

                  //create a new byte array and store the contents of the stream in it
                  ByteArrayInputStream importedData = new ByteArrayInputStream(exportedData.toByteArray());
                  //Turn it into an object
                  ObjectInputStream importedObject = new ObjectInputStream(importedData);
                  return (ImmutableLinkedList) importedObject.readObject(); //read the deserialized object
            }
            catch (Exception e)
            {
                  e.printStackTrace();
                  return null;
            }
      }
      private boolean isAValidIndex(int i)
      {
            return (i >= 0) && (i < this.size); 
      }
      @Override
      public Object[] toArray() 
      {
            Object[] arr = new Object[this.size];
            Node<Object> ptr = this.head;
            int index = 0;
        
            while (ptr != null)
            {
                  arr[index] = ptr.data;
                  index++;
                  ptr = ptr.next;
            
            }
            return arr;
      }
      @Override
      public int size()
      {
            return this.size;
      }
      @Override
      public boolean isEmpty()
      {
            return this.size == 0;
      }
      private Node<Object> findNode(int index)
      {
            if (!isAValidIndex(index))
            {
                  throw new IndexOutOfBoundsException();
            }
            int counter = 0;
            Node<Object> ptr = this.head;
            while (counter < index)
            {
                  ptr = ptr.next;
                  counter++;
            }
            return ptr;
      }
      @Override
      public ImmutableLinkedList add(Object e)
      {
            return add(this.size, e);
      }

      @Override
      public ImmutableLinkedList add(int index, Object e)
      {
            ImmutableLinkedList newObject = this.copyOf();
            if (index != 0)
            {
                  Node<Object> previousNode = newObject.findNode(index-1);
                  previousNode.next = new Node<Object>(e, previousNode.next);
            }
            else
            {
                  Node<Object> tempNextVal = this.head;
                  newObject.head = new Node<Object>(e, tempNextVal);
            }
            newObject.size++;
            return newObject;
      }
      @Override
      public ImmutableLinkedList addAll(Object[] c)
      {
            return addAll(this.size, c);
      }
      @Override
      public ImmutableLinkedList addAll(int index, Object[] c)
      {
            ImmutableLinkedList newObject = this.copyOf();
            //The add method is purposely not used here, since it would require
            //to search for the next index each time, which would drastically
            //increase the time complexity.
            Node<Object> addedNode = new Node<Object>(c[0], null);
            Node<Object> ptr = addedNode;
            for (int i = 1; i < c.length; i++)
            {
                  ptr.next = new Node<Object>(c[i], null);
                  ptr = ptr.next;
            }
            if (index != 0)
            {
                  Node<Object> previousNode = newObject.findNode(index-1);
                  ptr.next = previousNode.next;
                  previousNode.next = addedNode;
            }
            else
            {
                  ptr.next = this.head;
                  newObject.head = addedNode;
            }
            newObject.size += c.length;
            return newObject;
      }
      @Override
      public Object get(int index)
      {
            return this.findNode(index).data;
      }
      @Override
      public ImmutableLinkedList remove(int index)
      {
            if (!isAValidIndex(index))
            {
                  throw new IndexOutOfBoundsException();
            }
            ImmutableLinkedList newObject = this.copyOf();
            if (index == 0)
            {
                 newObject.head = newObject.head.next;
                 newObject.size--;
                 return newObject; 
            }
            Node<Object> previousNode = newObject.findNode(index-1);
            Node<Object> toBeRemoved = previousNode.next;
            if (toBeRemoved != null)
            {
                  previousNode.next = toBeRemoved.next;
            }
            newObject.size--;
            return newObject;
            
      }
      @Override
      public ImmutableLinkedList set(int index, Object e)
      {
            if (!isAValidIndex(index))
            {
                  throw new IndexOutOfBoundsException();
            }
            ImmutableLinkedList newObject = this.copyOf();
            if (index == 0)
            {
                 newObject.head.data = e;
                 return newObject; 
            }
            Node<Object> toBeChanged = newObject.findNode(index);

            toBeChanged.data = e;

            return newObject;
      }
      @Override
      public int indexOf(Object e)
      {
            Node<Object> ptr = this.head;
            int index = 0;
            while (ptr != null)
            {
                  if (ptr.data.equals(e))
                  {
                        return index;
                  }
                  ptr = ptr.next;
                  index++;
            }
            return -1;
      }
      @Override
      public ImmutableLinkedList clear()
      {
            return new ImmutableLinkedList();
      }
      @Override
      public String toString()
      {
            StringJoiner strJoiner = new StringJoiner(", ");
            Node<Object> ptr = this.head;
            while (ptr != null)
            {
                  strJoiner.add(ptr.data.toString());
                  ptr = ptr.next;
            }
            return strJoiner.toString();
      }
      public ImmutableLinkedList addFirst(Object e)
      {
            return add(0, e);
      }
      public ImmutableLinkedList addLast(Object e)
      {
            return add(this.size, e);
      }
      public Object getFirst()
      {
            return this.findNode(0).data;
      }
      public Object getLast()
      {
            return this.findNode(this.size-1).data;
      }
      public ImmutableLinkedList removeFirst()
      {
            return (ImmutableLinkedList) this.remove(0);
      }
      public ImmutableLinkedList removeLast()
      {
            return (ImmutableLinkedList) this.remove(this.size);
      }
}

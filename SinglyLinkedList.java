
/**
 * Your implementation of a circular singly linked list.
 *
 * @author Carson Duerr
 * @userid cduerr3
 * @GTID 903186923
 * @version 1.0
 */
public class SinglyLinkedList<T> {
    // Do not add new instance variables or modify existing ones.
    private LinkedListNode<T> head;
    private int size;

    /**
     * Adds the element to the index specified.
     *
     * Adding to indices 0 and {@code size} should be O(1), all other cases are
     * O(n).
     *
     * @param index the requested index for the new element
     * @param data the data for the new element
     * @throws IndexOutOfBoundsException if index is negative or
     * index > size
     * @throws IllegalArgumentException if data is null
     */
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index cannot be negative"
                    + " or greater than size");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (index == 0) {
            addToFront(data);
        } else if (index == size) {
            addToBack(data);
        } else {
            LinkedListNode<T> curr = head;
            int i = 0;
            while (i < index) {
                curr = curr.getNext();
                i++;
            }
            LinkedListNode<T> x = new LinkedListNode<T>(data, curr.getNext());
            curr.setNext(x);
            size++;
        }
    }

    /**
     * Adds the element to the front of the list.
     *
     * Must be O(1) for all cases.
     *
     * @param data the data for the new element
     * @throws IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (size == 0) {
            head = new LinkedListNode<T>(data);
            head.setNext(head);
        } else {
            LinkedListNode<T> x = new LinkedListNode<T>(head.getData(), head.getNext());
            head.setData(data);
            head.setNext(x);
        }
        size++;
    }

    /**
     * Adds the element to the back of the list.
     *
     * Must be O(1) for all cases.
     *
     * @param data the data for the new element
     * @throws IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (size == 0) {
            head = new LinkedListNode<T>(data);
            head.setNext(head);
        } else {
            LinkedListNode<T> x
                    = new LinkedListNode<T>(head.getData(), head.getNext());
            head.setData(data);
            head.setNext(x);
            head = head.getNext();
        }
        size++;
    }

    /**
     * Removes and returns the element from the index specified.
     *
     * Removing from index 0 should be O(1), all other cases are O(n).
     *
     * @param index the requested index to be removed
     * @return the data formerly located at index
     * @throws IndexOutOfBoundsException if index is negative or
     * index >= size
     */
    public T removeAtIndex(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative"
                    + "or greater than or equal to size");
        }
        T returned;
        if (size == 1) {
            returned = head.getData();
            head.setNext(null);
            head = null;
        } else if (index == 0) {
            returned = head.getData();
            head.setData(head.getNext().getData());
            head.setNext(head.getNext().getNext());
        } else {
            LinkedListNode<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            returned = current.getNext().getData();
            current.setNext(current.getNext().getNext());
        }
        size--;
        return returned;
    }

    /**
     * Removes and returns the element at the front of the list. If the list is
     * empty, return {@code null}.
     *
     * Must be O(1) for all cases.
     *
     * @return the data formerly located at the front, null if empty list
     */
    public T removeFromFront() {
        if (size == 0) {
            return null;
        }
        T returned = head.getData();
        if (size == 1) {
            head.setNext(null);
            head = null;
        } else {
            head.setData(head.getNext().getData());
            head.setNext(head.getNext().getNext());
        }
        size--;
        return returned;
    }

    /**
     * Removes and returns the element at the back of the list. If the list is
     * empty, return {@code null}.
     *
     * Must be O(n) for all cases.
     *
     * @return the data formerly located at the back, null if empty list
     */
    public T removeFromBack() {
        if (size == 0) {
            return null;
        }
        LinkedListNode<T> curr = head;
        while (curr.getNext().getNext() != head) {
            curr = curr.getNext();
        }
        T returned = curr.getNext().getData();
        curr.setNext(head);
        size--;
        return returned;
    }

    /**
     * Removes the last copy of the given data from the list.
     *
     * Must be O(n) for all cases.
     *
     * @param data the data to be removed from the list
     * @return the removed data occurrence from the list itself (not the data
     * passed in), null if no occurrence
     * @throws IllegalArgumentException if data is null
     */
    public T removeLastOccurrence(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        LinkedListNode<T> curr = head;
        LinkedListNode<T> hold = curr;
        T returned;
        for (int i = 0; i < size - 1; i++) {
            if (curr.getNext().getData().equals(data)) {
                hold = curr;
            }
            curr = curr.getNext();
        }
        returned = hold.getNext().getData();
        hold.setNext(hold.getNext().getNext());
        size--;
        return returned;
    }

    /**
     * Returns the element at the specified index.
     *
     * Getting index 0 should be O(1), all other cases are O(n).
     *
     * @param index the index of the requested element
     * @return the object stored at index
     * @throws IndexOutOfBoundsException if index < 0 or
     * index >= size
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new  IndexOutOfBoundsException(" Index cannot be negative or"
                    + " greater than or equal to size");
        }
        if (index == 0) {
            return head.getData();
        } else {
            LinkedListNode<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            return current.getNext().getData();
        }
    }

    /**
     * Returns an array representation of the linked list.
     *
     * Must be O(n) for all cases.
     *
     * @return an array of length {@code size} holding all of the objects in
     * this list in the same order
     */
    public Object[] toArray() {
        Object[] list = new Object[size];
        LinkedListNode<T> current = head;
        for (int i = 0; i < size; i++) {
            list[i] = current.getData();
            current = current.getNext();
        }
        return list;
    }

    /**
     * Returns a boolean value indicating if the list is empty.
     *
     * Must be O(1) for all cases.
     *
     * @return true if empty; false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the list of all data.
     *
     * Must be O(1) for all cases.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Returns the number of elements in the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY!
        return size;
    }

    /**
     * Returns the head node of the linked list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return node at the head of the linked list
     */
    public LinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }
}
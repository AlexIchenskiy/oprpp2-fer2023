package hr.fer.oprpp2.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Class representing a linked list collection.
 */
public class LinkedListIndexedCollection implements List {

    /**
     * Local class representing a list internal node.
     */
    private static class ListNode {

        /**
         * Previous node.
         */
        ListNode previous;

        /**
         * Next node.
         */
        ListNode next;

        /**
         * Value of this node.
         */
        Object value;

        /**
         * Constructs a node with a provided value.
         * @param value Object to be used as a node value
         */
        public ListNode(Object value) {
            this.value = value;
        }

        /**
         * Converts node to string.
         * @return String representation of the node
         */
        @Override
        public String toString() {
            return this.value.toString();
        }
    }

    /**
     * Number of the elements stored in the linked list.
     */
    private int size;

    /**
     * First node in the linked list.
     */
    private ListNode first;

    /**
     * Last node in the linked list.
     */
    private ListNode last;

    /**
     * Number of previous array modifications.
     */
    private long modificationCount = 0;

    /**
     * Local implementation of ElementsGetter for indexed array.
     */
    private static class ElementsGetterLLIC implements ElementsGetter {

        /**
         * Parent collection.
         */
        private final LinkedListIndexedCollection llic;

        /**
         * Pointer to the current list node.
         */
        private ListNode pointer;

        /**
         * Modification count at the moment of getter creation.
         */
        private final long savedModificationCount;

        private ElementsGetterLLIC(LinkedListIndexedCollection llic) {
            if (llic == null) {
                throw new NullPointerException("Array cant be null!");
            }

            this.llic = llic;
            this.pointer = llic.first;
            this.savedModificationCount = llic.modificationCount;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNextElement() {
            if (this.savedModificationCount != llic.modificationCount) {
                throw new ConcurrentModificationException();
            }

            return this.pointer != null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getNextElement() {
            if (this.savedModificationCount != llic.modificationCount) {
                throw new ConcurrentModificationException();
            }

            if (!this.hasNextElement()) {
                throw new NoSuchElementException("No elements left!");
            }

            Object value = this.pointer.value;
            this.pointer = this.pointer.next;

            return value;
        }
    }

    /**
     * Constructs an empty linked list.
     */
    public LinkedListIndexedCollection() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    /**
     * Constructs a linked list with all elements from provided collection.
     * @param other Other collection which values are used to fill linked list
     */
    public LinkedListIndexedCollection(Collection other) {
        this.addAll(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Object value) {
        if (value == null) {
            throw new NullPointerException("Value cant be null!");
        }

        ListNode newNode = new ListNode(value);

        if (size == 0) {
            this.first = newNode;
        } else {
            this.last.next = newNode;
            newNode.previous = this.last;
        }

        this.last = newNode;
        this.size++;
        this.modificationCount++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object value) {
        ListNode current = this.first;

        while (current != null) {
            if (current.value.equals(value)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object value) {
        ListNode current = this.first;

        while (current != null) {
            if (current.value.equals(value)) {
                if (current.previous != null) {
                    current.previous.next = current.next;
                } else {
                    first = current.next;
                }

                if (current.next != null) {
                    current.next.previous = current.previous;
                } else {
                    last = current.previous;
                }

                this.size--;
                this.modificationCount++;

                return true;
            }

            current = current.next;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        Object[] arr = new Object[this.size];

        ListNode current = this.first;
        int index = 0;

        while (current != null) {
            arr[index] = current.value;
            current = current.next;
            index++;
        }

        return arr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.first = null;
        this.last = null;
        this.size = 0;
        this.modificationCount++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ElementsGetter createElementsGetter() {
        return new ElementsGetterLLIC(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("Index cant be out of range!");
        }

        ListNode current = this.first;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Object value, int position) {
        if (position < 0 || position > this.size) {
            throw new IndexOutOfBoundsException("Index cant be out of range!");
        }

        ListNode newNode = new ListNode(value);

        if (this.size == 0) {
            this.first = newNode;
            this.last = newNode;
            this.size++;
            this.modificationCount++;
            return;
        }

        ListNode current = this.first;

        for (int i = 0; i < position; i++) {
            current = current.next;
        }

        if (position == 0) {
            newNode.next = this.first;
            this.first.previous = newNode;
            this.first = newNode;
            this.size++;
            this.modificationCount++;
            return;
        }

        if (position == this.size) {
            newNode.previous = this.last;
            this.last.next = newNode;
            this.last = newNode;
            this.size++;
            this.modificationCount++;
            return;
        }

        newNode.next = current;
        newNode.previous = current.previous;
        current.previous.next = newNode;
        current.previous = newNode;

        this.size++;
        this.modificationCount++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(Object value) {
        ListNode current = first;
        int index = 0;

        while (current != null) {
            if (current.value == value) {
                return index;
            }

            current = current.next;
            index++;
        }

        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("Index cant be out of range!");
        }

        ListNode current = first;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        if (current.previous != null) {
            current.previous.next = current.next;
        } else {
            first = current.next;
        }

        if (current.next != null) {
            current.next.previous = current.previous;
        } else {
            last = current.previous;
        }

        this.size--;
        this.modificationCount++;
    }

}

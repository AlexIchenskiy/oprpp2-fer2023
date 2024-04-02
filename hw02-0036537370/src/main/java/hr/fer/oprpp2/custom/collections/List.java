package hr.fer.oprpp2.custom.collections;

/**
 * Interface representing a list-like collection.
 */
public interface List extends Collection {

    /**
     * Get element by the provided index.
     * @param index Index of the desired element
     * @return Element on the provided index
     */
    Object get(int index);

    /**
     * Insert a value into collection.
     * @param value Value to be inserted
     * @param position Position of the inserted element
     */
    void insert(Object value, int position);

    /**
     * Index of the first appearance of the value in the collection.
     * @param value Value to be searched for.
     * @return Position of the element.
     */
    int indexOf(Object value);

    /**
     * Removes element from the collection by its index.
     * @param index Index of element to be removed
     */
    void remove(int index);

}

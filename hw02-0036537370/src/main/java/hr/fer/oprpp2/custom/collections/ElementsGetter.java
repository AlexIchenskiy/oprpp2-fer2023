package hr.fer.oprpp2.custom.collections;

/**
 * Class representing getter for collection elements.
 */
interface ElementsGetter {

    /**
     * Returns whether there are elements left in the collection.
     * @return Boolean representing if there are any more elements
     */
    boolean hasNextElement();

    /**
     * Returns the next element in the collection.
     * @return The next element from collection
     */
    Object getNextElement();

    /**
     * Applies the provided processors process method to all remaining elements in the collection.
     * @param p Processor to be used in elements processing
     */
    default void processRemaining(Processor p) {
        if (p == null) {
            throw new NullPointerException("Processor cant be null!");
        }

        while (this.hasNextElement()) {
            p.process(this.getNextElement());
        }
    }

}

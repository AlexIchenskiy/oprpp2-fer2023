package hr.fer.oprpp2.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a map-like structure where each value is a linked-list-like stack.
 */
public class ObjectMultistack {

    private final Map<String, MultistackEntry> map = new HashMap<>();

    public ObjectMultistack() {
    }

    public void push(String keyName, ValueWrapper valueWrapper) {
        MultistackEntry entry = new MultistackEntry(valueWrapper);

        if (this.map.containsKey(keyName)) entry.setNext(this.map.get(keyName));

        this.map.put(keyName, entry);
    }

    /**
     * Return and remove a value from the top of the stack defined by key.
     * @param keyName Key name for the desired stack
     * @return Value from the stack top
     */
    public ValueWrapper pop(String keyName) {
        MultistackEntry entry = this.map.get(keyName);

        if (entry == null) throw new RuntimeException("Stack is empty.");

        MultistackEntry nextEntry = entry.getNext();

        this.map.put(keyName, nextEntry);

        if (nextEntry == null) this.map.remove(keyName);

        return entry.getValue();
    }

    /**
     * Return a value from the top of the stack defined by key.
     * @param keyName Key name for the desired stack
     * @return Value from the stack top
     */
    public ValueWrapper peek(String keyName) {
        MultistackEntry entry = this.map.get(keyName);

        if (entry == null) throw new RuntimeException("Stack is empty.");

        return this.map.get(keyName).getValue();
    }

    /**
     * Check if a stack defined by the provided key is empty.
     * @param keyName Key name for the desired stack
     * @return Boolean representing whether the corresponding stack is empty
     */
    public boolean isEmpty(String keyName) {
        return !map.containsKey(keyName);
    }

    /**
     * Class representing an entry for the multistack map structure.
     */
    public static class MultistackEntry {

        private ValueWrapper value;

        private MultistackEntry next = null;

        public MultistackEntry(ValueWrapper value) {
            this.value = value;
        }

        public ValueWrapper getValue() {
            return value;
        }

        public void setValue(ValueWrapper value) {
            this.value = value;
        }

        public MultistackEntry getNext() {
            return next;
        }

        public void setNext(MultistackEntry next) {
            this.next = next;
        }

    }

}

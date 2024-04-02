package hr.fer.oprpp2.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

public class ObjectMultistack {

    private final Map<String, MultistackEntry> map = new HashMap<>();

    public ObjectMultistack() {
    }

    public void push(String keyName, ValueWrapper valueWrapper) {
        MultistackEntry entry = new MultistackEntry(valueWrapper);

        if (this.map.containsKey(keyName)) entry.setNext(this.map.get(keyName));

        this.map.put(keyName, entry);
    }

    public ValueWrapper pop(String keyName) {
        MultistackEntry entry = this.map.get(keyName);

        if (entry == null) throw new RuntimeException("Stack is empty.");

        MultistackEntry nextEntry = entry.getNext();

        this.map.put(keyName, nextEntry);

        if (nextEntry == null) this.map.remove(keyName);

        return entry.getValue();
    }

    public ValueWrapper peek(String keyName) {
        MultistackEntry entry = this.map.get(keyName);

        if (entry == null) throw new RuntimeException("Stack is empty.");

        return this.map.get(keyName).getValue();
    }

    public boolean isEmpty(String keyName) {
        return !map.containsKey(keyName);
    }

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

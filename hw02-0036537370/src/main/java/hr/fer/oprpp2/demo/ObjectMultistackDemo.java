package hr.fer.oprpp2.demo;

import hr.fer.oprpp2.custom.scripting.exec.ObjectMultistack;
import hr.fer.oprpp2.custom.scripting.exec.ValueWrapper;

public class ObjectMultistackDemo {
    public static void main(String[] args) {
        ObjectMultistack multistack = new ObjectMultistack();
        ValueWrapper year = new ValueWrapper(Integer.valueOf(2000));
        multistack.push("year", year);
        ValueWrapper price = new ValueWrapper(200.51);
        multistack.push("price", price);
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());
        System.out.println("Current value for price: "
                + multistack.peek("price").getValue());
        multistack.push("year", new ValueWrapper(Integer.valueOf(1900)));
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());
        multistack.peek("year").setValue(
                ((Integer)multistack.peek("year").getValue()).intValue() + 50
        );
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());
        multistack.pop("year");
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());
        multistack.peek("year").add("5");
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());
        multistack.peek("year").add(5);
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());
        multistack.peek("year").add(5.0);
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());

        System.out.println("\n");

        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);
        v1.add(v2.getValue()); // v1 now stores Integer(0); v2 still stores null.
        ValueWrapper v3 = new ValueWrapper("1.2E1");
        ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
        v3.add(v4.getValue()); // v3 now stores Double(13); v4 still stores Integer(1).
        ValueWrapper v5 = new ValueWrapper("12");
        ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));
        v5.add(v6.getValue()); // v5 now stores Integer(13); v6 still stores Integer(1).
        ValueWrapper v7 = new ValueWrapper("Ankica");
        ValueWrapper v8 = new ValueWrapper(Integer.valueOf(1));
        // v7.add(v8.getValue()); // throws RuntimeException
    }
}

package hr.fer.oprpp2.custom.collections.demo;

import hr.fer.oprpp2.custom.collections.Tester;

public class EvenIntegerTester implements Tester {

    @Override
    public boolean test(Object obj) {
        if (!(obj instanceof Integer)) {
            return false;
        }

        Integer i = (Integer) obj;

        return i % 2 == 0;
    }

    public static void main(String[] args) {
        Tester t = new EvenIntegerTester();
        System.out.println(t.test("Ivo"));
        System.out.println(t.test(22));
        System.out.println(t.test(3));
    }

}

package org.example;

public class TestTarget {

    public void testMethod(int n) {
        int sum = 0;

        for (int i = 0; i < n; i++) {
            sum += i;
        }

        if (sum > 10) {
            sum++;
        }

        while (sum < 100) {
            sum += 10;
        }
    }
}
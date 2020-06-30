package section3.lesson8;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Long> factorialNums = Arrays.asList(100L, 10000000L, 30000L, 24L, 54000L, 546L, 3000L, 9L);
        List<FactorialThread> factorialThreads = new ArrayList<>();

        for (Long l : factorialNums) {
            factorialThreads.add(new FactorialThread(l));
        }

        for (Thread t : factorialThreads) {
            t.start();
        }

        for (Thread t : factorialThreads) {
            t.join(2000);
        }

        for (Thread t : factorialThreads){
            t.interrupt();
        }

        for (FactorialThread t : factorialThreads) {
            if (t.isFinished()) {
                System.out.println("The factorial for " + t.getNumber() + " is " + t.getResult());
            } else {
                System.out.println("Thread with number " + t.getNumber() + " is not finished");
            }
        }
    }

    public static class FactorialThread extends Thread {
        long input;
        BigInteger result;
        boolean finished = false;

        public FactorialThread(long input) {
            this.input = input;
        }

        @Override
        public void run() {
            calculateFactorial();
            finished = true;
        }

        public void calculateFactorial() {
            result = BigInteger.ONE;
            for (long i = input; i > 0; i--) {
                if (this.isInterrupted()) {
                    result = BigInteger.ONE;
                }
                result = result.multiply(BigInteger.valueOf(i));
            }
        }

        public BigInteger getResult() {
            return result;
        }

        public long getNumber() {
            return input;
        }

        public boolean isFinished() {
            return finished;
        }
    }
}

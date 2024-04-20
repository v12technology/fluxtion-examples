package com.fluxtion.example.reference.serviceexport;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;

import java.util.function.IntSupplier;

public class ExportingService {
    public interface MyService {
        void addNumbers(int a, int b);
    }

    public static class MyServiceImpl implements @ExportService MyService, IntSupplier {

        private int sum;

        @Override
        public void addNumbers(int a, int b) {
            System.out.printf("adding %d + %d %n", a, b);
            sum = a + b;
        }

        @Override
        public int getAsInt() {
            return sum;
        }
    }

    public static class ResultPublisher {
        private final IntSupplier intSupplier;

        public ResultPublisher(IntSupplier intSupplier) {
            this.intSupplier = intSupplier;
        }

        @OnTrigger
        public boolean printResult() {
            System.out.println("result - " + intSupplier.getAsInt());
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new ResultPublisher(new MyServiceImpl()));
        processor.init();

        //get the exported service
        MyService myService = processor.getExportedService();
        myService.addNumbers(30, 12);
    }
}

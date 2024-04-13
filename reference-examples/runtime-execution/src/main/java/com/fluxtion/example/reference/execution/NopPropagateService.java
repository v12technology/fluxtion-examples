package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;

import java.util.function.IntSupplier;

public class NopPropagateService {
    public interface MyService {
        void cumulativeSum(int a);
        void reset();
    }

    public static class MyServiceImpl
            implements
            @ExportService(propagate = false) MyService,
            @ExportService Runnable,
            IntSupplier {

        private int sum;

        @Override
        public void cumulativeSum(int a) {
            sum += a;
            System.out.printf("MyServiceImpl::adding %d cumSum: %d %n", a, sum);
        }

        @Override
        public void reset() {
            sum = 0;
            System.out.printf("MyServiceImpl::reset cumSum: %d %n", sum);
        }

        @Override
        public int getAsInt() {
            return sum;
        }

        @Override
        public void run() {
            System.out.println("running calculation - will trigger publish");
        }
    }

    public static class ResultPublisher {
        private final IntSupplier intSupplier;

        public ResultPublisher(IntSupplier intSupplier) {
            this.intSupplier = intSupplier;
        }

        @OnTrigger
        public boolean printResult() {
            System.out.println("ResultPublisher::result - " + intSupplier.getAsInt());
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new ResultPublisher(new MyServiceImpl()));
        processor.init();

        //get the exported service - no triggering notifications fired on this service
        MyService myService = processor.getExportedService();
        myService.cumulativeSum(11);
        myService.cumulativeSum(31);
        System.out.println();

        //will cause a trigger notification
        processor.consumeServiceIfExported(Runnable.class, Runnable::run);

        System.out.println();
        myService.reset();
    }
}

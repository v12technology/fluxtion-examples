package com.fluxtion.example.reference.serviceexport;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoPropagateFunction;
import com.fluxtion.runtime.annotations.OnTrigger;

import java.util.function.IntSupplier;

public class NopPropagateServiceMethod {
    public interface MyService {
        void cumulativeSum(int a);
        void reset();
    }

    public static class MyServiceImpl implements @ExportService MyService, IntSupplier {

        private int sum;

        @Override
        public void cumulativeSum(int a) {
            sum += a;
            System.out.printf("MyServiceImpl::adding %d cumSum: %d %n", a, sum);
        }

        @Override
        @NoPropagateFunction
        public void reset() {
            sum = 0;
            System.out.printf("MyServiceImpl::reset cumSum: %d %n", sum);
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
            System.out.println("ResultPublisher::result - " + intSupplier.getAsInt());
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new ResultPublisher(new MyServiceImpl()));
        processor.init();

        //get the exported service
        MyService myService = processor.getExportedService();
        myService.cumulativeSum(11);
        myService.cumulativeSum(31);
        System.out.println();

        myService.reset();
    }
}

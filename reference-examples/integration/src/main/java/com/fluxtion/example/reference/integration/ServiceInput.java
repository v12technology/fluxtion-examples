package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.annotations.ExportService;

import java.util.function.Consumer;

public class ServiceInput {

    public static void main(String[] args) {
        EventProcessor<?> processor = Fluxtion.interpret(new MyConsumer());

        //lifecycle init required
        processor.init();

        //lookup service using type inference
        ServiceController svc = processor.getExportedService();
        svc.serviceOn(System.out::println, "WORLD");

        //SUPPORTED lookups

        //lookup with explicit type
        svc = processor.getExportedService(ServiceController.class);
        svc.serviceOn(System.out::println, "WORLD");

        //lookup with explicit type, use default value if none exported
        MiaServiceController svcMissing = processor.getExportedService(MiaServiceController.class, (consumer, message) -> {
            System.out.println("MiaServiceController not exported");
            return false;
        });
        svcMissing.serviceOn(System.out::println, "WORLD");

        //lookup and consume service if exported
        processor.consumeServiceIfExported(ServiceController.class, s -> s.serviceOn(System.out::println, "WORLD"));

        //is service exported
        System.out.println("ServiceController.class exported   : " + processor.exportsService(ServiceController.class));
        System.out.println("MiaServiceController.class exported: " + processor.exportsService(MiaServiceController.class));
    }

    public interface ServiceController {
        boolean serviceOn(Consumer<String> consumer, String message);
    }

    public interface MiaServiceController {
        boolean serviceOn(Consumer<String> consumer, String message);
    }

    public static class MyConsumer implements @ExportService ServiceController {
        @Override
        public boolean serviceOn(Consumer<String> consumer, String message) {
            consumer.accept("hello " + message);
            return false;
        }
    }
}

package com.fluxtion.example.cookbook.lottery;

import com.fluxtion.compiler.extern.spring.FluxtionSpring;
import com.fluxtion.example.cookbook.lottery.auditor.SystemStatisticsAuditor;
import com.fluxtion.runtime.audit.EventLogControlEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BuildAot {
    public static void main(String[] args) {
        FluxtionSpring.compileAot(
                new ClassPathXmlApplicationContext("/spring-lottery.xml"),
                c ->{
                    c.addAuditor(new SystemStatisticsAuditor(), "systemAuditor");
                },
                c -> {
                    c.setPackageName("com.fluxtion.example.cookbook.lottery.aot");
                    c.setClassName("LotteryProcessor");
                    //required because maven does not pass the classpath properly
                    c.setCompileSource(false);
                }
        );
    }
}

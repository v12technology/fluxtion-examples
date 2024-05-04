package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

public class DSLCodeSketch {

    private static void mapSample(EventProcessorConfig config){
        var stringFlow = DataFlow.subscribe(String.class).console("\ninput: '{}'");

        var charCount = stringFlow.map(new MyFunctions()::totalCharCount)
                .console("charCount: {}");
        var upperCharCount = stringFlow.map(new MyFunctions()::totalUpperCaseCharCount)
                .console("upperCharCount: {}");

        DataFlow.mapBiFunction(new MyFunctions.SimpleMath()::updatePercentage, upperCharCount, charCount)
                .console("percentage chars upperCase all words:{}");

        DataFlow.mapBiFunction(MyFunctions::updatePercentage,  upperCharCount, charCount)
                .console("percentage chars upperCase this word:{}");
    }

    private static void basicMap() {
        var stringFlow = DataFlow.subscribe(String.class);

        stringFlow.map(String::toLowerCase);
        stringFlow.mapToInt(s -> s.length()/2);
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(DSLCodeSketch::mapSample);
        processor.init();

        processor.onEvent("test ME");
        processor.onEvent("and AGAIN");
    }
}

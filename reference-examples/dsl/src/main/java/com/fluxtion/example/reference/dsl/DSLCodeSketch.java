package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.reference.dsl.MyFunctions.SimpleMath;

public class DSLCodeSketch {

    private static void mapSample(EventProcessorConfig config) {
        //STATEFUL FUNCTIONS
        MyFunctions myFunctions = new MyFunctions();
        SimpleMath simpleMath = new SimpleMath();

        //BUILD THE GRAPH WITH DSL
        var stringFlow = DataFlow.subscribe(String.class).console("\ninput: '{}'");

        var charCount = stringFlow.map(myFunctions::totalCharCount)
                .console("charCount: {}");

        var upperCharCount = stringFlow.map(myFunctions::totalUpperCaseCharCount)
                .console("upperCharCount: {}");

        DataFlow.mapBiFunction(simpleMath::updatePercentage, upperCharCount, charCount)
                .console("percentage chars upperCase all words:{}");

        //STATELESS FUNCTION
        DataFlow.mapBiFunction(MyFunctions::wordUpperCasePercentage, upperCharCount, charCount)
                .console("percentage chars upperCase this word:{}");
    }

    private static void basicMap() {
        var stringFlow = DataFlow.subscribe(String.class);

        stringFlow.map(String::toLowerCase);
        stringFlow.mapToInt(s -> s.length() / 2);
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(DSLCodeSketch::mapSample);
        processor.init();

        processor.onEvent("test ME");
        processor.onEvent("and AGAIN");
    }
}

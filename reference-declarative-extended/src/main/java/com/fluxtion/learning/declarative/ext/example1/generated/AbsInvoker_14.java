package com.fluxtion.learning.declarative.ext.example1.generated;

import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnEventComplete;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.extension.declarative.api.numeric.NumericValuePush;
import com.fluxtion.extension.declarative.api.numeric.NumericValue;
import com.fluxtion.extension.declarative.funclib.api.math.UnaryFunctions.Abs;
/**
 * generated NumericFunction wrapper.
 * Wraps a numeric function for invocation.
 * target class  : Abs
 * target method : abs
 * 
 * @author Greg Higgins
 */
public class AbsInvoker_14 extends Number implements NumericValue{

    //source operand inputs
    public SubtractInvoker_6 source_SubtractInvoker_6_13;
    private Abs f = new  Abs();
    private double result;
    private boolean updated;



    @OnParentUpdate("source_SubtractInvoker_6_13")
    public void sourceChange_source_SubtractInvoker_6_13(SubtractInvoker_6 updated){
        calculate();
    }

    public void calculate(){
        double oldValue = result;
        result = f.abs(source_SubtractInvoker_6_13.doubleValue());
        updated = oldValue != result;
    }

    @OnEvent
    public boolean onEvent(){
        return updated;
    }

    @OnEventComplete
    public void afterCalculate(){
        updated = false;
    }

    


    @Initialise
    public void init(){
        result = 0;
        updated = false;
    }

    @Override
    public short shortValue() {
        return (short) result;
    }

    @Override
    public byte byteValue() {
        return (byte) result;
    }

    @Override
    public float floatValue() {
        return (float) result;
    }

    @Override
    public int intValue() {
        return (int) result;
    }

    @Override
    public long longValue() {
        return (long) result;
    }

    @Override
    public double doubleValue() {
        return (double) result;
    }
    
}


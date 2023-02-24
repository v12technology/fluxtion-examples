package com.fluxtion.example.aot.imperative;

import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.stream.EventStream.IntEventSupplier;

public class MyRootClass {

    public IntEventSupplier intEventSupplier;

    @OnEventHandler
    public void onString(String input){
        System.out.println("received:" + input);
    }

    @OnTrigger
    public void triggered(){
        if(intEventSupplier.hasChanged()){
            System.out.println("IntEventSupplier has updated:" + intEventSupplier.getAsInt());
        }else{
            System.out.println("IntEventSupplier not updated");
        }
    }
}

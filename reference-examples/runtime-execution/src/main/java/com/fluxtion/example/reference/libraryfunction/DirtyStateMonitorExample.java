package com.fluxtion.example.reference.libraryfunction;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.callback.DirtyStateMonitor;
import com.fluxtion.runtime.dataflow.FlowSupplier;
import com.fluxtion.runtime.node.NamedNode;

public class DirtyStateMonitorExample {

    public static class TriggeredChild implements NamedNode {
        @Inject
        public DirtyStateMonitor dirtyStateMonitor;
        private final FlowSupplier<Integer> intDataFlow;

        public TriggeredChild(FlowSupplier<Integer> intDataFlow) {
            this.intDataFlow = intDataFlow;
        }

        @OnTrigger
        public boolean triggeredChild() {
            System.out.println("TriggeredChild -> " + intDataFlow.get());
            return true;
        }

        public void printDirtyStat() {
            System.out.println("\nintDataFlow dirtyState:" + dirtyStateMonitor.isDirty(intDataFlow));
        }

        public void markDirty() {
            dirtyStateMonitor.markDirty(intDataFlow);
            System.out.println("\nmark dirty intDataFlow dirtyState:" + dirtyStateMonitor.isDirty(intDataFlow));
        }

        @Override
        public String getName() {
            return "triggeredChild";
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {
        var processor = Fluxtion.interpret(new TriggeredChild(DataFlow.subscribe(Integer.class).flowSupplier()));
        processor.init();
        TriggeredChild triggeredChild = processor.getNodeById("triggeredChild");

        processor.onEvent(2);
        processor.onEvent(4);

        //NOTHING HAPPENS
        triggeredChild.printDirtyStat();
        processor.triggerCalculation();

        //MARK DIRTY
        triggeredChild.markDirty();
        processor.triggerCalculation();
    }
}

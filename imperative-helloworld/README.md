# Hello world example imperative construction


creates a processing graph imperatively, extracts double values from events, calculates the sum and prints a
message to console if the sum is greater than a 100.

The graph is constructed imperatively and the root node is passed to the Fluxtion compiler. The compiler reflects the
object graph and generates an EventProcessor that invokes the node methods in the correct topological order in response
to an incoming event. App classes use annotations to mark methods that will be events handlers in the final 
event processor.

- @OnEventHandler annotation to declare the entry point of an execution path. 
- @OnTrigger annotated methods indicate call back methods to be invoked if a parent propagates a change. 
- The return flag from the @OnTrigger method indicates if the event should be propagated. 
In this case the event is only propagated if the sum > 100.

### Node diagram of event processor

![](docs/Processor.png)
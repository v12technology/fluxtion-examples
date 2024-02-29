# Hello world example imperative construction

This example creates a Fluxtion event processor imperatively. The goal is to extract double values from two different 
events streams, calculate the sum and print a message to console if the sum is greater than 100. The values are stored and
updated independently partitioned by event type. The stream of events can be infinitely long, calculations are run
whenever a new event is received.

The event processor is constructed imperatively using Fluxtion builder api's supplied with user classes. The resulting 
event processor instance is fed the event streams. All dispatch and change notification is handled by Fluxtion when an
event is received. Business logic resides in the user functions/classes.

# Building
The process for building an event processor with Fluxtion are quite simple:

- Create user classes with business logic
- Annotate callback methods
   - **@OnEventHandler** annotation declares the [entry point](src/main/java/com/fluxtion/example/imperative/helloworld/Event_A_Handler.java) of an execution path, triggered by an external event.
   - **@OnTrigger** annotated [methods](src/main/java/com/fluxtion/example/imperative/helloworld/DataSumCalculator.java) indicate call back methods to be invoked if a parent propagates a change.
     The return boolean flag from a trigger method indicates if event notification should be propagated.
- Add the user classes to a [fluxtion builder](src/main/java/com/fluxtion/example/imperative/helloworld/AotBuilder.java) 
- Add the Fluxtion maven plugin to your build [pom.xml](pom.xml), the event processor will be generated ahead of time (AOT)
- Instantiate the generated event processor and call init 
- Invoke onEvent to trigger a calculation cycle when a user event is received, see [main example](src/main/java/com/fluxtion/example/imperative/helloworld/Main.java)

## Interpreted mode
Fluxtion can run in an interpreted mode, no AOT compilation takes place and the event processor is created on demand 
while executing the program. Steps for interpreted mode

- Create user classes with business logic
- Annotate callback methods
    - **@OnEventHandler** annotation declares the [entry point](src/main/java/com/fluxtion/example/imperative/helloworld/Event_A_Handler.java) of an execution path, triggered by an external event.
    - **@OnTrigger** annotated methods indicate call back methods to be invoked if a parent propagates a change.
    - The return flag from the DataAddition [@OnTrigger](src/main/java/com/fluxtion/example/imperative/helloworld/DataSumCalculator.java) method, calculate,
      indicates if the event should be propagated. In this case the event is only propagated if the sum > 100
- Call Fluxtion.interpret with the user classes to be managed ```Fluxtion.interpret(new BreachNotifier())```
- Call init on the interpreted processor
- Invoke onEvent [main example](src/main/java/com/fluxtion/example/imperative/helloworld/Main.java) to trigger a calculation cycle

## Generated event processor source
The AOT generated event processor source file is here [BreachNotifierProcessor](src/main/java/com/fluxtion/example/imperative/helloworld/generated/BreachNotifierProcessor.java)

## Node diagram of event processor
The AOT processing generates a diagram of the event processor graph that can be very helpful when graphs become
complicated.

![](src/main/resources/com/fluxtion/example/imperative/helloworld/generated/BreachNotifierProcessor.png)

# Executing
Running the program will print the following content to console:

```log
sum:34.4
sum:86.5
sum:157.1
WARNING DataSumCalculator value is greater than 100 sum = 157.1
sum:64.5
```
# User classes
The application has the following classes:

* Event classes: [Event_B](src/main/java/com/fluxtion/example/imperative/helloworld/Event_A.java) [Event_B](src/main/java/com/fluxtion/example/imperative/helloworld/Event_A.java)
* Event Handlers: [Event_B_Handler](src/main/java/com/fluxtion/example/imperative/helloworld/Event_A_Handler.java) [Event_B_Handler](src/main/java/com/fluxtion/example/imperative/helloworld/Event_B_Handler.java)
* Calculation: [DataSumCalculator](src/main/java/com/fluxtion/example/imperative/helloworld/DataSumCalculator.java) Calculates the sum when its annotate trigger method is called. Returns true if the sum > 100 to indicate a propagation notification is required
* Output: [BreachNotifier](src/main/java/com/fluxtion/example/imperative/helloworld/BreachNotifier.java) prints to console when its trigger method is invoked

User classes are arranged in a normal object graph, the annotations indicate the callback methods. The event propagation
follows the reverse path of the reference links from the event handler -> calculation -> output. 

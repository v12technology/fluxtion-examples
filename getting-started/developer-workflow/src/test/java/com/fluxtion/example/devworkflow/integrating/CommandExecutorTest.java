package com.fluxtion.example.devworkflow.integrating;

import com.fluxtion.compiler.Fluxtion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.LongAdder;

class CommandExecutorTest {

    @Test
    public void testPermission(){
        var processor = Fluxtion.interpret(new CommandExecutor(new CommandAuthorizerNode()));
        processor.init();

        CommandAuthorizer commandAuthorizer = processor.getExportedService();
        commandAuthorizer.authorize(new CommandPermission("admin", "shutdown"));
        commandAuthorizer.authorize(new CommandPermission("admin", "listUser"));
        commandAuthorizer.authorize(new CommandPermission("Aslam", "listUser"));
        commandAuthorizer.authorize(new CommandPermission("Puck", "createMischief"));

        LongAdder longAdder = new LongAdder();
        processor.onEvent(new AdminCommand("admin", "shutdown", longAdder::increment));
        Assertions.assertEquals(1, longAdder.intValue());

        processor.onEvent(new AdminCommand("Aslam", "listUser", longAdder::increment));
        Assertions.assertEquals(2, longAdder.intValue());

        processor.onEvent(new AdminCommand("Puck", "createMischief", longAdder::increment));
        Assertions.assertEquals(3, longAdder.intValue());

        processor.onEvent(new AdminCommand("Aslam", "shutdown", longAdder::increment));
        Assertions.assertEquals(3, longAdder.intValue());

        commandAuthorizer.removeAuthorized(new CommandPermission("Puck", "createMischief"));
        processor.onEvent(new AdminCommand("Puck", "createMischief", longAdder::increment));
        Assertions.assertEquals(3, longAdder.intValue());
    }
}
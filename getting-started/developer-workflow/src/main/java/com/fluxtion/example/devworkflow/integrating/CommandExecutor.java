package com.fluxtion.example.devworkflow.integrating;

import com.fluxtion.runtime.annotations.NoTriggerReference;
import com.fluxtion.runtime.annotations.OnEventHandler;
import lombok.Data;

@Data
public class CommandExecutor {
    @NoTriggerReference
    private final CommandAuthorizerNode commandAuthorizer;

    @OnEventHandler
    public boolean executeCommand(AdminCommand command) {
        boolean authorized = commandAuthorizer.isAuthorized(command);
        if (authorized) {
            command.commandToExecute().run();
        }
        return authorized;
    }
}

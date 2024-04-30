package com.fluxtion.example.devworkflow.experiment;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoTriggerReference;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Data;

public class MainExperiment {
    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new CommandExecutor(new CommandAuthorizerNode()));
        processor.init();

        CommandAuthorizer commandAuthorizer = processor.getExportedService();
        commandAuthorizer.authorize(new CommandPermission("admin", "shutdown"));
        commandAuthorizer.authorize(new CommandPermission("admin", "listUser"));
        commandAuthorizer.authorize(new CommandPermission("Aslam", "listUser"));
        commandAuthorizer.authorize(new CommandPermission("Puck", "createMischief"));

        commandAuthorizer.dumpMap();

        processor.onEvent(new AdminCommand("admin", "shutdown", () -> System.out.println("executing shutdown command")));
        processor.onEvent(new AdminCommand("Aslam", "listUser", () -> System.out.println("executing listUser command")));
        processor.onEvent(new AdminCommand("Puck", "createMischief", () -> System.out.println("move the stool")));
        processor.onEvent(new AdminCommand("Aslam", "shutdown", () -> System.out.println("executing shutdown command")));

        commandAuthorizer.removeAuthorized(new CommandPermission("Puck", "createMischief"));
        commandAuthorizer.dumpMap();
        processor.onEvent(new AdminCommand("Puck", "createMischief", () -> System.out.println("move the stool")));
    }

    public interface CommandAuthorizer {
        boolean authorize(CommandPermission commandPermission);

        boolean removeAuthorized(CommandPermission commandPermission);

        //used for testing
        void dumpMap();
    }

    public static class CommandAuthorizerNode implements @ExportService CommandAuthorizer {
        private transient final Multimap<String, String> permissionMap = HashMultimap.create();

        @Override
        public boolean authorize(CommandPermission commandPermission) {
            permissionMap.put(commandPermission.user, commandPermission.command);
            return false;
        }

        @Override
        public boolean removeAuthorized(CommandPermission commandPermission) {
            permissionMap.remove(commandPermission.user, commandPermission.command);
            return false;
        }

        @Override
        public void dumpMap() {
            System.out.println("""
                    
                    Permission map
                    --------------------
                    %s
                    --------------------
                    """.formatted(permissionMap.toString()));
        }

        boolean isAuthorized(AdminCommand adminCommand) {
            return permissionMap.containsEntry(adminCommand.user, adminCommand.command);
        }
    }

    @Data
    public static class CommandExecutor {
        @NoTriggerReference
        private final CommandAuthorizerNode commandAuthorizer;

        @OnEventHandler
        public boolean executeCommand(AdminCommand command) {
            boolean authorized = commandAuthorizer.isAuthorized(command);
            if (authorized) {
                System.out.println("Executing command " + command);
                command.commandToExecute().run();
            } else {
                System.out.println("FAILED authorization for command " + command);
            }
            return authorized;
        }
    }

    public record CommandPermission(String user, String command) { }

    public record AdminCommand(String user, String command, Runnable commandToExecute) {
        @Override
        public String toString() {
            return "AdminCommand{user='" + user + '\'' + ", command='" + command + '\'' +'}';
        }
    }
}

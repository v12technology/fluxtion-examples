package com.fluxtion.example.devworkflow.integrating;

public record AdminCommand(String user, String command, Runnable commandToExecute) {

    @Override
    public String toString() {
        return "AdminCommand{" +
                "user='" + user + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}

package com.fluxtion.example.devworkflow.integrating;

public interface CommandAuthorizer {
    boolean authorize(CommandPermission commandPermission);

    boolean removeAuthorized(CommandPermission commandPermission);
}

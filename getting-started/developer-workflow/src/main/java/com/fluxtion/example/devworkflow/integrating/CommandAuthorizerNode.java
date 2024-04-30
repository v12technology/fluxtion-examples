package com.fluxtion.example.devworkflow.integrating;

import com.fluxtion.runtime.annotations.ExportService;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class CommandAuthorizerNode implements @ExportService CommandAuthorizer {
    private transient final Multimap<String, String> permissionMap = HashMultimap.create();

    @Override
    public boolean authorize(CommandPermission commandPermission) {
        permissionMap.put(commandPermission.user(), commandPermission.command());
        return false;
    }

    @Override
    public boolean removeAuthorized(CommandPermission commandPermission) {
        permissionMap.remove(commandPermission.user(), commandPermission.command());
        return false;
    }

    boolean isAuthorized(AdminCommand adminCommand) {
        return permissionMap.containsEntry(adminCommand.user(), adminCommand.command());
    }
}

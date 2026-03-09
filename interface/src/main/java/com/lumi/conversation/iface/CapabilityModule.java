package com.lumi.conversation.iface;

/**
 * Defines the contract for a Lumi capability module.
 * All binary/WASM modules must implement this interface.
 */
public interface CapabilityModule {

    String getName();

    String getVersion();

    String[] getPermissions();

    String execute(String input);
}

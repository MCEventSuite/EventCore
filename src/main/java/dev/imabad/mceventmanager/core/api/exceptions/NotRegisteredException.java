package dev.imabad.mceventmanager.core.api.exceptions;

import dev.imabad.mceventmanager.core.api.IRegistry;

public class NotRegisteredException extends RuntimeException {

    public NotRegisteredException(IRegistry registry, String item) {
        super("Could not find " + item + " in " + registry.getName() + " registry");
    }
}

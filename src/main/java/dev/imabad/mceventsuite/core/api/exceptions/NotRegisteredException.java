package dev.imabad.mceventsuite.core.api.exceptions;

import dev.imabad.mceventsuite.core.api.IRegistry;

public class NotRegisteredException extends RuntimeException {

    public NotRegisteredException(IRegistry registry, String item) {
        super("Could not find " + item + " in " + registry.getName() + " registry");
    }
}

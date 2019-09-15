package dev.imabad.mceventsuite.core.api.exceptions;

import dev.imabad.mceventsuite.core.api.modules.Module;

public class CircularDependencyException extends RuntimeException {

    public CircularDependencyException(Module loadingModule, Module circularModule) {
        super("Circular dependency detected! " + loadingModule.getClass().getName() + " has " + circularModule.getClass().getName() + " as a dependency");
    }
}

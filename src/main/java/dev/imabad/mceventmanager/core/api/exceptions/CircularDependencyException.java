package dev.imabad.mceventmanager.core.api.exceptions;

import dev.imabad.mceventmanager.core.api.modules.Module;

public class CircularDependencyException extends RuntimeException {

    public CircularDependencyException(Module loadingModule, Module circularModule) {
        super("Circular dependency detected! " + loadingModule.getClass().getName() + " has " + circularModule.getClass().getName() + " as a dependency");
    }
}

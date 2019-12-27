package dev.imabad.mceventsuite.core;

import dev.imabad.mceventsuite.core.api.events.CoreEvent;
import dev.imabad.mceventsuite.core.registries.EventRegistry;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventRegistryTest {

    private EventRegistry eventRegistry = new EventRegistry();

    class TestEvent extends CoreEvent {}

    @Test
    public void firingAnEventWorks() {
        CompletableFuture future = new CompletableFuture();
        eventRegistry.registerListener(TestEvent.class, (event) -> {
            future.complete(true);
        });
        eventRegistry.handleEvent(new TestEvent());
        try {
            assertEquals(future.get(), true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}

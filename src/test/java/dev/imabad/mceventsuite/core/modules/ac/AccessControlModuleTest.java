package dev.imabad.mceventsuite.core.modules.ac;

import dev.imabad.mceventsuite.core.api.commands.EventCommandBuilder;
import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.ac.db.AccessControlSetting;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class AccessControlModuleTest {

    private AccessControlModule module = new AccessControlModule();
    private EventRank defaultRank = new EventRank(0, "default", "", "", Collections.emptyList());
    private EventRank ticketRank = new EventRank(1, "ticket", "", "", Collections.emptyList());
    private EventRank modRank = new EventRank(2, "mod", "", "", Collections.emptyList());
    private EventRank adminRank = new EventRank(3, "admin", "", "", Collections.emptyList());

    @Test
    public void deniesIfEmpty() {
        module.setAccessControlSettings(Collections.emptyList());
        assertEquals(module.checkIfAllowed(defaultRank), AccessControlResponse.DENIED);
        assertEquals(module.checkIfAllowed(ticketRank), AccessControlResponse.DENIED);
        assertEquals(module.checkIfAllowed(modRank), AccessControlResponse.DENIED);
        assertEquals(module.checkIfAllowed(adminRank), AccessControlResponse.DENIED);
    }

    @Test
    public void deniesDefault() {
        module.setAccessControlSettings(Collections.singletonList(new AccessControlSetting(ticketRank)));
        assertEquals(module.checkIfAllowed(defaultRank), AccessControlResponse.DENIED);
        assertEquals(module.checkIfAllowed(ticketRank), AccessControlResponse.ALLOWED);
        assertEquals(module.checkIfAllowed(modRank), AccessControlResponse.ALLOWED);
        assertEquals(module.checkIfAllowed(adminRank), AccessControlResponse.ALLOWED);
    }

    @Test
    public void acceptsAfterTime() throws InterruptedException {
        module.setAccessControlSettings(Collections.singletonList(new AccessControlSetting(ticketRank, System.currentTimeMillis() + 2000)));
        assertFalse(module.checkIfAllowed(defaultRank).isAllowed());
        assertFalse(module.checkIfAllowed(ticketRank).isAllowed());
        assertFalse(module.checkIfAllowed(modRank).isAllowed());
        assertFalse(module.checkIfAllowed(adminRank).isAllowed());
        Thread.sleep(2000);
        assertFalse(module.checkIfAllowed(defaultRank).isAllowed());
        assertTrue(module.checkIfAllowed(ticketRank).isAllowed());
        assertTrue(module.checkIfAllowed(modRank).isAllowed());
        assertTrue(module.checkIfAllowed(adminRank).isAllowed());
    }
}

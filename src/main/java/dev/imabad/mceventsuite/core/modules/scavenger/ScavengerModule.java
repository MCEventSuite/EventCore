package dev.imabad.mceventsuite.core.modules.scavenger;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.eventpass.EventPassModule;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassDAO;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassReward;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.modules.mysql.events.MySQLLoadedEvent;
import dev.imabad.mceventsuite.core.modules.scavenger.db.ScavengerDAO;
import dev.imabad.mceventsuite.core.modules.scavenger.db.ScavengerHuntPlayer;
import dev.imabad.mceventsuite.core.modules.scavenger.db.ScavengerLocation;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;

import java.util.*;

public class ScavengerModule extends Module {

    private List<ScavengerLocation> locations;
    private MySQLDatabase mySQLDatabase;
    private ScavengerDAO dao;

    @Override
    public String getName() {
        return "scavenger";
    }

    @Override
    public void onEnable() {
        mySQLDatabase = EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase();
        EventCore.getInstance().getEventRegistry().registerListener(MySQLLoadedEvent.class, mySQLLoadedEvent -> {
            dao = new ScavengerDAO(mySQLDatabase);
            mySQLDatabase.registerDAOs(dao);
            locations = dao.getLocations();
        });
    }

    @Override
    public void onDisable() {

    }

    public List<ScavengerLocation> getLocations() {
        return locations;
    }


    public static Component foundCard(String cardName, int xp){
        return Component.text("--------------------------------------------")
                .color(NamedTextColor.BLUE)
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text("+").append(Component.text(xp)).append(Component.text("XP")).color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD))
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text("You found the ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(cardName).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD))
                .append(Component.text(" card! ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.text("It was added to your collection.").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text("Click here").color(NamedTextColor.GREEN).decorate(TextDecoration.UNDERLINED).clickEvent(ClickEvent.openUrl("https://pass.cubedcon.com")))
                .append(Component.text(" to view the Event Pass rewards online").color(NamedTextColor.AQUA))
                .append(Component.newline())
                .append(Component.newline())
                .color(NamedTextColor.BLUE)
                .append(Component.text("--------------------------------------------"));
    }

    public void findCard(ScavengerLocation scavengerLocation, UUID uuid, Audience audience) {
        EventCore.getInstance().getEventPlayerManager().getPlayer(uuid).ifPresent(player -> {
            Set<ScavengerLocation> foundLocations = dao.getPlayerFoundLocations(player);
            ScavengerHuntPlayer scavengerHuntPlayer = dao.getOrCreateScavengerHuntPlayer(player);
            if(foundLocations.stream().noneMatch(location -> location.getName().equals(scavengerLocation.getName()))){
                foundLocations.add(scavengerLocation);
                scavengerHuntPlayer.setFoundLocations(foundLocations);
                dao.saveScavengerHuntPlayer(scavengerHuntPlayer);
                int total = getLocations().size();
                Component mainTitle = Component.text("You found a card!").color(NamedTextColor.GREEN);
                Component subTitle = Component.text(foundLocations.size()).append(Component.text("/")).append(Component.text(total)).append(Component.text(" Collected")).color(NamedTextColor.YELLOW);
                audience.showTitle(Title.title(mainTitle, subTitle));
                int xp = foundLocations.size() == total ? 600 : 100;
                audience.sendMessage(foundCard(scavengerLocation.getName(), xp));
                EventCore.getInstance().getModuleRegistry().getModule(EventPassModule.class).awardXP(player, xp, audience, null, false);
            } else {
                audience.sendMessage(Component.text("You have already found this card.").color(NamedTextColor.RED));
            }
        });
    }

    @Override
    public List<Class<? extends Module>> getDependencies() {
        return Collections.singletonList(MySQLModule.class);
    }
}

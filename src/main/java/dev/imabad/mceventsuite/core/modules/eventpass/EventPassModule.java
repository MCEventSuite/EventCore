package dev.imabad.mceventsuite.core.modules.eventpass;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.announcements.db.ScheduledAnnouncementDAO;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassDAO;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassPlayer;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassReward;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassUnlockedReward;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.modules.mysql.events.MySQLLoadedEvent;
import dev.imabad.mceventsuite.core.modules.redis.RedisChannel;
import dev.imabad.mceventsuite.core.modules.redis.RedisModule;
import dev.imabad.mceventsuite.core.modules.redis.messages.players.UpdatePlayerXPMessage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventPassModule extends Module {

    public static Component xpGiven(int amount, String reason){
        return Component.text("You earned ")
                .color(NamedTextColor.GREEN)
                .append(Component.text(amount)
                        .append(Component.text("XP"))
                        .color(NamedTextColor.YELLOW))
                .append(Component.text("!")
                        .color(NamedTextColor.GREEN))
                .append(Component.newline())
                .append(Component.text(reason)
                        .color(NamedTextColor.BLUE));
    }

    public static Component levelUp(int newLevel, EventPassReward unlocked){
        Component component = Component.text("--------------------------------------------")
                .color(NamedTextColor.DARK_PURPLE)
                .append(Component.newline())
                .append(Component.text("LEVEL UP!").decorate(TextDecoration.BOLD).color(NamedTextColor.YELLOW))
                .append(Component.text(" You reached Event Pass").color(NamedTextColor.GOLD))
                .append(Component.text(" Level " + newLevel).color(NamedTextColor.YELLOW))
                .append(Component.text("!").color(NamedTextColor.GOLD))
                .append(Component.newline());
        if(unlocked != null){
            component = component.append(Component.text("You unlocked").color(NamedTextColor.GOLD))
            .append(Component.text(" " + unlocked.getName() + " " + unlocked.getDescription()).color(NamedTextColor.LIGHT_PURPLE))
            .append(Component.newline());
        }
       component = component.append(Component.newline())
                .append(Component.text("Click here").color(NamedTextColor.GREEN).decorate(TextDecoration.UNDERLINED).clickEvent(ClickEvent.openUrl("https://pass.cubedcon.com")))
                .append(Component.text(" to view the Event Pass rewards online").color(NamedTextColor.BLUE))
                .append(Component.newline())
                .color(NamedTextColor.DARK_PURPLE)
                .append(Component.text("--------------------------------------------"));
        return component;
    }

    private MySQLDatabase mySQLDatabase;
    private EventPassDAO dao;
    private List<EventPassReward> eventPassRewards;

    public void awardXP(EventPlayer player, int amount, Audience audience, String reason){
        EventPassPlayer eventPassPlayer = dao.getOrCreateEventPass(player);
        boolean wentUpLevel = eventPassPlayer.addXP(amount);
        dao.saveEventPassPlayer(eventPassPlayer);
        audience.sendMessage(EventPassModule.xpGiven(amount, reason));
        if(wentUpLevel){
            int newLevel = eventPassPlayer.levelFromXP();
            Optional<EventPassReward> eventPassRewards = getEventPassRewards().stream().filter(eventPassReward -> eventPassReward.getRequiredLevel() == newLevel).findFirst();
            eventPassRewards.ifPresent(reward -> {
                EventPassUnlockedReward unlockedReward = new EventPassUnlockedReward(reward, player);
                dao.saveUnlockedReward(unlockedReward);
            });
            Component message;
            message = eventPassRewards.map(eventPassReward -> EventPassModule.levelUp(newLevel, eventPassReward)).orElseGet(() -> EventPassModule.levelUp(newLevel, null));
            audience.sendMessage(message);
            EventCore.getInstance().getModuleRegistry().getModule(RedisModule.class).publishMessage(RedisChannel.GLOBAL, new UpdatePlayerXPMessage(player.getUUID(), newLevel));
        }
    }

    @Override
    public String getName() {
        return "module";
    }

    @Override
    public void onEnable() {
        mySQLDatabase = EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase();
        EventCore.getInstance().getEventRegistry().registerListener(MySQLLoadedEvent.class, mySQLLoadedEvent -> {
            dao = new EventPassDAO(mySQLDatabase);
            mySQLDatabase.registerDAOs(dao);
            eventPassRewards = dao.getRewards(EventCore.getInstance().getConfig().getCurrentYearAsInt());
        });
    }

    @Override
    public void onDisable() {
        eventPassRewards.clear();
    }

    @Override
    public List<Class<? extends Module>> getDependencies() {
        return Arrays.asList(MySQLModule.class);
    }

    public List<EventPassReward> getEventPassRewards() {
        return eventPassRewards;
    }
}

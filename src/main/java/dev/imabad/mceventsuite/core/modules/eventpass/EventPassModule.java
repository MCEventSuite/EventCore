package dev.imabad.mceventsuite.core.modules.eventpass;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.announcements.db.ScheduledAnnouncementDAO;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassDAO;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassPlayer;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassReward;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.modules.mysql.events.MySQLLoadedEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EventPassModule extends Module {

    public static Component xpGiven(int amount){
        return Component.text(amount).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).append(Component.text(" XP earned!").color(NamedTextColor.BLUE));
    }

    public static Component levelUp(int newLevel){
        return Component.text("You've now leveled up to level ").color(NamedTextColor.BLUE).append(Component.text(newLevel).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)).append(Component.text("!").color(NamedTextColor.BLUE));
    }

    public static Component unlocked(EventPassReward unlocked){
        return Component.text("You've unlocked the ").color(NamedTextColor.BLUE).append(Component.text(unlocked.getName()).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)).append(Component.text("!").color(NamedTextColor.BLUE));
    }

    private MySQLDatabase mySQLDatabase;
    private EventPassDAO dao;
    private List<EventPassReward> eventPassRewards;

    public void awardXP(EventPlayer player, int amount, Audience audience){
        EventPassPlayer eventPassPlayer = dao.getOrCreateEventPass(player);
        boolean wentUpLevel = eventPassPlayer.addXP(amount);
        dao.saveEventPassPlayer(eventPassPlayer);
        audience.sendActionBar(EventPassModule.xpGiven(amount));
        if(wentUpLevel){
            int newLevel = eventPassPlayer.levelFromXP();
            audience.sendMessage(EventPassModule.levelUp(newLevel));
            audience.playSound(Sound.sound(Key.key("minecraft:ui.toast.challenge_complete"), Sound.Source.AMBIENT, 1f, 1f));
            List<EventPassReward> eventPassRewards = getEventPassRewards().stream().filter(eventPassReward -> eventPassReward.getRequiredLevel() == newLevel).collect(Collectors.toList());
            eventPassRewards.forEach(eventPassReward -> audience.sendMessage(EventPassModule.unlocked(eventPassReward)));
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
            eventPassRewards = dao.getRewards();
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

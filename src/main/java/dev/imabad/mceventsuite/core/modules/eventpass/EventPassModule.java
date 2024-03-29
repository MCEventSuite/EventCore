package dev.imabad.mceventsuite.core.modules.eventpass;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.announcements.db.ScheduledAnnouncementDAO;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassDAO;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassPlayer;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassReward;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassUnlockedReward;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.modules.mysql.dao.RankDAO;
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
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.*;
import java.util.stream.Collectors;

public class EventPassModule extends Module {

    private static final HashMap<Integer, Integer> LEVELS = new HashMap<>();

    static {
        LEVELS.put(0, 0);
        for(int i = 1; i < 51; i++) {
            int xp = experience(i);
            LEVELS.put(xp, i);
        }
    }

    public static int experience(int level){
        if(level == 0){
            return 0;
        }
        if(level == 1){
            return 500;
        }
        double pastExperience = experience(level - 1);
        pastExperience = pastExperience + (500 + ((level - 1) * 50));
        return (int) Math.round(pastExperience);
    }

    public static int levelFromExperience(int experience) {
        return LEVELS.get(LEVELS.keySet().stream().min(Comparator.comparingInt(i -> i > experience ? Integer.MAX_VALUE : Math.abs(i - experience)))
                .orElse(0));
    }

    public static Component xpGiven(int amount, String reason){
        return Component.newline()
                .append(Component.text("+")
                        .append(Component.text(amount))
                        .append(Component.text("XP"))
                        .color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD)
                ).append(Component.newline())
                .append(Component.text(reason)
                        .color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.newline());
    }

    public static Component levelUp(int newLevel, EventPassReward unlocked){
        Component component = Component.text("--------------------------------------------")
                .color(NamedTextColor.BLUE)
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text("LEVEL UP!").decorate(TextDecoration.BOLD).color(NamedTextColor.YELLOW))
                .append(Component.text(" You reached Event Pass").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" Level " + newLevel).color(NamedTextColor.YELLOW))
                .append(Component.text("!").color(NamedTextColor.GOLD))
                .append(Component.newline());
        if(unlocked != null){
            component = component.append(Component.text("You unlocked").color(NamedTextColor.LIGHT_PURPLE))
            .append(Component.text(" " + unlocked.getName() + " " + unlocked.getDescription()).color(NamedTextColor.GOLD))
            .append(Component.newline());
        }
       component = component.append(Component.newline())
                .append(Component.text("Click here").color(NamedTextColor.GREEN).decorate(TextDecoration.UNDERLINED).clickEvent(ClickEvent.openUrl("https://pass.cubedcon.com")))
                .append(Component.text(" to view the Event Pass rewards online").color(NamedTextColor.AQUA))
                .append(Component.newline())
                .append(Component.newline())
                .color(NamedTextColor.BLUE)
                .append(Component.text("--------------------------------------------"));
        return component;
    }

    private MySQLDatabase mySQLDatabase;
    private EventPassDAO dao;
    private List<EventPassReward> eventPassRewards;

    public void awardXP(EventPlayer player, int amount, Audience audience, String reason){
        awardXP(player, amount, audience, reason, true);
    }

    public void awardXP(EventPlayer player, int amount, Audience audience, String reason, boolean sendMessage){
        EventPassPlayer eventPassPlayer = dao.getOrCreateEventPass(player);
        boolean wentUpLevel = eventPassPlayer.addXP(amount);
        dao.saveEventPassPlayer(eventPassPlayer);
        if(sendMessage)
            audience.sendMessage(EventPassModule.xpGiven(amount, reason));
        if(wentUpLevel){
            int newLevel = eventPassPlayer.levelFromXP();
            List<EventPassReward> unlockedRewards = dao.getUnlockedRewards(player).stream().map(EventPassUnlockedReward::getUnlockedReward).collect(Collectors.toList());
            List<EventPassReward> rewards = getEventPassRewards().stream().filter(eventPassReward -> eventPassReward.getRequiredLevel() > 0 && eventPassReward.getRequiredLevel() <= newLevel && !unlockedRewards.contains(eventPassReward)).sorted(Comparator.comparingInt(EventPassReward::getRequiredLevel)).collect(Collectors.toList());
            for(EventPassReward reward : rewards) {
                final int rank = reward.getEligible_rank();
                if(rank != 0) {
                    final Optional<EventRank> eventRankOptional = EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase()
                            .getDAO(RankDAO.class).getRanks().stream().filter((r) -> r.getId() == rank).findFirst();
                    if(!eventRankOptional.isPresent()) {
                        throw new IllegalArgumentException("Reward " + reward.getId() + " has rank of " + reward.getEligible_rank() + " but rank does not exist!");
                    }
                    final EventRank eventRank = eventRankOptional.get();
                    if(player.getRank().getPower() < eventRank.getPower()) {
                        audience.sendMessage(Component.text("You could've unlocked " + reward.getName() + " " + reward.getDescription() + " " +
                                "but you need " + eventRank.getName() + "! Consider purchasing from ").color(NamedTextColor.RED)
                                .append(Component.text("store.cubedcon.com").color(NamedTextColor.GREEN)));
                        rewards.remove(reward);
                        continue;
                    }
                }
                EventPassUnlockedReward unlockedReward = new EventPassUnlockedReward(reward, player);
                dao.saveUnlockedReward(unlockedReward);
            }
            EventPassReward rewardForMessage = rewards.size() > 0 ? rewards.get(rewards.size() - 1) : null;
            Component message;
            message = EventPassModule.levelUp(newLevel, rewardForMessage);
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
            System.out.println("Loaded " + eventPassRewards.size() + " rewards for current year!");
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

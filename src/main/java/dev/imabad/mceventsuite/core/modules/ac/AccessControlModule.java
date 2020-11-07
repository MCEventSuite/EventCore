package dev.imabad.mceventsuite.core.modules.ac;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.ac.db.AccessControlDAO;
import dev.imabad.mceventsuite.core.modules.ac.db.AccessControlSetting;
import dev.imabad.mceventsuite.core.modules.ac.db.PlayerBan;
import dev.imabad.mceventsuite.core.modules.ac.db.PlayerBanDAO;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.modules.mysql.events.MySQLLoadedEvent;
import dev.imabad.mceventsuite.core.modules.redis.RedisMessageListener;
import dev.imabad.mceventsuite.core.modules.redis.RedisModule;
import dev.imabad.mceventsuite.core.util.TimeUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AccessControlModule extends Module {

    private MySQLDatabase mySQLDatabase;
    private List<AccessControlSetting> accessControlSettings;

    @Override
    public String getName() {
        return "ac";
    }

    @Override
    public void onEnable() {
        mySQLDatabase = EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase();
        EventCore.getInstance().getEventRegistry().registerListener(MySQLLoadedEvent.class, mySQLLoadedEvent -> {
//            mySQLDatabase.registerEntity(AccessControlSetting.class);
            mySQLDatabase.registerDAOs(new AccessControlDAO(mySQLDatabase), new PlayerBanDAO(mySQLDatabase));
            accessControlSettings = mySQLDatabase.getDAO(AccessControlDAO.class).getAccessControlSettings();
        });
        EventCore.getInstance().getModuleRegistry().getModule(RedisModule.class).registerListener(RefreshAccessControlListMessage.class, new RedisMessageListener<>(refreshAccessControlListMessage -> {
            accessControlSettings = mySQLDatabase.getDAO(AccessControlDAO.class).getAccessControlSettings();
        }));
    }

    @Override
    public void onDisable() {

    }

    public void setAccessControlSettings(List<AccessControlSetting> accessControlSettings) {
        this.accessControlSettings = accessControlSettings;
    }

    @Override
    public List<Class<? extends Module>> getDependencies() {
        return Arrays.asList(RedisModule.class, MySQLModule.class);
    }

    public AccessControlResponse checkIfAllowed(EventRank eventRank){
        if(accessControlSettings.isEmpty()){
            return AccessControlResponse.DENIED;
        }
        Optional<AccessControlSetting> optionalAccessControlSetting = accessControlSettings.stream().filter(accessControlSetting -> eventRank.getPower() <= accessControlSetting.getRank().getPower()).findFirst();
        if(optionalAccessControlSetting.isPresent()){
            AccessControlSetting accessControlSetting = optionalAccessControlSetting.get();
            if(accessControlSetting.getUnlockTime() > System.currentTimeMillis()){
                return new AccessControlResponse(false, accessControlSetting.getDenyMessage());
            } else if (accessControlSetting.getUnlockTime() == -1){
                return new AccessControlResponse(false, accessControlSetting.getDenyMessage());
            }
        } else {
            return AccessControlResponse.ALLOWED;
        }
        return AccessControlResponse.ALLOWED;
    }

    public PlayerBan getActivePlayerBan(EventPlayer player){
        return mySQLDatabase.getDAO(PlayerBanDAO.class).getPlayerActiveBan(player);
    }

    public AccessControlResponse checkIfAllowed(EventPlayer eventPlayer){
        AccessControlResponse rankResponse = checkIfAllowed(eventPlayer.getRank());
        if(rankResponse.isAllowed()){
            PlayerBan playerBan = getActivePlayerBan(eventPlayer);
            if(playerBan != null){
                return new AccessControlResponse(false, playerBan.kickMessage());
            }
            return AccessControlResponse.ALLOWED;
        }
        return rankResponse;
    }
}

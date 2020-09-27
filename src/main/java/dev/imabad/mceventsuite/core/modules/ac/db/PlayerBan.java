package dev.imabad.mceventsuite.core.modules.ac.db;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.util.TimeUtils;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "player_bans")
public class PlayerBan {

    private String id;
    private EventPlayer player;
    private EventPlayer banner;
    private String reason;
    private long expires;
    private long banDate;
    private String extraData;

    public PlayerBan(EventPlayer player, EventPlayer banner, String reason, long expires, long banDate) {
        this(player, banner, reason, expires, banDate, "");
    }

    public PlayerBan(EventPlayer player, EventPlayer banner, String reason, long expires, long banDate, String extraData) {
        this.id = UUID.randomUUID().toString();
        this.player = player;
        this.banner = banner;
        this.reason = reason;
        this.expires = expires;
        this.banDate = banDate;
        this.extraData = extraData;
    }

    protected PlayerBan() {

    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }

    @OneToOne
    @JoinColumn(name="player", referencedColumnName = "uuid")
    public EventPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EventPlayer player) {
        this.player = player;
    }

    @OneToOne
    @JoinColumn(name="banner", referencedColumnName = "uuid")
    public EventPlayer getBanner() {
        return banner;
    }

    public void setBanner(EventPlayer banner) {
        this.banner = banner;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public long getBanDate() {
        return banDate;
    }

    public void setBanDate(long banDate) {
        this.banDate = banDate;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String kickMessage(){
        StringBuilder reasonBuilder = new StringBuilder();
        reasonBuilder.append("You have been banned");
        if(getExpires() == -1){
            reasonBuilder.append(" permanently.");
        } else {
            reasonBuilder.append(" for ");
            long durationInMillis = getExpires() - getBanDate();
            reasonBuilder.append(TimeUtils.getFormattedDuration(durationInMillis));
            reasonBuilder.append(".");
        }
        reasonBuilder.append("\n Reason: ");
        reasonBuilder.append(getReason());
        reasonBuilder.append("\n If you believe this to be a mistake please contact a moderator via Discord");
        return reasonBuilder.toString();
    }
}

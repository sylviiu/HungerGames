package tk.shanebee.hg.util;

import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import tk.shanebee.hg.util.Util;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Stream;

public class Voicechat implements VoicechatPlugin {
    public String getPluginId() {
        return "tk.shanebee.hg";
    }

    private VoicechatApi api;
    private static VoicechatServerApi serverApi;


    public void initialize(VoicechatApi api) {
        Util.log("Voicechat initialize called!");
        //this.api = api;
    }

    @Nullable
    public Group group = null;

    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::getSpectatorGroup);
    }

    private Group getSpectatorGroup(VoicechatServerStartedEvent event) {
        serverApi = event.getVoicechat();

        group = serverApi.groupBuilder()
                .setPersistent(true)
                .setName("Spectators")
                .setType(Group.Type.NORMAL)
                .build();

        Util.log("Created Spectators group!");

        return group;
    }

    public void addSpectator(Player player) {
        VoicechatConnection connection = serverApi.getConnectionOf(player.getUniqueId());
        if(connection != null && group != null) {
            Util.log("Adding player " + player.getName() + " to group " + group.getName());
            connection.setGroup(group);
        } else {
            Util.warning("Cannot add player to spectators group because it's null!");
        }
    }

    public void removeSpectator(Player player) {
        VoicechatConnection connection = serverApi.getConnectionOf(player.getUniqueId());
        if(connection != null && connection.getGroup() != null) {
            Util.log("Removing player " + player.getName() + " from group " + connection.getGroup().getName());
            connection.setGroup(null);
        } else {
            Util.log("Not removing player from group because they're in no group!");
        }
    }
}

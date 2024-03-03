package tk.shanebee.hg.util;

import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import tk.shanebee.hg.HG;

public class Voicechat implements VoicechatPlugin {
    private VoicechatApi api;
    @Nullable
    private static VoicechatServerApi serverApi = null;

    @Nullable
    public Group group = null;


    @Override
    public String getPluginId() {
        return HG.PLUGIN_ID;
    }

    @Override
    public void initialize(VoicechatApi api) {
        Util.log("Voicechat initialize called!");
        //this.api = api;
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        Util.log("Registering voicechat events");
        registration.registerEvent(VoicechatServerStartedEvent.class, this::getSpectatorGroup);
    }

    private void getSpectatorGroup(VoicechatServerStartedEvent event) {
        if(serverApi == null)
            serverApi = event.getVoicechat();

        if(group == null)
            group = serverApi.groupBuilder()
                    .setPersistent(false)
                    .setName("Spectators")
                    .setType(Group.Type.NORMAL)
                    .build();

        Util.log("Created Spectators group!");
    }

    public void addSpectator(Player player) {
        assert serverApi != null;
        VoicechatConnection connection = serverApi.getConnectionOf(player.getUniqueId());
        if(connection != null && group != null) {
            Util.log("Adding player " + player.getName() + " to group " + group.getName());
            connection.setGroup(group);
        } else {
            Util.warning("Cannot add player to spectators group because it's null!");
        }
    }

    public void removeSpectator(Player player) {
        assert serverApi != null;
        VoicechatConnection connection = serverApi.getConnectionOf(player.getUniqueId());
        if(connection != null && connection.getGroup() != null) {
            Util.log("Removing player " + player.getName() + " from group " + connection.getGroup().getName());
            connection.setGroup(null);
        } else {
            Util.log("Not removing player from group because they're in no group!");
        }
    }
}

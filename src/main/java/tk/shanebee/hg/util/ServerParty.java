package tk.shanebee.hg.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.bukkit.Bukkit.getServer;

public class ServerParty implements Party{
    @Override
    public boolean hasParty(Player p) {
        return true;
    }

    @Override
    public int partySize(Player p) {
        int PlayerCount = getServer().getOnlinePlayers().size();
        int ReturnedNumber = Math.min(PlayerCount, 24);

        Util.log("partySize requested; has: " + PlayerCount + ", returning: " + ReturnedNumber);

        return ReturnedNumber;
    }

    @Override
    public boolean isOwner(Player p) {
        boolean HasPerm = p.hasPermission("hg.startgames");
        Util.log("has perm: " + HasPerm);
        return HasPerm;
    }

    @Override
    public List<Player> getMembers(Player owner) {
        List<Player> list = (List<Player>) getServer().getOnlinePlayers().stream().collect(toList());
        int size = list.size();

        Util.log("got playerlist of " + size + " people, with: " + list.stream().map(Player::getName).collect(Collectors.joining(", ")));

        if(size > 24) {
            Util.log("returning limited to 24");
            return list.subList(0, 24);
        } else {
            Util.log("returning original of " + size);
            return list;
        }
    }
}

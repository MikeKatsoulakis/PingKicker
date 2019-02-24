package me.mikedev.com.pingkicker.Listeners;

import me.mikedev.com.pingkicker.PingKicker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private PingKicker plugin = PingKicker.getInstance();
    private final int MAX_PING = plugin.MAX_PING;
    private final String logo = plugin.logo;
    private final FileConfiguration config = plugin.getConfig();
    private final float minutes = (float) config.getDouble("minutes");

    /*
     *
     * Join Event Handler
     *
     */

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Player p = e.getPlayer();

        int ping = plugin.getPing(p);
        if(ping > MAX_PING && MAX_PING > 0 && minutes > 0){
            p.kickPlayer(logo + config.getString("kick_message"));
        }

    }

}

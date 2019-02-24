package me.mikedev.com.pingkicker;

import me.mikedev.com.pingkicker.Commands.PingCommand;
import me.mikedev.com.pingkicker.Listeners.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;


public final class PingKicker extends JavaPlugin implements Listener {

    /*
     *
     * Class Instance
     *
     */

    private static PingKicker plugin;
    public static PingKicker getInstance() {return plugin;}

    public final int MAX_PING = getConfig().getInt("max_ping"); // Getting teh max ping to be allowed, set by the user

    public String logo = ChatColor.RED + "" + ChatColor.BOLD + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "Ping!" +
            ChatColor.RED + "" + ChatColor.BOLD + "] "; // To use at the start of every message

    @Override
    public void onEnable() {

        plugin = this; //Instance initialization

        saveDefaultConfig();

        /*
         *
         * Events
         *
         */
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);

        /*
         *
         * Commands
         *
         */
        getCommand("ping").setExecutor(new PingCommand());

        /*
         *
         * Checking the ping of every player, depending on
         * how often the user has defined in the configuration file.
         * -1 -> no checking
         *
         */

        int timeInTicks = 1200*getConfig().getInt("minutes");

        if(timeInTicks > 0){

            new BukkitRunnable() {

                @Override
                public void run() {
                    check();
                }

            }.runTaskTimer(this, 0L, timeInTicks);

        }

    }

    private void check(){
        for(Player p : Bukkit.getOnlinePlayers()){

            int ping = getPing(p); // Getting the ping with our function.

            /*
             *
             * Checking if it is over the MAX ping, and then kicking the player if it is.
             *
             *
             */

            if(ping > MAX_PING && MAX_PING != -1){
                p.kickPlayer(getConfig().getString(logo + "kick_message"));
            }

        }
    }

    /**
     *
     * Getting the ping of a player. Using reflection, to be a bit more version independent
     *
     * @param p = The player we want to get the ping.
     * @return The ping of the player
     *
     */

    public int getPing(Player p){
        int ping = 0;
        try {
            Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
            ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        return ping;
    }

    /*
     *
     * Getting the average ping on teh server, using the function we made earlier #getPing(Player p)
     *
     * @return The Average ping
     *
     */

    public float getAveragePing(){
        int pings = 0;

        for(Player p : Bukkit.getOnlinePlayers()){
            pings += getPing(p);
        }
        return (float) pings/Bukkit.getOnlinePlayers().size();
    }
}

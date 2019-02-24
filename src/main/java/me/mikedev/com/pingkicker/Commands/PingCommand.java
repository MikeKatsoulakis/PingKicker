package me.mikedev.com.pingkicker.Commands;

import me.mikedev.com.pingkicker.PingKicker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    private PingKicker plugin = PingKicker.getInstance();
    private final String logo = plugin.logo;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        /*
         *
         * SubCommands: /ping, /ping reload, /ping [PlayerName], ping average/avg
         * We will have to check for command arguments
         * We will have to check permissions separately
         *
         */

        /*
         * Checking for the length of the arguments
         */

        if(args.length == 0) {

            /*
             * Checking for the permission
             */

            if (sender instanceof Player && (sender.hasPermission("ping.ping") || sender.hasPermission("ping.*"))) {

                /*
                 *
                 * Getting the ping through the function we made in the main class
                 *
                 */

                int ping = plugin.getPing((Player) sender);

                sender.sendMessage(logo + ChatColor.GREEN + "" + ChatColor.BOLD + ping);

            } else {

                /*
                 * Sending a message in case the player does not have the permission needed
                 */

                sender.sendMessage(logo + ChatColor.DARK_RED + "" + ChatColor.BOLD + "You do not have permission!");
            }

        }else {

            /*
             * Checking for the length of the arguments
             */

            if (args.length == 1) {

                /*
                 * Checking for the sub-command
                 */

                if (args[0].equalsIgnoreCase("reload")) {

                    /*
                     * Checking in case the sender is a player, if yes, then we check for the permission
                     */

                    if (sender != null) {

                        /*
                         * Checking for the permission
                         */

                        if (sender instanceof ConsoleCommandSender || sender.hasPermission("ping.reload") || sender.hasPermission("ping.*")) {

                            /*
                             * reloading the config
                             */

                            plugin.reloadConfig();

                        } else {

                            /*
                             * Sending a message in case the player does not have the permission needed
                             */

                            sender.sendMessage(logo + ChatColor.DARK_RED + "" + ChatColor.BOLD + "You do not have permission!");
                        }
                    } else {

                        /*
                         * reloading the config
                         */

                        plugin.reloadConfig();
                    }

                    /*
                     * Checking for the sub-command
                     */

                } else if(args[0].equalsIgnoreCase("average")) {

                    /*
                     * Checking for the permission
                     */

                    if(sender instanceof ConsoleCommandSender || sender.hasPermission("ping.average")  || sender.hasPermission("ping.*")){

                        float avgPing = plugin.getAveragePing(); // The average ping using the appropriate function in the main class

                        sender.sendMessage(logo + ChatColor.GREEN + "" + ChatColor.BOLD + avgPing);

                    } else {

                        /*
                         * Sending a message in case the player does not have the permission needed
                         */

                        sender.sendMessage(logo + ChatColor.DARK_RED + "" + ChatColor.BOLD + "You do not have permission!");
                    }

                }else{

                    /*
                     * Checking if the Player Name the sender typed is actually a player
                     */

                    if (Bukkit.getPlayer(args[0]) != null) {

                        /*
                         * Checking for the permission
                         */

                        if (sender instanceof ConsoleCommandSender || sender.hasPermission("ping.other") || sender.hasPermission("ping.*")) {

                            int ping = plugin.getPing(Bukkit.getPlayer(args[0])); // Getting the ping from our function in the main class

                            sender.sendMessage(logo + ChatColor.GREEN + "" + ChatColor.BOLD + ping);

                        } else {

                            /*
                             * Sending a message in case the player does not have the permission needed
                             */

                            sender.sendMessage(logo + ChatColor.DARK_RED + "" + ChatColor.BOLD + "You do not have permission!");
                        }
                    } else {

                        /*
                         * Sending a message in case the argument was not correct
                         */

                        sender.sendMessage(logo + ChatColor.GOLD + "" + ChatColor.BOLD + "Usage: /ping <reload>/<playername>");
                    }

                }

            } else {

                /*
                 * Sending a message in case the arguments length was not correct
                 */

                sender.sendMessage(logo + ChatColor.GOLD + "" + ChatColor.BOLD + "Usage: /ping <reload>/<playername>");
            }
        }
        return false;
    }
}

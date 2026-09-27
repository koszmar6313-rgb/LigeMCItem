package pl.ligemc.items.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.ligemc.items.LigeMCItems;
import pl.ligemc.items.gui.ItemGui;

public class LigeMCItemsCommand implements CommandExecutor {

    private final LigeMCItems plugin;

    public LigeMCItemsCommand(LigeMCItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Ta komenda jest tylko dla graczy!");
            return true;
        }

        if (!player.hasPermission("ligemcitems.admin")) {
            player.sendMessage("§cNie masz uprawnień!");
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("wand")) {
                player.getInventory().addItem(plugin.getRegionManager().getWandItem());
                player.sendMessage("§aOtrzymałeś Region Wand!");
                return true;
            } else if (args[0].equalsIgnoreCase("region") && args.length > 1 && args[1].equalsIgnoreCase("create")) {
                if (args.length < 3) {
                    player.sendMessage("§cUżycie: /ligemcitems region create <nazwa>");
                    return true;
                }
                plugin.getRegionManager().createRegion(player, args[2]);
                return true;
            }
        }

        ItemGui.openGui(player);
        return true;
    }
}

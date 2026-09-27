package pl.ligemc.items.listeners;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import pl.ligemc.items.LigeMCItems;

public class RegionListener implements Listener {

    private final LigeMCItems plugin;

    public RegionListener(LigeMCItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.GOLDEN_AXE && item.hasItemMeta() &&
            item.getItemMeta().hasDisplayName() &&
            LegacyComponentSerializer.legacyAmpersand().serialize(item.getItemMeta().displayName()).contains("REGION WAND")) {

            if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock() != null) {
                event.setCancelled(true);
                plugin.getRegionManager().setPos1(player, event.getClickedBlock().getLocation());
                player.sendMessage("§aZapisano pozycję 1 (pos1)!");
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
                event.setCancelled(true);
                plugin.getRegionManager().setPos2(player, event.getClickedBlock().getLocation());
                player.sendMessage("§aZapisano pozycję 2 (pos2)!");
            }
        }
    }
}

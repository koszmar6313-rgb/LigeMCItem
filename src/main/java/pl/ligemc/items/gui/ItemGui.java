package pl.ligemc.items.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.ligemc.items.LigeMCItems;

public class ItemGui {

    public static void openGui(Player player) {
        Component title = LegacyComponentSerializer.legacyAmpersand().deserialize("&8LigeMCItems");
        Inventory gui = Bukkit.createInventory(null, 27, title);

        LigeMCItems plugin = LigeMCItems.getInstance();
        gui.setItem(10, plugin.getItemManager().getItem1());
        gui.setItem(11, plugin.getItemManager().getItem2());
        gui.setItem(12, plugin.getItemManager().getItem3());
        gui.setItem(13, plugin.getItemManager().getItem4());
        gui.setItem(14, plugin.getItemManager().getItem5());

        player.openInventory(gui);
    }
}

package pl.ligemc.items.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private final ItemStack item1;
    private final ItemStack item2;
    private final ItemStack item3;
    private final ItemStack item4;
    private final ItemStack item5;

    public ItemManager() {
        this.item1 = createCustomItem(Material.IRON_NUGGET, "&f&lꜱʜᴜʀɪᴋᴇɴ ʟᴏᴅᴜ", List.of(
                "&8x &fPo kliknięciu prawym rzucasz",
                "&8x &fshuriken który &bzamraża &fgracza",
                "&8x &fna 2 sekundy przez co nie może się ruszać"
        ));

        this.item2 = createCustomItem(Material.LIME_DYE, "&a&lᴛᴀʀᴄᴢᴀ ᴇɴᴇʀɢɪɪ ʟʟᴏʏᴅᴀ", List.of(
                "&8x &fPo kliknięciu odbijasz uderzenia",
                "&8x &fgraczy którzy cię biją przez 3 sekundy"
        ));

        this.item3 = createCustomItem(Material.RED_DYE, "&#2F34F6&lɴᴜɴᴄᴢᴀᴋᴏ ᴊᴀʏᴀ", List.of(
                "&8x &fPo uderzeniu gracza przyzywasz na niego",
                "&8x &e3 potężne pioruny &fktóre zabierają mu 2 serca",
                "&8x &fjeżeli gracz ma 2 serca lub mniej pioruny nie działają"
        ));

        this.item4 = createCustomItem(Material.BROWN_DYE, "&x&4&8&3&C&3&C&lᴋᴜʟᴀ ᴢɪᴇᴍɪ ᴄᴏʟᴇ'ᴀ", List.of(
                "&8x &fPo zjedzeniu ziemi powiększasz się do",
                "&8x &f1.5 rozmiaru na 20 sekund i dostajesz &5Odporność III"
        ));

        this.item5 = createCustomItem(Material.BLUE_DYE, "&c&lᴡʏʙᴜᴄʜᴏᴡᴇ ʙᴏᴍʙʏ ᴋᴀɪᴀ", List.of(
                "&8x &fPo kliknięciu prawym używasz bomby",
                "&8x &fodrzuca ona pobliskich graczy a ty",
                "&8x &fstajesz się &enieśmiertelny &fna 5 sekund"
        ));
    }

    private ItemStack createCustomItem(Material material, String name, List<String> loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(name));
            meta.setCustomModelData(1);

            List<Component> lore = new ArrayList<>();
            for (String line : loreLines) {
                lore.add(LegacyComponentSerializer.legacyAmpersand().deserialize(line));
            }
            meta.lore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public boolean isItem1(ItemStack item) { return check(item, Material.IRON_NUGGET); }
    public boolean isItem2(ItemStack item) { return check(item, Material.LIME_DYE); }
    public boolean isItem3(ItemStack item) { return check(item, Material.RED_DYE); }
    public boolean isItem4(ItemStack item) { return check(item, Material.BROWN_DYE); }
    public boolean isItem5(ItemStack item) { return check(item, Material.BLUE_DYE); }

    private boolean check(ItemStack item, Material material) {
        if (item == null || item.getType() != material) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1;
    }

    public ItemStack getItem1() { return item1.clone(); }
    public ItemStack getItem2() { return item2.clone(); }
    public ItemStack getItem3() { return item3.clone(); }
    public ItemStack getItem4() { return item4.clone(); }
    public ItemStack getItem5() { return item5.clone(); }
}

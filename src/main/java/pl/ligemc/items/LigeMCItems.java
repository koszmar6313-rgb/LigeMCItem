package pl.ligemc.items;

import org.bukkit.plugin.java.JavaPlugin;
import pl.ligemc.items.commands.LigeMCItemsCommand;
import pl.ligemc.items.listeners.ItemListener;
import pl.ligemc.items.listeners.RegionListener;
import pl.ligemc.items.managers.CooldownManager;
import pl.ligemc.items.managers.ItemManager;
import pl.ligemc.items.managers.RegionManager;

public final class LigeMCItems extends JavaPlugin {

    private static LigeMCItems instance;
    private ItemManager itemManager;
    private CooldownManager cooldownManager;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.itemManager = new ItemManager();
        this.cooldownManager = new CooldownManager();
        this.regionManager = new RegionManager(this);

        getCommand("ligemcitems").setExecutor(new LigeMCItemsCommand(this));

        getServer().getPluginManager().registerEvents(new ItemListener(this), this);
        getServer().getPluginManager().registerEvents(new RegionListener(this), this);
    }

    public static LigeMCItems getInstance() {
        return instance;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}

package pl.ligemc.items.listeners;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import pl.ligemc.items.LigeMCItems;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ItemListener implements Listener {

    private final LigeMCItems plugin;
    private final Set<UUID> activeShield = new HashSet<>();
    private final Set<UUID> invulnerablePlayers = new HashSet<>();

    public ItemListener(LigeMCItems plugin) {
        this.plugin = plugin;
    }

    private boolean checkRegion(Player player) {
        if (plugin.getRegionManager().isInside(player.getLocation())) {
            String msg = plugin.getConfig().getString("blocked-region-message", "§cNie możesz używać przedmiotów eventowych na tym regionie!");
            player.sendMessage(msg);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (event.getView().title().equals(LegacyComponentSerializer.legacyAmpersand().deserialize("&8LigeMCItems"))) {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player player && event.getCurrentItem() != null) {
                player.getInventory().addItem(event.getCurrentItem().clone());
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        // ITEM 1: SHURIKEN LODU
        if (plugin.getItemManager().isItem1(item)) {
            event.setCancelled(true);
            if (checkRegion(player)) return;

            if (plugin.getCooldownManager().hasCooldown(player, "item1")) {
                player.sendMessage("§cMusisz odczekać jeszcze " + plugin.getCooldownManager().getRemainingSeconds(player, "item1") + "s!");
                return;
            }

            plugin.getCooldownManager().setCooldown(player, "item1", 40);
            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setCustomName("SHURIKEN_LODU");
        }

        // ITEM 2: TARCZA ENERGII LLOYDA
        else if (plugin.getItemManager().isItem2(item)) {
            event.setCancelled(true);
            if (checkRegion(player)) return;

            if (plugin.getCooldownManager().hasCooldown(player, "item2")) {
                player.sendMessage("§cMusisz odczekać jeszcze " + plugin.getCooldownManager().getRemainingSeconds(player, "item2") + "s!");
                return;
            }

            plugin.getCooldownManager().setCooldown(player, "item2", 180);
            activeShield.add(player.getUniqueId());
            player.sendMessage("§aTarcza energii aktywowana na 3 sekundy!");

            Bukkit.getScheduler().runTaskLater(plugin, () -> activeShield.remove(player.getUniqueId()), 60L);
        }

        // ITEM 4: KULA ZIEMI COLE'A
        else if (plugin.getItemManager().isItem4(item)) {
            event.setCancelled(true);
            if (checkRegion(player)) return;

            if (plugin.getCooldownManager().hasCooldown(player, "item4")) {
                player.sendMessage("§cMusisz odczekać jeszcze " + plugin.getCooldownManager().getRemainingSeconds(player, "item4") + "s!");
                return;
            }

            plugin.getCooldownManager().setCooldown(player, "item4", 120);
            player.damage(2.0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 400, 2));

            AttributeInstance scale = player.getAttribute(Attribute.SCALE);
            if (scale != null) scale.setBaseValue(1.5);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (scale != null) scale.setBaseValue(1.0);
            }, 400L);
        }

        // ITEM 5: WYBUCHOWE BOMBY KAIA
        else if (plugin.getItemManager().isItem5(item)) {
            event.setCancelled(true);
            if (checkRegion(player)) return;

            if (plugin.getCooldownManager().hasCooldown(player, "item5")) {
                player.sendMessage("§cMusisz odczekać jeszcze " + plugin.getCooldownManager().getRemainingSeconds(player, "item5") + "s!");
                return;
            }

            plugin.getCooldownManager().setCooldown(player, "item5", 20);
            item.setAmount(item.getAmount() - 1);

            player.getWorld().createExplosion(player.getLocation(), 2.0f, false, false);
            for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                if (entity instanceof Player victim) {
                    Vector dir = victim.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().setY(0.5);
                    victim.setVelocity(dir.multiply(1.5));
                    victim.damage(4.0, player);
                }
            }

            invulnerablePlayers.add(player.getUniqueId());
            Bukkit.getScheduler().runTaskLater(plugin, () -> invulnerablePlayers.remove(player.getUniqueId()), 100L);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        // Efekt Shurikenu Lodu (Trafienie Śnieżką)
        if (event.getDamager() instanceof Snowball snowball && "SHURIKEN_LODU".equals(snowball.getCustomName())) {
            if (event.getEntity() instanceof Player target) {
                target.setFreezeTicks(140);
                target.getWorld().spawnParticle(Particle.SNOWFLAKE, target.getLocation().add(0, 1, 0), 30);
            }
            return;
        }

        // ITEM 3: NUNCZAKO JAYA
        if (event.getDamager() instanceof Player attacker && event.getEntity() instanceof Player victim) {
            ItemStack mainHand = attacker.getInventory().getItemInMainHand();
            if (plugin.getItemManager().isItem3(mainHand)) {
                if (checkRegion(attacker)) return;

                if (!plugin.getCooldownManager().hasCooldown(attacker, "item3")) {
                    if (victim.getHealth() > 4.0) {
                        plugin.getCooldownManager().setCooldown(attacker, "item3", 60);
                        victim.getWorld().strikeLightningEffect(victim.getLocation());
                        victim.getWorld().strikeLightningEffect(victim.getLocation());
                        victim.getWorld().strikeLightningEffect(victim.getLocation());

                        double newHealth = Math.max(4.0, victim.getHealth() - 4.0);
                        victim.setHealth(newHealth);
                    }
                }
            }
        }

        // Odbicie Tarczy Energii Lloyda
        if (event.getEntity() instanceof Player defender && activeShield.contains(defender.getUniqueId())) {
            event.setCancelled(true);
            if (event.getDamager() instanceof Player attacker) {
                attacker.getWorld().strikeLightningEffect(attacker.getLocation());
                attacker.damage(4.0, defender);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && invulnerablePlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}

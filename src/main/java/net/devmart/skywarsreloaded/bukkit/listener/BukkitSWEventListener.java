package net.devmart.skywarsreloaded.bukkit.listener;

import net.devmart.skywarsreloaded.api.enums.DeathCause;
import net.devmart.skywarsreloaded.api.gui.handlers.SWGuiClickHandler;
import net.devmart.skywarsreloaded.api.listener.PlatformSWEventListener;
import net.devmart.skywarsreloaded.api.utils.Item;
import net.devmart.skywarsreloaded.api.utils.SWCoord;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWDroppedItem;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWEntity;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.api.wrapper.event.*;
import net.devmart.skywarsreloaded.api.wrapper.server.SWInventory;
import net.devmart.skywarsreloaded.api.wrapper.world.SWWorld;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.managers.BukkitInventoryManager;
import net.devmart.skywarsreloaded.bukkit.utils.BukkitItem;
import net.devmart.skywarsreloaded.bukkit.wrapper.entity.BukkitSWDroppedItem;
import net.devmart.skywarsreloaded.bukkit.wrapper.event.BukkitSWPlayerFoodLevelChangeEvent;
import net.devmart.skywarsreloaded.bukkit.wrapper.server.BukkitSWInventory;
import net.devmart.skywarsreloaded.core.utils.CoreSWCoord;
import net.devmart.skywarsreloaded.core.wrapper.event.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitSWEventListener implements Listener, PlatformSWEventListener {

    private final BukkitSkyWarsReloaded skywars;

    public BukkitSWEventListener(BukkitSkyWarsReloaded skywars) {
        this.skywars = skywars;
    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        // Get data
        SWAsyncPlayerPreLoginEvent.Result result = SWAsyncPlayerPreLoginEvent.Result.valueOf(event.getLoginResult().name());

        // Fire event
        SWAsyncPlayerPreLoginEvent swEvent = new CoreSWAsyncPlayerPreLoginEvent(event.getUniqueId(), event.getName(),
                event.getAddress(), result, null);

        skywars.getEventManager().callEvent(swEvent);

        // Update changes
        SWAsyncPlayerPreLoginEvent.Result updatedResult = swEvent.getResult();
        AsyncPlayerPreLoginEvent.Result updatedResultBukkit = AsyncPlayerPreLoginEvent.Result.valueOf(updatedResult.name());
        event.setLoginResult(updatedResultBukkit);
        if (!updatedResult.equals(SWAsyncPlayerPreLoginEvent.Result.ALLOWED)) {
            event.setKickMessage(swEvent.getKickMessage());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());

        // Fire Event
        SWPlayerJoinEvent swEvent = new CoreSWPlayerJoinEvent(p, event.getJoinMessage());
        skywars.getEventManager().callEvent(swEvent);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());

        // Fire Event
        SWPlayerQuitEvent swEvent = new CoreSWPlayerQuitEvent(p, event.getQuitMessage());
        skywars.getEventManager().callEvent(swEvent);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());
        SWWorld from = this.skywars.getServer().getWorld(event.getFrom().getName());
        SWWorld to = this.skywars.getServer().getWorld(event.getPlayer().getWorld().getName());

        // Fire Event
        SWPlayerChangedWorldEvent swEvent = new CoreSWPlayerChangedWorldEvent(p, from, to);
        skywars.getEventManager().callEvent(swEvent);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());

        SWCoord location = null;
        Block block = event.getClickedBlock();
        String blockType = "AIR";

        if (block != null) {
            Location loc = block.getLocation();
            location = new CoreSWCoord(skywars.getUtils().getSWWorld(loc.getWorld().getName()), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            blockType = block.getType().name();
        }
        SWPlayerInteractEvent.Action action = SWPlayerInteractEvent.Action.valueOf(event.getAction().name());

        // Fire Event
        SWPlayerInteractEvent swEvent = new CoreSWPlayerInteractEvent(p, location, blockType, action);
        skywars.getEventManager().callEvent(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBlockBreakEvent(BlockBreakEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());
        Block block = event.getBlock();
        Location loc = block.getLocation();
        SWCoord coord = new CoreSWCoord(skywars.getUtils().getSWWorld(loc.getWorld().getName()), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        String wName = block.getType().name();

        // Fire core event
        SWBlockBreakEvent swEvent = new CoreSWBlockBreakEvent(p, coord, wName);
        skywars.getEventManager().callEvent(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        SWPlayer p = this.getPlayerFromBukkitPlayer(e.getPlayer());

        SWAsyncPlayerChatEvent swEvent = new CoreSWAsyncPlayerChatEvent(p, e.getMessage());
        skywars.getEventManager().callEvent(swEvent);

        e.setMessage(swEvent.getMessage());
        if (swEvent.isCancelled()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBlockPlaceEvent(BlockPlaceEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());
        Block block = event.getBlock();
        Location loc = block.getLocation();
        SWCoord coord = new CoreSWCoord(skywars.getUtils().getSWWorld(loc.getWorld().getName()), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        String wName = event.getBlockPlaced().getType().name();

        // Fire core event
        SWBlockPlaceEvent swEvent = new CoreSWBlockPlaceEvent(p, coord, wName);
        skywars.getEventManager().callEvent(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent event) {
        // Get data
        int x = event.getChunk().getX();
        int z = event.getChunk().getZ();
        SWWorld world = this.skywars.getServer().getWorld(event.getWorld().getName());

        // Fire core event
        SWChunkLoadEvent swEvent = new CoreSWChunkLoadEvent(world, x, z, event.isNewChunk());
        skywars.getEventManager().callEvent(swEvent);
    }

    @EventHandler
    public void onWorldInitEvent(WorldInitEvent event) {
        // Get data
        SWWorld world = this.skywars.getServer().getWorld(event.getWorld().getName());

        // Fire core event
        SWWorldInitEvent swEvent = new CoreSWWorldInitEvent(world);
        skywars.getEventManager().callEvent(swEvent);
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        SWPlayer p = this.getPlayerFromBukkitPlayer(player);

        SWPlayerFoodLevelChangeEvent swEvent = new BukkitSWPlayerFoodLevelChangeEvent(
                event, p, event.getFoodLevel(),
                new BukkitItem(skywars, event.getItem())
        );
        skywars.getEventManager().callEvent(swEvent);

        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());
        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getWorld() == null || to == null || to.getWorld() == null) return;

        SWCoord coordFrom = new CoreSWCoord(skywars.getUtils().getSWWorld(from.getWorld().getName()),
                from.getBlockX(), from.getBlockY(), from.getBlockZ());
        SWCoord coordTo = new CoreSWCoord(skywars.getUtils().getSWWorld(to.getWorld().getName()),
                to.getBlockX(), to.getBlockY(), to.getBlockZ());

        // Fire core event
        SWPlayerMoveEvent swEvent = new CoreSWPlayerMoveEvent(p, coordFrom, coordTo);
        skywars.getEventManager().callEvent(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        // Get data
        SWEntity entity = skywars.getEntityManager().getEntityByUUID(event.getEntity().getUniqueId());
        DeathCause reason = DeathCause.fromString(event.getCause().name());
        double damage = event.getDamage();
        double finalDamage = event.getFinalDamage();

        // Fire core event
        SWEntityDamageEvent swEvent = new CoreSWEntityDamageEvent(entity, damage, finalDamage, reason);
        skywars.getEventManager().callEvent(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
        event.setDamage(swEvent.getDamage());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getEntity());

        List<Item> drops = new ArrayList<>();
        for (ItemStack stack : event.getDrops()) {
            drops.add(new BukkitItem(skywars, stack));
        }

        // Fire core event
        SWPlayerDeathEvent swEvent = new CoreSWPlayerDeathEvent(p, event.getDeathMessage(), event.getKeepInventory(), drops);
        skywars.getEventManager().callEvent(swEvent);

        // Update changes
        event.setDeathMessage(swEvent.getDeathMessage());
        event.setKeepInventory(swEvent.isKeepInventory());
        event.getDrops().clear();
        event.getDrops().addAll(swEvent.getDrops().stream().map(item -> ((BukkitItem) item).getBukkitItem()).collect(Collectors.toList()));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Get data
        SWEntity entity = skywars.getEntityManager().getEntityByUUID(event.getEntity().getUniqueId());
        SWEntity damager = skywars.getEntityManager().getEntityByUUID(event.getDamager().getUniqueId());
        DeathCause reason = DeathCause.fromString(event.getCause().name());
        double damage = event.getDamage();
        double finalDamage = event.getFinalDamage();

        // Fire core event
        SWEntityDamageByEntityEvent swEvent = new CoreSWEntityDamageByEntityEvent(entity, damager, damage, finalDamage, reason);
        skywars.getEventManager().callEvent(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
        event.setDamage(swEvent.getDamage());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        SWInventory inv = ((BukkitInventoryManager) this.skywars.getInventoryManager()).getSWInventory(event.getInventory());
        if (inv == null) inv = new BukkitSWInventory(skywars, event.getInventory(), "Inventory");

        SWGuiClickHandler.ClickType clickType;
        switch (event.getClick()) {
            case RIGHT:
            case SHIFT_RIGHT:
                clickType = SWGuiClickHandler.ClickType.SECONDARY;
                break;
            case MIDDLE:
                clickType = SWGuiClickHandler.ClickType.MIDDLE;
                break;
            default:
                clickType = SWGuiClickHandler.ClickType.PRIMARY;
                break;
        }

        CoreSWInventoryClickEvent swEvent = new CoreSWInventoryClickEvent(
                this.getPlayerFromBukkitPlayer((Player) event.getWhoClicked()),
                inv,
                clickType,
                event.getSlot(),
                event.getRawSlot(),
                event.isShiftClick(),
                new BukkitItem(skywars, event.getCurrentItem())
        );
        skywars.getEventManager().callEvent(swEvent);

        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
        event.setCurrentItem(((BukkitItem) swEvent.getCurrentItem()).getBukkitItem());
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        SWPlayer p = this.getPlayerFromBukkitPlayer((Player) e.getEntity());
        SWDroppedItem item = new BukkitSWDroppedItem(skywars, e.getItem());

        SWPlayerPickupItemEvent swEvent = new CoreSWPlayerPickupItemEvent(p, item, e.getRemaining());
        skywars.getEventManager().callEvent(swEvent);

        if (swEvent.isCancelled()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        SWPlayer p = this.getPlayerFromBukkitPlayer(e.getPlayer());
        SWDroppedItem item = new BukkitSWDroppedItem(skywars, e.getItemDrop());

        SWPlayerDropItemEvent swEvent = new CoreSWPlayerDropItemEvent(p, item);
        skywars.getEventManager().callEvent(swEvent);

        if (swEvent.isCancelled()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        SWPlayer p = this.getPlayerFromBukkitPlayer((Player) e.getPlayer());
        SWInventory inv = ((BukkitInventoryManager) this.skywars.getInventoryManager()).getSWInventory(e.getInventory());
        if (inv == null) inv = new BukkitSWInventory(skywars, e.getInventory(), "Inventory");

        SWInventoryCloseEvent swEvent = new CoreSWInventoryCloseEvent(p, inv);
        skywars.getEventManager().callEvent(swEvent);
    }

    // Utils

    private SWPlayer getPlayerFromBukkitPlayer(Player player) {
        return this.skywars.getPlayerManager().getPlayerByUUID(player.getUniqueId());
    }

}

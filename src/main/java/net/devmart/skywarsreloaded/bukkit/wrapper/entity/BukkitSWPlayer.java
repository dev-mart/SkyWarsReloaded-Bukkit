package net.devmart.skywarsreloaded.bukkit.wrapper.entity;

import net.devmart.skywarsreloaded.api.data.player.stats.SWPlayerData;
import net.devmart.skywarsreloaded.api.hook.SWVaultHook;
import net.devmart.skywarsreloaded.api.party.SWParty;
import net.devmart.skywarsreloaded.api.utils.SWCoord;
import net.devmart.skywarsreloaded.api.wrapper.Item;
import net.devmart.skywarsreloaded.api.wrapper.ParticleEffect;
import net.devmart.skywarsreloaded.api.wrapper.PotionEffect;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.api.wrapper.server.SWInventory;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.wrapper.BukkitItem;
import net.devmart.skywarsreloaded.bukkit.wrapper.BukkitParticleEffect;
import net.devmart.skywarsreloaded.bukkit.wrapper.BukkitPotionEffect;
import net.devmart.skywarsreloaded.bukkit.wrapper.server.BukkitSWInventory;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class BukkitSWPlayer extends BukkitSWEntity implements SWPlayer {

    @Nullable
    private Player player;
    private SWInventory inventory;

    private final AtomicBoolean online;
    private final AtomicBoolean frozen;

    private SWPlayerData playerData;
    private SWParty party;

    public BukkitSWPlayer(BukkitSkyWarsReloaded skywars, UUID uuid, boolean online) {
        super(skywars, uuid);
        this.online = new AtomicBoolean(online);
        this.frozen = new AtomicBoolean(false);
        this.fetchParentPlayer();
    }

    public BukkitSWPlayer(BukkitSkyWarsReloaded skywars, Player player, boolean online) {
        this(skywars, player.getUniqueId(), online);
    }

    @Nullable
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void sendMessage(String message) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return player.hasPermission(permission);
    }

    @Override
    public Item getItemInHand(boolean offHand) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        ItemStack item;
        if (this.skywars.getUtils().getServerVersion() >= 9) {
            if (offHand) item = player.getInventory().getItemInOffHand();
            else item = player.getInventory().getItemInMainHand();
        } else item = player.getInventory().getItemInHand();
        if (item == null || item.getType() == Material.AIR) return null;
        return new BukkitItem(skywars, item);
    }

    @Override
    public SWInventory getInventory() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");

        return this.inventory;
    }

    @Override
    public void setSlot(int slot, Item item) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().setItem(slot, item != null ? ((BukkitItem) item).getBukkitItem() : null);
    }

    @Override
    public Item getSlot(int slot) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return new BukkitItem(skywars, player.getInventory().getItem(slot));
    }

    @Override
    public Item getHelmet() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return new BukkitItem(skywars, player.getInventory().getHelmet());
    }

    @Override
    public void setHelmet(Item helmet) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().setHelmet(helmet != null ? ((BukkitItem) helmet).getBukkitItem() : null);
    }

    @Override
    public Item getChestplate() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return new BukkitItem(skywars, player.getInventory().getChestplate());
    }

    @Override
    public void setChestplate(Item chestplate) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().setChestplate(chestplate != null ? ((BukkitItem) chestplate).getBukkitItem() : null);
    }

    @Override
    public Item getLeggings() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return new BukkitItem(skywars, player.getInventory().getLeggings());
    }

    @Override
    public void setLeggings(Item leggings) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().setLeggings(leggings != null ? ((BukkitItem) leggings).getBukkitItem() : null);
    }

    @Override
    public Item getBoots() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return new BukkitItem(skywars, player.getInventory().getBoots());
    }

    @Override
    public void setBoots(Item boots) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().setBoots(boots != null ? ((BukkitItem) boots).getBukkitItem() : null);
    }

    @Override
    public void clearInventory() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().clear();
    }

    @Override
    public void setExp(int level, float exp) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setLevel(level);
        this.player.setExp(exp);
    }

    @Override
    public void playSound(SWCoord coord, String sound, float volume, float pitch) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.playSound(
                new Location(player.getWorld(), coord.xPrecise(), coord.yPrecise(), coord.zPrecise()),
                Sound.valueOf(sound.toUpperCase()),
                volume, pitch
        );
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        sendTitle(title, subtitle, 20, 50, 20);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        if (skywars.getUtils().getServerVersion() >= 11) player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        else player.sendTitle(title, subtitle);
    }

    @Override
    public String getName() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return player.getName();
    }

    @Override
    public void setGameMode(int gamemode) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        switch (gamemode) {
            case 0:
                player.setGameMode(GameMode.SURVIVAL);
                break;
            case 1:
                player.setGameMode(GameMode.CREATIVE);
                break;
            case 2:
                player.setGameMode(GameMode.ADVENTURE);
                break;
            case 3:
                player.setGameMode(GameMode.SPECTATOR);
                break;
        }
    }

    @Override
    public boolean isOnline() {
        return online.get();
    }

    @Override
    public void setOnline(boolean online) {
        this.online.set(online);
    }

    @Override
    public SWPlayerData getPlayerData() {
        return this.playerData;
    }

    @Override
    public void setPlayerData(SWPlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public @Nullable SWParty getParty() {
        return this.party;
    }

    @Override
    public void setParty(@Nullable SWParty party) {
        this.party = party;
    }

    @Override
    public void freeze() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.frozen.set(true);
        this.player.setAllowFlight(true);
        this.player.setFlying(true);
    }

    @Override
    public void unfreeze() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.frozen.set(false);
        this.player.setAllowFlight(false);
        this.player.setFlying(false);
    }

    @Override
    public boolean isFrozen() {
        return this.frozen.get();
    }

    @Override
    public void fetchParentPlayer() {
        if (skywars.getServer().isPrimaryThread()) {
            this.player = Bukkit.getPlayer(this.getUuid());
        }
        this.entity = this.player;

        if (this.player != null) this.inventory = new BukkitSWInventory(skywars, player.getInventory(), "Inventory");
    }

    @Override
    public int getFoodLevel() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return this.player.getFoodLevel();
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setFoodLevel(foodLevel);
    }

    @Override
    public void setSaturation(float saturation) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setSaturation(saturation);
    }

    @Override
    public float getSaturation() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return this.player.getSaturation();
    }

    @Override
    public void resetPlayerTime() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.resetPlayerTime();
    }

    @Override
    public void resetPlayerWeather() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.resetPlayerWeather();
    }

    @Override
    public boolean isAllowFlight() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return this.player.getAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean allowFlight) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setAllowFlight(allowFlight);
    }

    @Override
    public void setFlying(boolean flying) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setFlying(flying);
    }

    @Override
    public boolean isFlying() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return this.player.isFlying();
    }

    @Override
    public void clearBodyArrows() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setArrowsInBody(0);
    }

    @Override
    public String getType() {
        return "PLAYER";
    }

    @Override
    public void openInventory(SWInventory inventory) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.openInventory(((BukkitSWInventory) inventory).getBukkitInventory());
    }

    @Override
    public void closeInventory() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.closeInventory();
    }

    @Override
    public void clearArmor() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.getInventory().setArmorContents(new ItemStack[4]);
    }

    @Override
    public void removePotionEffect(String value) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        try {
            this.player.removePotionEffect(Objects.requireNonNull(PotionEffectType.getByName(value.toUpperCase())));
        } catch (Exception e) {
            skywars.getLogger().error("No potion effect type could be found with the name '" + value + "' when removing.");
        }
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return this.player.getActivePotionEffects().stream()
                .map(bukkitEffect -> new BukkitPotionEffect(skywars, bukkitEffect))
                .collect(Collectors.toList());
    }

    @Override
    public void spawnParticle(SWCoord location, ParticleEffect particleEffect) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");

        Particle bukkitParticle = ((BukkitParticleEffect) particleEffect).getBukkitParticle();
        if (bukkitParticle == null) {
            skywars.getLogger().error("Failed to apply particle effect to player because the effect is not valid.");
            return;
        }

        player.spawnParticle(
                bukkitParticle,
                location.xPrecise(), location.yPrecise(), location.zPrecise(),
                particleEffect.getCount(),
                particleEffect.getOffsetX(), particleEffect.getOffsetY(), particleEffect.getOffsetZ(),
                0,
                particleEffect.getFormattedData()
        );
    }

    @Override
    public void clearPotionEffects() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.getActivePotionEffects().forEach(potionEffect -> this.player.removePotionEffect(potionEffect.getType()));
    }

    @Override
    public boolean hasBalance(int money) {
        SWVaultHook vaultHook = skywars.getHookManager().getHook(SWVaultHook.class);
        return vaultHook != null && vaultHook.isEnabled() && vaultHook.hasBalance(this, money);
    }

    @Override
    public boolean depositBalance(int amount) {
        SWVaultHook vaultHook = skywars.getHookManager().getHook(SWVaultHook.class);
        return vaultHook != null && vaultHook.isEnabled() && vaultHook.deposit(this, amount);
    }

    @Override
    public boolean withdrawBalance(int amount) {
        SWVaultHook vaultHook = skywars.getHookManager().getHook(SWVaultHook.class);
        return vaultHook != null && vaultHook.isEnabled() && vaultHook.withdraw(this, amount);
    }
}

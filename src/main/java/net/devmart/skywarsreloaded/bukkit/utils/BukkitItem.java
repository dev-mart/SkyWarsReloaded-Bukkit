package net.devmart.skywarsreloaded.bukkit.utils;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.utils.Item;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloadedPlugin;
import net.devmart.skywarsreloaded.bukkit.wrapper.item.BukkitSWEnchantmentType;
import net.devmart.skywarsreloaded.core.utils.AbstractItem;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BukkitItem extends AbstractItem implements ConfigurationSerializable {

    protected ItemStack itemStack;
    protected String displayName;
    protected List<String> lore;
    protected Map<String, String> replacements;
    protected SWPlayer placeholderPlayer;

    public BukkitItem(SkyWarsReloaded skywars, String material) {
        super(skywars);
        this.replacements = new HashMap<>();
        this.placeholderPlayer = null;

        setItemStack(new ItemStack(validateMaterial(material)));
    }

    public BukkitItem(SkyWarsReloaded skywars, ItemStack itemStack) {
        super(skywars);
        this.replacements = new HashMap<>();
        this.placeholderPlayer = null;

        setItemStack(itemStack);
    }

    public BukkitItem setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;

        if (itemStack != null) {
            ItemMeta meta = itemStack.getItemMeta();

            if (meta != null) {
                this.lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
                this.displayName = meta.getDisplayName();
                return this;
            }
        }

        this.lore = new ArrayList<>();
        this.displayName = null;
        return this;
    }

    @Override
    public String getMaterial() {
        if (itemStack == null) return null;
        return this.itemStack.getType().toString();
    }

    @Override
    public BukkitItem setMaterial(String material) {
        try {
            this.itemStack.setType(Material.valueOf(material));
        } catch (Exception ex) {
            this.skywars.getLogger().warn("Attempted to use a material that doesn't exist! \"" + material + "\"");
            this.itemStack.setType(Material.STONE);
            ex.printStackTrace();
        }
        return this;
    }

    @Override
    public int getAmount() {
        if (itemStack == null) return 0;
        return this.itemStack.getAmount();
    }

    @Override
    public BukkitItem setAmount(int amount) {
        if (itemStack == null) return this;
        this.itemStack.setAmount(amount);
        return this;
    }

    @Override
    public List<String> getEnchantments() {
        if (this.itemStack == null) return new ArrayList<>();
        return this.itemStack.getEnchantments().entrySet().stream()
                .map((enchantType) -> enchantType.getKey().getKey().getKey() + ":" + enchantType.getValue().toString())
                .collect(Collectors.toList());
    }

    @Override
    public BukkitItem setEnchantments(List<String> enchantments) {
        if (itemStack == null) return this;
        ItemMeta meta = itemStack.getItemMeta();
        enchantments.forEach(enchantment -> {
            String[] split = enchantment.split(":");

            String type = split[0];
            int level = 1;
            if (split.length > 1 && skywars.getUtils().isInt(split[1])) {
                level = Integer.parseInt(split[1]);
            }

            try {
                Enchantment bukkitEnchantment = ((BukkitSWEnchantmentType) skywars.getNMSManager().getNMS().getEnchantment(type)).getEnchantment();
                assert meta != null;
                meta.addEnchant(bukkitEnchantment, level, true);
            } catch (Exception e) {
                skywars.getLogger().error("Enchantment with name '" + type + "' could not be resolved for item " + itemStack.getType().name() + ". Ignoring it.");
            }
        });
        itemStack.setItemMeta(meta);
        return this;
    }

    @Override
    public List<String> getLore() {
        return this.lore;
    }

    @Override
    public BukkitItem setLore(List<String> lore) {
        if (lore == null) lore = new ArrayList<>();
        this.lore = lore;
        return this;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public BukkitItem setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public List<String> getItemFlags() {
        if (itemStack == null) return new ArrayList<>();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return new ArrayList<>();

        return itemMeta.getItemFlags().stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public BukkitItem setItemFlags(List<String> itemFlags) {
        if (itemStack == null) return this;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return this;

        meta.removeItemFlags(meta.getItemFlags().toArray(ItemFlag[]::new));

        itemFlags.forEach(flag -> {
            try {
                meta.addItemFlags(ItemFlag.valueOf(flag));
            } catch (Exception e) {
                skywars.getLogger().error("Flag with name '" + flag + "' could not be resolved for item " + itemStack.getType().name() + ". Ignoring it.");
            }
        });
        itemStack.setItemMeta(meta);
        return this;
    }

    @Override
    public BukkitItem addItemFlag(String flag) {
        if (itemStack == null) return this;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return this;

        try {
            meta.addItemFlags(ItemFlag.valueOf(flag));
        } catch (Exception e) {
            skywars.getLogger().error("Flag with name '" + flag + "' could not be resolved for item " + itemStack.getType().name() + ". Ignoring it.");
        }
        itemStack.setItemMeta(meta);
        return this;
    }

    @Override
    public BukkitItem addAllItemFlags() {
        if (itemStack == null) return this;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return this;

        meta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(meta);
        return this;
    }

    @Override
    public short getDurability() {
        if (itemStack instanceof Damageable) {
            return (short) ((Damageable) itemStack).getDamage();
        }

        return 0;
    }

    @Override
    public BukkitItem setDurability(short durability) {
        if (itemStack instanceof Damageable) {
            ((Damageable) itemStack).setDamage(durability);
        }
        return this;
    }

    @Override
    public byte getDamage() {
        if (itemStack == null) return 0;
        return itemStack.getData().getData();
    }

    @Override
    public BukkitItem setDamage(byte damage) {
        if (itemStack == null) return this;
        itemStack.getData().setData(damage);
        return this;
    }

    @Override
    public java.awt.Color getColor() {
        if (itemStack == null) return null;

        Color color = null;
        if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
            color = ((LeatherArmorMeta) itemStack.getItemMeta()).getColor();
        } else if (itemStack.getItemMeta() instanceof PotionMeta) {
            color = ((PotionMeta) itemStack.getItemMeta()).getColor();
        }

        return color == null ? null : new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue());
    }

    @Override
    public BukkitItem setColor(java.awt.Color color) {
        if (itemStack == null) return this;
        final Color bukkitColor = Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());

        if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) itemStack.getItemMeta()).setColor(bukkitColor);
        } else if (itemStack.getItemMeta() instanceof PotionMeta) {
            ((PotionMeta) itemStack.getItemMeta()).setColor(bukkitColor);
        }
        return this;
    }

    @Override
    @Nullable
    public String getSkullOwner() {
        if (itemStack == null) return null;

        if (itemStack.getItemMeta() instanceof SkullMeta) {
            return ((SkullMeta) itemStack.getItemMeta()).getOwner();
        }
        return null;
    }

    @Override
    public BukkitItem setSkullOwner(String owner) {
        if (itemStack == null) return this;
        if (itemStack.getItemMeta() instanceof SkullMeta) {
            ((SkullMeta) itemStack.getItemMeta()).setOwner(owner);
        }
        return this;
    }

    private Material validateMaterial(String material) {
        try {
            return Material.valueOf(material);
        } catch (Exception e) {
            this.skywars.getLogger().error("Invalid material found for item: " + material);
            return Material.STONE;
        }
    }

    @Override
    public boolean isSimilar(Item item) {
        return getBukkitItem().isSimilar(((BukkitItem) item).getBukkitItem());
    }

    @Override
    public Item replace(String search, String replace) {
        this.replacements.put(search, replace);
        return this;
    }

    protected String parseMessage(String message) {
        message = skywars.getUtils().formatMessage(message, this.replacements, true);
        if (placeholderPlayer != null) {
            message = skywars.getUtils().parsePlaceholders(message, placeholderPlayer);
        }
        return message;
    }

    public ItemStack getBukkitItem() {
        if (this.itemStack == null) return null;
        ItemStack item = this.itemStack.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        if (this.displayName != null) {
            meta.setDisplayName(parseMessage(this.displayName));
        }

        List<String> parsedLore = this.lore.stream()
                .map(this::parseMessage)
                .collect(Collectors.toList());
        meta.setLore(parsedLore);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Item withExternalPlaceholders(SWPlayer player) {
        this.placeholderPlayer = player;
        return this;
    }

    @Override
    public Item clone() {
        return new BukkitItem(skywars, getBukkitItem())
                .withExternalPlaceholders(placeholderPlayer);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BukkitItem && ((BukkitItem) obj).itemStack.equals(itemStack);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return itemStack.serialize();
    }

    public static BukkitItem deserialize(Map<String, Object> map) {
        BukkitSkyWarsReloadedPlugin plugin = (BukkitSkyWarsReloadedPlugin) Bukkit.getPluginManager().getPlugin("SkyWarsReloaded");
        return new BukkitItem(plugin.getSkywars(), ItemStack.deserialize(map));
    }

}

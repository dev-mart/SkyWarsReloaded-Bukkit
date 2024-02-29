package net.devmart.skywarsreloaded.bukkit.protocol;

import net.devmart.skywarsreloaded.api.utils.SWCoord;
import net.devmart.skywarsreloaded.api.wrapper.Item;
import net.devmart.skywarsreloaded.api.wrapper.item.SWEnchantmentType;
import net.devmart.skywarsreloaded.api.wrapper.server.SWGameRule;
import net.devmart.skywarsreloaded.api.wrapper.world.SWWorld;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.wrapper.BukkitItem;
import net.devmart.skywarsreloaded.bukkit.wrapper.item.BukkitSWEnchantmentType;
import net.devmart.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BukkitNMS_13 extends BukkitNMS_12 {

    public BukkitNMS_13(BukkitSkyWarsReloaded skywars, String serverPackage) {
        super(skywars, serverPackage);
    }

    @Override
    public void initVersionedAPI() {
        // Versioned enums
        voidBiome = "THE_VOID";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setGameRule(SWWorld world, SWGameRule rule, Object value) {
        World bukkitWorld = Bukkit.getWorld(world.getName());
        if (bukkitWorld == null) return;
        if (rule.getValueType().equals(Integer.class)) {

            int intValue;
            if (value instanceof Integer) {
                intValue = (Integer) value;
            } else if (value instanceof String) {
                intValue = Integer.parseInt((String) value);
            } else {
                return;
            }

            GameRule<?> gameRule = GameRule.getByName(rule.getMinecraftId());
            if (gameRule == null) return;
            bukkitWorld.setGameRule((GameRule<Integer>) gameRule, intValue);
        } else if (rule.getValueType().equals(Boolean.class)) {

            boolean boolValue;
            if (value instanceof Boolean) {
                boolValue = (Boolean) value;
            } else if (value instanceof String) {
                boolValue = Boolean.parseBoolean((String) value);
            } else {
                return;
            }

            GameRule<?> gameRule = GameRule.getByName(rule.getMinecraftId());
            if (gameRule == null) return;
            bukkitWorld.setGameRule((GameRule<Boolean>) gameRule, boolValue);
        }
    }

    @Override
    public void setBlock(SWCoord loc, Item item) {
        if (loc == null || !(loc.getWorld() instanceof BukkitSWWorld) || !(item instanceof BukkitItem)) return;
        World world = ((BukkitSWWorld) loc.getWorld()).getBukkitWorld();
        ItemStack itemStack = ((BukkitItem) item).getBukkitItem();

        Block bukkitBlock = world.getBlockAt(loc.x(), loc.y(), loc.z());
        bukkitBlock.setType(itemStack.getType());
    }

    @Override
    public SWEnchantmentType getEnchantment(String name) {
        return new BukkitSWEnchantmentType(Enchantment.getByKey(NamespacedKey.minecraft(name)));
    }
}

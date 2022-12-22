package net.devmart.skywarsreloaded.bukkit.wrapper.item;

import net.devmart.skywarsreloaded.api.wrapper.item.SWEnchantmentType;
import org.bukkit.enchantments.Enchantment;

public class BukkitSWEnchantmentType implements SWEnchantmentType {

    private final Enchantment enchant;

    public BukkitSWEnchantmentType(Enchantment enchant) {
        this.enchant = enchant;
    }

    @Override
    public String getName() {
        return this.enchant.getName();
    }

    public Enchantment getEnchantment() {
        return this.enchant;
    }

}

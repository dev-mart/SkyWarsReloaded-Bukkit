package net.devmart.skywarsreloaded.bukkit.wrapper;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.core.wrapper.AbstractParticleEffect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class BukkitParticleEffect extends AbstractParticleEffect {

    protected Particle particle;
    protected Object formattedData;

    public BukkitParticleEffect(SkyWarsReloaded skywars, String input) {
        super(skywars, input);

        try {
            this.particle = Particle.valueOf(getType());
            this.formattedData = this.determineData();
        } catch (Exception e) {
            this.particle = null;
            skywars.getLogger().error("Failed to load Bukkit particle effect from string '%s' because the type '%s' is not a valid Bukkit particle.", input, getType());
        }
    }

    protected Object determineData() {
        int dataLength = this.getRawData().length;
        if (this.getRawData() == null || dataLength == 0) {
            return null;
        }

        if (getType().equalsIgnoreCase("REDSTONE") || getType().equalsIgnoreCase("DUST_COLOR_TRANSITION")) {
            int[] rgb = getRGB(this.getRawData()[0]);
            float size = 1.0f;
            if (dataLength >= 2) {
                if (skywars.getUtils().isFloat(this.getRawData()[dataLength - 1])) {
                    size = Float.parseFloat(this.getRawData()[dataLength - 1]);
                } else {
                    skywars.getLogger().error("Failed to parse size from particle effect data '%s' because it is not a float.", size);
                }
            }

            if (getType().equalsIgnoreCase("DUST_COLOR_TRANSITION")) {
                int[] rgbTo = rgb;
                if (dataLength >= 2) rgbTo = getRGB(this.getRawData()[1]);

                return new Particle.DustTransition(
                        org.bukkit.Color.fromRGB(rgb[0], rgb[1], rgb[2]),
                        org.bukkit.Color.fromRGB(rgbTo[0], rgbTo[1], rgbTo[2]),
                        size
                );
            }

            return new Particle.DustOptions(
                    org.bukkit.Color.fromRGB(rgb[0], rgb[1], rgb[2]),
                    size
            );
        } else if (getType().equalsIgnoreCase("ITEM_CRACK")) {
            String material = this.getRawData()[0].toUpperCase();

            Material bukkitMaterial;
            try {
                bukkitMaterial = org.bukkit.Material.valueOf(material);
            } catch (Exception e) {
                skywars.getLogger().error("Failed to parse material from particle effect data '%s' because it is not a valid Bukkit material.", material);
                bukkitMaterial = Material.STONE;
            }

            ItemStack item = new ItemStack(bukkitMaterial);
            if (dataLength >= 2) {
                if (skywars.getUtils().isInt(this.getRawData()[1])) {
                    try {
                        byte castedByte = Byte.parseByte(this.getRawData()[1]);

                        item = new ItemStack(bukkitMaterial, 1, castedByte);
                    } catch (Exception ignored) {
                    }
                }
            }

            return item;
        } else if (getType().equalsIgnoreCase("BLOCK_CRACK") || getType().equalsIgnoreCase("BLOCK_DUST") ||
                getType().equalsIgnoreCase("FALLING_DUST") || getType().equalsIgnoreCase("BLOCK_MARKER")) {
            String material = this.getRawData()[0].toUpperCase();

            Material bukkitMaterial;
            try {
                bukkitMaterial = org.bukkit.Material.valueOf(material);
            } catch (Exception e) {
                skywars.getLogger().error("Failed to parse material from particle effect data '%s' because it is not a valid Bukkit material.", material);
                bukkitMaterial = Material.STONE;
            }

            return bukkitMaterial.createBlockData();
        } else if (getType().equalsIgnoreCase("SHRIEK")) {
            try {
                return Integer.parseInt(this.getRawData()[0]);
            } catch (Exception e) {
                skywars.getLogger().error("Failed to parse argument from SHRIEK particle effect data '%s' because it is not an integer.", this.getRawData()[0]);
                return 0;
            }
        }

        try {
            return Float.parseFloat(this.getRawData()[0]);
        } catch (Exception ignored) {
            return 0f;
        }
    }

    protected int[] getRGB(String hexColor) {
        int startIndex = hexColor.startsWith("#") ? 1 : 0;

        try {
            int red = Integer.parseInt(hexColor.substring(startIndex, startIndex + 2), 16);
            int green = Integer.parseInt(hexColor.substring(startIndex + 2, startIndex + 4), 16);
            int blue = Integer.parseInt(hexColor.substring(startIndex + 4, startIndex + 6), 16);
            return new int[]{red, green, blue};
        } catch (Exception e) {
            skywars.getLogger().error("Failed to parse RGB values from hex color '%s'.", hexColor);
            return new int[]{255, 255, 255};
        }
    }

    public Particle getBukkitParticle() {
        return this.particle;
    }

    public Object getFormattedData() {
        return formattedData;
    }

    @Override
    public void setFormattedData(Object formattedData) {
        this.formattedData = formattedData;
    }

}

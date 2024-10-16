package net.devmart.skywarsreloaded.bukkit.data.config;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.data.config.YAMLConfig;
import net.devmart.skywarsreloaded.api.data.player.stats.PlayerStat;
import net.devmart.skywarsreloaded.api.unlockable.Unlockable;
import net.devmart.skywarsreloaded.api.utils.Message;
import net.devmart.skywarsreloaded.api.utils.SWCoord;
import net.devmart.skywarsreloaded.api.utils.SWLogger;
import net.devmart.skywarsreloaded.api.utils.properties.UnlockableProperties;
import net.devmart.skywarsreloaded.api.wrapper.Item;
import net.devmart.skywarsreloaded.bukkit.wrapper.BukkitItem;
import net.devmart.skywarsreloaded.core.data.config.AbstractYAMLConfig;
import net.devmart.skywarsreloaded.core.utils.CoreMessage;
import net.devmart.skywarsreloaded.core.utils.CoreSWCoord;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class BukkitYAMLConfig extends AbstractYAMLConfig {

    private final Object reloadLock = new Object();
    private FileConfiguration fileConfiguration;
    private ConfigurationSection configSection;
    private ConfigurationSection defaultConfigSection;
    private boolean hasParent = false;

    public BukkitYAMLConfig(SkyWarsReloaded skywars, String id, File directory, String fileName) {
        super(skywars, id, directory, fileName);

        this.onSetup();
    }

    public BukkitYAMLConfig(SkyWarsReloaded skywars, String id, @Nullable String directory, String fileName) {
        super(skywars, id, directory, fileName);

        this.onSetup();
    }

    public BukkitYAMLConfig(SkyWarsReloaded skywars, String id, File directory, String fileName, String defaultFile) {
        super(skywars, id, directory, fileName, defaultFile);

        this.onSetup();
    }

    public BukkitYAMLConfig(SkyWarsReloaded skyWars, String id, @Nullable String directory, String fileName, String defaultFile) {
        super(skyWars, id, directory, fileName, defaultFile);

        this.onSetup();
    }

    public BukkitYAMLConfig(BukkitYAMLConfig parent, ConfigurationSection section, ConfigurationSection defaultSection) {
        super(parent.skywars, parent.id, parent.directory, parent.fileName, parent.defaultFilePath);
        this.hasParent = true;
        this.fileConfiguration = parent.fileConfiguration;
        this.configSection = section;
        this.defaultConfigSection = defaultSection;
    }

    protected boolean onSetup() {
        synchronized (reloadLock) {
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.getFile());
            this.configSection = this.fileConfiguration.getConfigurationSection("");
            BufferedReader defaultFileReader = this.getDefaultFileReader();
            this.defaultConfigSection = YamlConfiguration.loadConfiguration(defaultFileReader);
            return true;
        }
    }

    public ConfigurationSection getConfigSection() {
        return configSection;
    }

    @Override
    public String getString(String property) {
        return getString(property, this.defaultConfigSection.getString(property, ""));
    }

    @Override
    public String getString(String property, String defaultValue) {
        return configSection.getString(property, defaultValue);
    }

    @Override
    public String getString(Enum<?> property) {
        return getString(property.toString());
    }

    @Override
    public String getString(Enum<?> property, String defaultValue) {
        return getString(property.toString(), defaultValue);
    }

    @Override
    public int getInt(String property) {
        return getInt(property, this.defaultConfigSection.getInt(property, 0));
    }

    @Override
    public int getInt(String property, int defaultValue) {
        return configSection.getInt(property, defaultValue);
    }

    @Override
    public int getInt(Enum<?> property) {
        return getInt(property.toString());
    }

    @Override
    public int getInt(Enum<?> property, int defaultValue) {
        return getInt(property.toString(), defaultValue);
    }

    @Override
    public double getDouble(String property) {
        return getDouble(property, this.defaultConfigSection.getDouble(property, 0));
    }

    @Override
    public double getDouble(String property, double defaultValue) {
        return configSection.getDouble(property, defaultValue);
    }

    @Override
    public double getDouble(Enum<?> property) {
        return getDouble(property.toString());
    }

    @Override
    public double getDouble(Enum<?> property, double defaultValue) {
        return getDouble(property.toString(), defaultValue);
    }

    @Override
    public List<String> getStringList(String property) {
        return configSection.getStringList(property);
    }

    @Override
    public List<String> getStringList(Enum<?> property) {
        return getStringList(property.toString());
    }

    @Override
    public List<?> getList(String property) {
        return getList(property, this.defaultConfigSection.getList(property, null));
    }

    @Override
    public List<?> getList(String property, List<?> defaultValue) {
        return configSection.getList(property, defaultValue);
    }

    @Override
    public List<?> getList(Enum<?> property) {
        return getList(property.toString());
    }

    @Override
    public List<?> getList(Enum<?> property, List<?> defaultValue) {
        return getList(property.toString(), defaultValue);
    }

    @Override
    public List<Map<?, ?>> getMapList(String property) {
        return configSection.getMapList(property);
    }

    @Override
    public List<Map<?, ?>> getMapList(Enum<?> property) {
        return getMapList(property.toString());
    }

    @Override
    public List<Item> getItemList(String property) {
        final List<?> list = getList(property, new ArrayList<>());
        List<Item> items = new ArrayList<>();

        list.forEach(object -> {
            if (object != null) {
                Item item = null;
                if (object instanceof Item) {
                    item = (Item) object;
                } else if (object instanceof Map) {
                    item = skywars.getItemManager().getItem((Map<String, Object>) object);
                }

                if (item != null) items.add(item);
                else skywars.getLogger().error("Invalid item found in list for property '" + property + "': " + object.getClass().getName());
            }
        });

        return items;
    }

    @Override
    public List<Item> getItemList(Enum<?> property) {
        return getItemList(property.toString());
    }

    @Override
    public Map<String, String> getStringMap(String property) {
        HashMap<String, String> convertedMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : getValues(property, false).entrySet()) {
            convertedMap.put(entry.getKey(), entry.getValue().toString());
        }
        return convertedMap;
    }

    @Override
    public Map<String, String> getStringMap(Enum<?> toString) {
        return getStringMap(toString.toString());
    }

    @Override
    public boolean getBoolean(String property) {
        return getBoolean(property, this.defaultConfigSection.getBoolean(property, false));
    }

    @Override
    public boolean getBoolean(String property, boolean defaultValue) {
        return configSection.getBoolean(property, defaultValue);
    }

    @Override
    public boolean getBoolean(Enum<?> property) {
        return getBoolean(property.toString());
    }

    @Override
    public boolean getBoolean(Enum<?> property, boolean defaultValue) {
        return getBoolean(property.toString(), defaultValue);
    }

    @Override
    public Object get(String property) {
        return get(property, this.defaultConfigSection.get(property, null));
    }

    @Override
    public Object get(String property, Object defaultValue) {
        return configSection.get(property, defaultValue);
    }

    @Override
    public Object get(Enum<?> property) {
        return get(property.toString());
    }

    @Override
    public Object get(Enum<?> property, Object defaultValue) {
        return get(property.toString(), defaultValue);
    }

    @Override
    public void set(String property, Object value) {
        if (value instanceof BukkitItem) {
            BukkitItem item = (BukkitItem) value;
            configSection.set(property + ".material", item.getMaterial());
            if (item.getAmount() != 1) configSection.set(property + ".amount", item.getAmount());
            if (item.getDamage() > 0) configSection.set(property + ".damage", item.getDamage());
            if (item.getDurability() > 0) configSection.set(property + ".durability", item.getDurability());
            if (item.getDisplayName() != null && !item.getDisplayName().isEmpty())
                configSection.set(property + ".display-name", item.getDisplayName());
            if (item.getLore() != null && !item.getLore().isEmpty()) configSection.set(property + ".lore", item.getLore());
            if (!item.getEnchantments().isEmpty())
                configSection.set(property + ".enchantments", item.getEnchantments());
            if (!item.getItemFlags().isEmpty()) configSection.set(property + ".item-flags", item.getItemFlags());
            if (item.getSkullOwner() != null) configSection.set(property + ".owner", item.getSkullOwner());
            if (item.getColor() != null)
                configSection.set(property + ".color", "#" + Integer.toHexString(item.getColor().getRGB()).substring(2));
        } else {
            configSection.set(property, value);
        }
    }

    @Override
    public void set(Enum<?> property, Object value) {
        set(property.toString(), value);
    }

    @Override
    public boolean isSet(String property) {
        return configSection.isSet(property);
    }

    @Override
    public boolean isSet(Enum<?> property) {
        return isSet(property.toString());
    }

    @Override
    public boolean contains(String property) {
        return configSection.contains(property) || defaultConfigSection.contains(property);
    }

    @Override
    public boolean contains(Enum<?> property) {
        return contains(property.toString());
    }

    @Override
    public Set<String> getKeys(String property) {
        final ConfigurationSection sect = configSection.getConfigurationSection(property);
        if (sect == null) return new HashSet<>();
        return sect.getKeys(false);
    }

    @Override
    public Set<String> getKeys(Enum<?> property) {
        return getKeys(property.toString());
    }

    @Override
    public Map<String, Object> getValues(String property, boolean deep) {
        ConfigurationSection section = defaultConfigSection.getConfigurationSection(property);
        return getValues(property, deep, section == null ? new HashMap<>() : section.getValues(true));
    }

    @Override
    public Map<String, Object> getValues(String property, boolean deep, Map<String, Object> defaultValue) {
        ConfigurationSection section = configSection.getConfigurationSection(property);
        if (section == null) return defaultValue;

        return section.getValues(true);
    }

    @Override
    public Item getItem(String category, Item def) {
        if (!contains(category)) return def;

        Map<String, Object> map = getValues(category, true);
        if (map.isEmpty()) return def;

        if (!map.containsKey("material")) map.put("material", def == null ? "STONE" : def.getMaterial());
        final Item item = skywars.getItemManager().getItem(map);

        return item == null ? def : item;
    }

    @Override
    public Item getItem(Enum<?> category) {
        return getItem(category.toString());
    }

    @Override
    public Item getItem(String category) {
        return getItem(category, null);
    }

    @Override
    public Item getItem(Enum<?> category, Item def) {
        return getItem(category.toString(), def);
    }

    @Override
    public SWCoord getCoord(String property, SWCoord def) {
        if (!contains(property)) {
            return null;
        }

        try {
            return new CoreSWCoord(skywars, configSection.getString(property));
        } catch (Exception e) {
            skywars.getLogger().error("Failed to load Coord '" + property + "'. " + e.getClass().getName() + ": " + e.getLocalizedMessage());
            return def;
        }
    }

    @Override
    public SWCoord getCoord(Enum<?> property, SWCoord def) {
        return getCoord(property.toString(), def);
    }

    @Override
    public SWCoord getCoord(String property) {
        return getCoord(property, null);
    }

    @Override
    public SWCoord getCoord(Enum<?> property) {
        return getCoord(property.toString());
    }

    @Override
    public Message getMessage(String property) {
        Object res = get(property, null);
        if (res == null) {
            res = this.defaultConfigSection.get(property, null);
        }

        // Auto report if still null, the default config should catch this
        if (res == null) {
            String msg = "Return value of " + property + " is null";
            SWLogger logger = this.skywars.getLogger();
            logger.error(msg);
            logger.reportException(new NullPointerException("Return value of " + property + " is null"));
            return new CoreMessage(skywars, "<error, please check console and report this>");
        }

        if (res instanceof String) {
            return new CoreMessage(skywars, (String) res);
        } else if (res instanceof List) {
            // Sanity check

            @SuppressWarnings("rawtypes")
            List list = (List) res;
            if (!list.isEmpty() && !(list.get(0) instanceof String)) {
                this.skywars.getLogger().error("Invalid configuration message list for '" + property +
                        "': the type of the first list item is a " + list.get(0).getClass().getTypeName() + ". " +
                        "Please make sure the message is surrounded with quotes.");
            }
            @SuppressWarnings("unchecked")
            List<String> stringList = (List<String>) list;
            return new CoreMessage(skywars, stringList.toArray(new String[0]));
        }
        return null;
    }

    @Override
    public Message getMessage(Enum<?> property) {
        return getMessage(property.toString());
    }

    @Override
    public Message getMessage(String property, String def) {
        if (!contains(property)) return new CoreMessage(skywars, def);
        return new CoreMessage(skywars, getString(property));
    }

    @Override
    public Message getMessage(Enum<?> property, String def) {
        return getMessage(property.toString(), def);
    }

    @Override
    public Message getMessage(String property, List<String> def) {
        if (!contains(property)) return new CoreMessage(skywars, def.toArray(new String[0]));
        return new CoreMessage(skywars, getStringList(property).toArray(new String[0]));
    }

    @Override
    public <E extends Enum<E>> E getEnum(Class<E> enumClass, String property) {
        return getEnum(enumClass, property, null);
    }

    @Override
    public <E extends Enum<E>> E getEnum(Class<E> enumClass, String property, String def) {
        if (enumClass == null || !enumClass.isEnum()) return null;

        String value = getString(property).toUpperCase();

        try {
            return Enum.valueOf(enumClass, value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void loadUnlockableData(Unlockable unlockable, String property) {
        if (!contains(property)) return;
        ConfigurationSection section = configSection.getConfigurationSection(property);
        if (section == null) return;

        unlockable.setDisplayName(section.getString(UnlockableProperties.DISPLAY_NAME, id));
        unlockable.setIcon(this.getItem(property + "." + UnlockableProperties.ICON));
        unlockable.setUnavailableIcon(this.getItem(property + "." + UnlockableProperties.UNAVAILABLE_ICON));
        unlockable.setDescription(section.getString(UnlockableProperties.DESCRIPTION, "SkyWarsReloaded Unlockable"));
        unlockable.setLore(section.getStringList(UnlockableProperties.LORE));

        unlockable.setNeedPermission(section.getBoolean(UnlockableProperties.REQUIREMENTS_PERMISSION, false));
        unlockable.setCost(section.getInt(UnlockableProperties.REQUIREMENTS_COST, 0));
        unlockable.setMenuPosition(section.getInt(UnlockableProperties.MENU_POSITION, 0));
        if (section.contains(UnlockableProperties.REQUIREMENTS_STATS)) {
            section.getConfigurationSection(UnlockableProperties.REQUIREMENTS_STATS).getKeys(false).forEach(stat -> {
                try {
                    PlayerStat playerStat = PlayerStat.fromString(stat);
                    if (playerStat == null) {
                        skywars.getLogger().error("Invalid stat '" + stat + "' in unlockable '" + unlockable.getId() + "'");
                        return;
                    }

                    unlockable.addMinimumStat(playerStat, section.getInt(UnlockableProperties.REQUIREMENTS_STATS + "." + stat, 0));
                } catch (Exception e) {
                    skywars.getLogger().error(String.format("Failed to load %s stat requirement for kit %s. Ignoring it. (%s)", stat, unlockable.getId(), e.getClass().getName() + ": " + e.getLocalizedMessage()));
                }
            });
        }
    }

    @Override
    public void loadUnlockableData(Unlockable unlockable, Enum<?> property) {
        loadUnlockableData(unlockable, property.toString());
    }

    @Override
    public YAMLConfig getSection(String path) {
        ConfigurationSection configurationSection = configSection.getConfigurationSection(path);
        ConfigurationSection defaultSection = defaultConfigSection.getConfigurationSection(path);

        if (configurationSection == null) {
            configurationSection = new YamlConfiguration().createSection(path);
        }
        if (defaultSection == null) {
            defaultSection = new YamlConfiguration().createSection(path);
        }

        return new BukkitYAMLConfig(this, configurationSection, defaultSection);
    }

    @Override
    public void save() {
        if (hasParent || fileConfiguration == null) {
            skywars.getLogger().error("Cannot save configuration file %s as it is likely a section of a parent configuration file.", getFileName());
            return;
        }

        try {

            fileConfiguration.save(getFile());
        } catch (IOException e) {
            skywars.getLogger().error("SkyWarsReloaded failed to save the YAML file called '" + getFileName() + "'.");
        }
    }
}

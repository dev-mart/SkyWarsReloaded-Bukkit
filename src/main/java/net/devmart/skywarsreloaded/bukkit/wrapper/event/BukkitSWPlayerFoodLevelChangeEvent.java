package net.devmart.skywarsreloaded.bukkit.wrapper.event;

import net.devmart.skywarsreloaded.api.wrapper.Item;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.core.wrapper.event.CoreSWPlayerFoodLevelChangeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class BukkitSWPlayerFoodLevelChangeEvent extends CoreSWPlayerFoodLevelChangeEvent {

    private final FoodLevelChangeEvent event;

    public BukkitSWPlayerFoodLevelChangeEvent(FoodLevelChangeEvent event, SWPlayer player, int foodLevel, Item item) {
        super(player, foodLevel, item);
        this.event = event;
    }

    public FoodLevelChangeEvent getEvent() {
        return event;
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        event.setFoodLevel(foodLevel);
    }
}

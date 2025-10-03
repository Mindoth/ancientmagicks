package net.mindoth.ancientmagicks.item.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;

public class SpellComponentItem extends Item {

    private final int cost;
    public int getCost() {
        return this.cost;
    }

    public SpellComponentItem(Properties pProperties, int cost) {
        super(pProperties);
        this.cost = cost;
    }

    private boolean isEncodedComponent(ItemStack stack) {
        return stack.getItem() instanceof SpellComponentItem component && component.isEncodeable() && stack.hasTag() && stack.getTag().contains(NBT_KEY_COMPONENT_DATA);
    }

    public static final String NBT_KEY_EMPTY = "am_empty";
    public static final String NBT_KEY_COMPONENT_DATA = "am_component_data_string";

    public boolean isEncodeable() {
        return false;
    }

    public void decodeTooltipData(List<Component> tooltip, String data, Item item) {
    }

    public String encodeComponentData(ItemStack stack) {
        if ( isEncodeable() && stack.hasTag() && stack.getTag().contains(NBT_KEY_COMPONENT_DATA) ) return stack.getTag().getString(NBT_KEY_COMPONENT_DATA);
        return NBT_KEY_EMPTY;
    }

    public static final String RED = "red";
    public static final String GREEN = "green";
    public static final String BLUE = "blue";
    public static final String POWER = "power";
    public static final String LIFE = "life";
    public static final String REACH = "reach";
    public static final String AOE = "aoe";

    //Projectile exclusive
    public static final String SPEED = "speed";
    public static final String ENTITY_PIERCE = "entity_pierce";
    public static final String BLOCK_PIERCE = "block_pierce";
    public static final String BLOCK_BOUNCE = "block_bounce";
    public static final String IS_HOMING = "is_homing";

    public static HashMap<String, Float> createDefaultStats() {
        HashMap<String, Float> stats = new HashMap<>();
        stats.put(RED, -1.0F);
        stats.put(GREEN, -1.0F);
        stats.put(BLUE, -1.0F);
        stats.put(POWER, 1.0F);
        stats.put(LIFE, 1.0F);
        stats.put(REACH, 4.5F);
        stats.put(AOE, 0.0F);
        return stats;
    }
}

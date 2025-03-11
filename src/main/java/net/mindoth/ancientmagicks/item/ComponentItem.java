package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;

public class ComponentItem extends Item {

    private final int manaCost;
    public int getManaCost() {
        return this.manaCost;
    }

    private final int cooldown;
    public int getCooldown() {
        return this.cooldown;
    }

    public ComponentItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties);
        this.manaCost = manaCost;
        this.cooldown = cooldown;
    }

    public static final String NBT_KEY_EMPTY = "am_empty";
    public static final String NBT_KEY_COMPONENT_DATA = "am_component_data_string";

    public boolean isEncodeable() {
        return false;
    }

    public void decodeTooltipData(List<Component> tooltip, String data, String key, Item item) {
    }

    public String encodeComponentData(ItemStack stack) {
        if ( isEncodeable() && stack.hasTag() && stack.getTag().contains(NBT_KEY_COMPONENT_DATA) ) return stack.getTag().getString(NBT_KEY_COMPONENT_DATA);
        return NBT_KEY_EMPTY;
    }

    public static final String POWER = "power";
    public static final String LIFE = "life";
    public static final String AOE = "aoe";
    public static final String REACH = "reach";

    //Projectile exclusive
    public static final String SPEED = "speed";
    public static final String ENTITY_PIERCE = "entity_pierce";
    public static final String BLOCK_PIERCE = "block_pierce";
    public static final String BLOCK_BOUNCE = "block_bounce";
    public static final String IS_HOMING = "is_homing";

    public static HashMap<String, Float> createDefaultStats() {
        HashMap<String, Float> stats = new HashMap<>();
        stats.merge(POWER, 1.0F, Float::sum);
        stats.merge(LIFE, 1.0F, Float::sum);
        stats.merge(AOE, 0.0F, Float::sum);
        stats.merge(REACH, 0.0F, Float::sum);
        return stats;
    }

    public static HashMap<String, Float> createSpellStats(List<SpellModifierItem> modifiers) {
        HashMap<String, Float> stats = createDefaultStats();
        for ( SpellModifierItem item : modifiers ) item.addStatsToMap(stats);
        return stats;
    }
}

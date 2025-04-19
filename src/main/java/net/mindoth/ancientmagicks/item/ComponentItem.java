package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks." + stack.getItem()).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
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

    public static HashMap<String, Float> createSpellStats(List<SpellModifierItem> modifiers) {
        HashMap<String, Float> stats = createDefaultStats();
        for ( SpellModifierItem item : modifiers ) item.addStatsToMap(stats);
        return stats;
    }
}

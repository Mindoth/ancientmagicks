package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.world.item.Item;

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

    public static final String POWER = "power";
    public static final String LIFE = "life";
    public static final String SPEED = "speed";
    public static final String AOE = "aoe";
    public static final String REACH = "reach";
    public static final String GRAVITY = "gravity";

    public static HashMap<String, Float> createDefaultStats() {
        HashMap<String, Float> stats = new HashMap<>();
        stats.merge(POWER, 1.0F, Float::sum);
        stats.merge(LIFE, 100.0F, Float::sum);
        stats.merge(SPEED, 1.0F, Float::sum);
        stats.merge(AOE, 0.0F, Float::sum);
        stats.merge(REACH, 4.0F, Float::sum);
        stats.merge(GRAVITY, 0.0F, Float::sum);
        return stats;
    }

    public static HashMap<String, Float> createSpellStats(List<SpellModifierItem> modifiers) {
        HashMap<String, Float> stats = createDefaultStats();
        for ( SpellModifierItem item : modifiers ) item.addStatsToMap(stats);
        return stats;
    }
}

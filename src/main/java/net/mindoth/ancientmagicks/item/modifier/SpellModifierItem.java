package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.ProjectileSpellEntity;
import net.minecraft.world.item.Item;

import java.util.HashMap;

public class SpellModifierItem extends Item {

    public SpellModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties);
    }

    public void addModifierToEntity(ProjectileSpellEntity projectile, int count) {
    }

    public void addStatsToMap(HashMap<String, Float> stats) {
    }
}

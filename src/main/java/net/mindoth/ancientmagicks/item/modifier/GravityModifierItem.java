package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class GravityModifierItem extends SpellModifierItem {

    public GravityModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierOnEntityCreation(AbstractSpellEntity projectile, int count) {
        if ( projectile.isNoGravity() ) projectile.setNoGravity(false);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(GRAVITY, 1.0F, Float::sum);
    }
}

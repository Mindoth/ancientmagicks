package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class ColorModifierItem extends SpellModifierItem {

    private final String color;
    public String getColor() {
        return this.color;
    }

    private final SpellEffectItem.ColorCode colorCode;

    public ColorModifierItem(Properties pProperties, String color, int manaCost, int cooldown, SpellEffectItem.ColorCode colorCode) {
        super(pProperties, manaCost, cooldown);
        this.color = color;
        this.colorCode = colorCode;
    }

    @Override
    public boolean usableWithForms() {
        return true;
    }

    @Override
    public boolean usableWithEffects() {
        return true;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.RED, colorCode.getParticleColor().r);
        projectile.getEntityData().set(AbstractSpellEntity.GREEN, colorCode.getParticleColor().g);
        projectile.getEntityData().set(AbstractSpellEntity.BLUE, colorCode.getParticleColor().b);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.replace(RED, (float)colorCode.getParticleColor().r);
        stats.replace(GREEN, (float)colorCode.getParticleColor().g);
        stats.replace(BLUE, (float)colorCode.getParticleColor().b);
    }
}

package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class SpellModifierItem extends ComponentItem {

    public SpellModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
    }

    public void addStatsToMap(HashMap<String, Float> stats) {
    }
}

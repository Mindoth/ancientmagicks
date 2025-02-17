package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.SpellProjectileEntity;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.HashMap;

public class SpellModifierItem extends Item {

    public SpellModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties);
    }

    public void addModifierToEntity(SpellProjectileEntity projectile, int count) {
    }

    public void addStatsToMap(HashMap<String, Float> stats) {
    }
}

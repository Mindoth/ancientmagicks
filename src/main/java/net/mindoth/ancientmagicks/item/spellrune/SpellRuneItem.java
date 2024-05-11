package net.mindoth.ancientmagicks.item.spellrune;

import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;

public class SpellRuneItem extends RuneItem {

    public SpellRuneItem(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    public static boolean isPushable(Entity entity) {
        return ( (entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof TNTEntity) && !(entity instanceof PlayerEntity) );
    }
}

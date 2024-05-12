package net.mindoth.ancientmagicks.item.spellrune;

import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;

public class SpellRuneItem extends RuneItem {

    public SpellRuneItem(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    public static boolean isAlly(LivingEntity owner, LivingEntity target) {
        if ( target instanceof PlayerEntity && !AncientMagicksCommonConfig.PVP.get() ) return true;
        else return target == owner || !target.canAttack(owner) || target.isAlliedTo(owner) || (target instanceof TameableEntity && ((TameableEntity)target).isOwnedBy(owner));
    }

    public static boolean isPushable(Entity entity) {
        return ( entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof TNTEntity || entity instanceof FallingBlockEntity );
    }
}

package net.mindoth.ancientmagicks.item.spellrune;

import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
<<<<<<< Updated upstream

import java.util.Properties;
=======
>>>>>>> Stashed changes

public class SpellRuneItem extends RuneItem {

    public SpellRuneItem(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

<<<<<<< Updated upstream
    public static boolean isPushable(Entity entity) {
        return ( (entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof PrimedTnt || entity instanceof FallingBlockEntity) && !(entity instanceof Player) );
    }

    public static boolean isAlly(LivingEntity owner, LivingEntity target) {
        return target == owner || target.isAlliedTo(owner) || (target instanceof TamableAnimal && ((TamableAnimal)target).isOwnedBy(owner));
=======
    public static boolean isAlly(LivingEntity owner, LivingEntity target) {
        if ( target instanceof Player && !AncientMagicksCommonConfig.PVP.get() ) return true;
        return target == owner || !target.canAttack(owner) || target.isAlliedTo(owner) || (target instanceof TamableAnimal && ((TamableAnimal)target).isOwnedBy(owner));
    }

    public static boolean isPushable(Entity entity) {
        return ( entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof PrimedTnt || entity instanceof FallingBlockEntity );
>>>>>>> Stashed changes
    }
}

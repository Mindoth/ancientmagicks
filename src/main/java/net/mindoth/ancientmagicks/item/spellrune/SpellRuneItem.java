package net.mindoth.ancientmagicks.item.spellrune;

import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;

import java.util.Properties;

public class SpellRuneItem extends RuneItem {

    public SpellRuneItem(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    public static boolean isPushable(Entity entity) {
        return ( (entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof PrimedTnt || entity instanceof FallingBlockEntity) && !(entity instanceof Player) );
    }

    public static boolean isAlly(LivingEntity owner, LivingEntity target) {
        return target == owner || target.isAlliedTo(owner) || (target instanceof TamableAnimal && ((TamableAnimal)target).isOwnedBy(owner));
    }
}

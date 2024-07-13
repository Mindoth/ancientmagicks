package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.effect.MindControlEffect;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class GlyphItem extends Item {
    public String tag;
    public GlyphItem(Properties pProperties, String tag) {
        super(pProperties);
        this.tag = tag;
    }

    public void onEntityHit(Level level, LivingEntity owner, EntityHitResult result) {
    }

    public void onBlockHit(Level level, LivingEntity owner, BlockHitResult result) {
    }

    public static boolean isAlly(LivingEntity owner, LivingEntity target) {
        if ( owner == null || target == null ) return false;
        if ( target instanceof Player && !AncientMagicksCommonConfig.PVP.get() ) return true;
        else return target == owner || !owner.canAttack(target) || owner.isAlliedTo(target) || (target instanceof TamableAnimal pet && pet.isOwnedBy(owner))
                || (target instanceof Mob mob && isMinionsOwner(owner, mob));
    }

    public static boolean isMinionsOwner(LivingEntity owner, Mob mob) {
        return mob.hasEffect(AncientMagicksEffects.MIND_CONTROL.get()) && mob.getPersistentData().hasUUID(MindControlEffect.NBT_KEY)
                && mob.getPersistentData().getUUID(MindControlEffect.NBT_KEY).equals(owner.getUUID()) && mob.getTarget() != owner;
    }
}

package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.effect.MindControlEffect;
import net.mindoth.ancientmagicks.entity.projectile.AbstractSpell;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class GlyphItem extends Item {
    public GlyphItem(Properties pProperties) {
        super(pProperties);
    }

    public void onEntityHit(@Nullable AbstractSpell spell, LivingEntity owner, EntityHitResult result) {
    }

    public void onBlockHit(@Nullable AbstractSpell spell, LivingEntity owner, BlockHitResult result) {
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

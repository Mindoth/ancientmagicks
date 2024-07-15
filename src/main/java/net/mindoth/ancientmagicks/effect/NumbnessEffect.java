package net.mindoth.ancientmagicks.effect;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class NumbnessEffect extends MobEffect {
    public NumbnessEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static final String NBT_KEY = "am_numbness_damage";

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void numbnessStoreDamage(final LivingDamageEvent event) {
        LivingEntity living = event.getEntity();
        DamageType type = event.getSource().type();
        final float damageAmount = event.getAmount();
        if ( living.hasEffect(AncientMagicksEffects.NUMBNESS.get()) && damageAmount > 0
                && type != living.damageSources().genericKill().type()
                && type != living.damageSources().fellOutOfWorld().type()
                && type != living.damageSources().outOfBorder().type() ) {
            if ( living instanceof Player player ) {
                CompoundTag playerData = player.getPersistentData();
                CompoundTag tag = playerData.getCompound(Player.PERSISTED_NBT_TAG);
                tag.putFloat(NBT_KEY, tag.getFloat(NBT_KEY) + damageAmount);
                playerData.put(Player.PERSISTED_NBT_TAG, tag);
                event.setAmount(0);
            }
            else if ( living instanceof Mob mob ) {
                CompoundTag tag = mob.getPersistentData();
                tag.putFloat(NBT_KEY, tag.getFloat(NBT_KEY) + damageAmount);
                event.setAmount(0);
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap map, int pAmplifier) {
        if ( living instanceof Player player ) {
            CompoundTag playerData = player.getPersistentData();
            CompoundTag tag = playerData.getCompound(Player.PERSISTED_NBT_TAG);
            final float damageAmount = tag.getFloat(NBT_KEY);
            if ( damageAmount > 0 ) player.hurt(living.damageSources().generic(), damageAmount);
            tag.putFloat(NBT_KEY, 0);
            playerData.put(Player.PERSISTED_NBT_TAG, tag);
        }
        else if ( living instanceof Mob mob ) {
            CompoundTag tag = mob.getPersistentData();
            final float damageAmount = tag.getFloat(NBT_KEY);
            if ( damageAmount > 0 ) mob.hurt(living.damageSources().generic(), damageAmount);
            tag.putFloat(NBT_KEY, 0);
        }
    }

    @SubscribeEvent
    public static void onNumbPlayerDeath(final LivingDeathEvent event) {
        if ( event.getEntity() instanceof Player player ) {
            CompoundTag playerData = player.getPersistentData();
            CompoundTag tag = playerData.getCompound(Player.PERSISTED_NBT_TAG);
            tag.putFloat(NBT_KEY, 0);
            playerData.put(Player.PERSISTED_NBT_TAG, tag);
        }
        else if ( event.getEntity() instanceof Mob mob ) {
            CompoundTag tag = mob.getPersistentData();
            tag.putFloat(NBT_KEY, 0);
        }
    }

    @SubscribeEvent
    public static void onNumbnessReapply(final MobEffectEvent.Applicable event) {
        if ( event.getEffectInstance().getEffect() == AncientMagicksEffects.NUMBNESS.get() ) {
            if ( event.getEntity().hasEffect(AncientMagicksEffects.NUMBNESS.get()) ) event.setResult(Event.Result.DENY);
            else event.setResult(Event.Result.DEFAULT);
        }
    }
}

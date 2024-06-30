package net.mindoth.ancientmagicks.item.spell.numbpain;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class NumbnessEffect extends MobEffect {

    public NumbnessEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    private static final String TAG_DEFENCE_COOLDOWN = ("am_numbness_damage");

    @SubscribeEvent
    public static void storeDamage(final LivingDamageEvent event) {
        final float damage = event.getAmount();
        if ( event.getEntity().hasEffect(AncientMagicksEffects.NUMBNESS.get()) && damage > 0 ) {
            if ( event.getEntity() instanceof Player player ) {
                CompoundTag playerData = player.getPersistentData();
                CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
                data.putFloat(TAG_DEFENCE_COOLDOWN, data.getFloat(TAG_DEFENCE_COOLDOWN) + damage);
                playerData.put(Player.PERSISTED_NBT_TAG, data);
                event.setAmount(0);
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        if ( pLivingEntity instanceof Player player ) {
            CompoundTag playerData = player.getPersistentData();
            CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
            final float damage = data.getFloat(TAG_DEFENCE_COOLDOWN);
            player.hurt(player.damageSources().playerAttack(player), damage);
            data.putFloat(TAG_DEFENCE_COOLDOWN, 0.0F);
            playerData.put(Player.PERSISTED_NBT_TAG, data);
        }
    }
}

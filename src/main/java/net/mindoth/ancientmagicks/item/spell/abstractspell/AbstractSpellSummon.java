package net.mindoth.ancientmagicks.item.spell.abstractspell;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlEffect;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class AbstractSpellSummon extends SpellItem {
    public AbstractSpellSummon(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    protected int getLife() {
        return 2400;
    }

    protected int getAmount() {
        return 1;
    }

    protected Mob getMinion(Level level) {
        return new Sheep(EntityType.SHEEP, level);
    }

    protected void playSound(Level level, Vec3 center) {
        playMagicSummonSound(level, center);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        state = true;

        if ( state ) {
            for ( int i = 0; i < getAmount(); i++ ) {
                Mob minion = getMinion(level);
                boolean isFlying = minion instanceof FlyingMob || minion instanceof FlyingAnimal;
                Vec3 pos = caster.position();
                Vec3 newPos = null;
                while ( newPos == null || !minion.position().equals(newPos) ) {
                    double d3 = pos.x + (minion.getRandom().nextDouble() - 0.5D) * 3.0D;
                    double d4 = Mth.clamp(pos.y + (double)(minion.getRandom().nextInt(4) - 2),
                            (double)owner.level().getMinBuildHeight(), (double)(owner.level().getMinBuildHeight() + ((ServerLevel)owner.level()).getLogicalHeight() - 1));
                    double d5 = pos.z + (minion.getRandom().nextDouble() - 0.5D) * 3.0D;
                    net.minecraftforge.event.entity.EntityTeleportEvent.ChorusFruit event = net.minecraftforge.event.ForgeEventFactory.onChorusFruitTeleport(owner, d3, d4, d5);
                    newPos = new Vec3(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                    minion.randomTeleport(newPos.x, newPos.y, newPos.z, true);
                }
                summonMinion(minion, owner, owner.level());
                playSound(level, minion.position());
            }
        }

        return state;
    }

    public void summonMinion(Mob minion, LivingEntity owner, Level level) {
        minion.getPersistentData().putUUID(MindControlEffect.NBT_KEY, owner.getUUID());
        minion.getPersistentData().putBoolean("am_is_minion", true);
        minion.addEffect(new MobEffectInstance(AncientMagicksEffects.MIND_CONTROL.get(), getLife()));
        ForgeEventFactory.onFinalizeSpawn(minion, (ServerLevel)level, level.getCurrentDifficultyAt(minion.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(minion);
        minion.spawnAnim();
    }
}
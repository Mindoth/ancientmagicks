package net.mindoth.ancientmagicks.item.spell.raisedead;

import net.mindoth.ancientmagicks.item.TabletItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RaiseDeadTablet extends TabletItem {

    public RaiseDeadTablet(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Mob minion = new SkeletonMinionEntity(level, owner);

        float life = 600.0F;
        float range = 14.0F;

        minion.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(4.0F * 1);
        minion.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(4.0F * 5);
        minion.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.25F);
        minion.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(6.0F * 2);
        minion.addEffect(new MobEffectInstance(AncientMagicksEffects.SKELETON_TIMER.get(), (int)life, 0, false, false, true));

        minion.moveTo(ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, true, true, true));
        minion.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(minion.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(minion);
        minion.spawnAnim();
        state = true;

        if ( state ) playMagicSummonSound(level, center);

        return state;
    }
}

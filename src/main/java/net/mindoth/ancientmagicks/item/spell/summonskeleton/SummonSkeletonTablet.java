package net.mindoth.ancientmagicks.item.spell.summonskeleton;

import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
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
import net.minecraftforge.event.ForgeEventFactory;

public class SummonSkeletonTablet extends TabletItem {

    public SummonSkeletonTablet(Properties pProperties, int tier, boolean isChannel, int cooldown) {
        super(pProperties, tier, isChannel, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Mob minion = new SkeletonMinionEntity(level, owner);

        float range = 14.0F;
        float life = 600.0F;

        minion.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(4.0F);
        minion.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(20.0F);
        minion.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.25F);
        minion.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(Math.max(range - 2.0F, 1.0F));
        minion.addEffect(new MobEffectInstance(AncientMagicksEffects.SKELETON_TIMER.get(), (int)life, 0, false, false, true));

        Vec3 pos;

        Vec3 freePoint = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, false, true, false, false);
        Vec3 blockPoint = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, false, true, true, false);
        if ( freePoint == blockPoint ) pos = freePoint;
        else pos = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, true, true, true, false);

        pos = new Vec3(pos.x, pos.y - 0.5D, pos.z);

        minion.moveTo(pos);
        ForgeEventFactory.onFinalizeSpawn(minion, (ServerLevel)level, level.getCurrentDifficultyAt(minion.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(minion);
        minion.spawnAnim();
        state = true;

        if ( state ) playMagicSummonSound(level, center);

        return state;
    }
}

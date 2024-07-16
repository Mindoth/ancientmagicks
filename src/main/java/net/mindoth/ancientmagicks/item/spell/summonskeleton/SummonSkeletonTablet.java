package net.mindoth.ancientmagicks.item.spell.summonskeleton;

import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class SummonSkeletonTablet extends SpellTabletItem {

    public SummonSkeletonTablet(Properties pProperties, boolean isChannel, int cooldown) {
        super(pProperties, isChannel, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Mob minion = new Skeleton(EntityType.SKELETON, level);

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
        state = true;

        if ( state ) playMagicSummonSound(level, center);

        return state;
    }
}

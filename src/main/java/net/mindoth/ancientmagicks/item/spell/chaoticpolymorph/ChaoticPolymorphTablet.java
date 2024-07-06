package net.mindoth.ancientmagicks.item.spell.chaoticpolymorph;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ChaoticPolymorphTablet extends SpellTabletItem {

    public ChaoticPolymorphTablet(Properties pProperties, int tier, boolean isChannel, int cooldown) {
        super(pProperties, tier, isChannel, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        float range = 14.0F;
        float size = range * 0.5F;

        LivingEntity target;
        if ( caster == owner ) target = (LivingEntity)ShadowEvents.getPointedEntity(level, caster, range, 0.25F, caster == owner, true);
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);

        //TODO Check if game is in peaceful
        if ( level instanceof ServerLevel serverLevel && target instanceof Mob ) {
            if ( AncientMagicks.MOB_LIST.isEmpty() ) AncientMagicks.createMobList(serverLevel);
            int index = ThreadLocalRandom.current().nextInt(0, AncientMagicks.MOB_LIST.size());
            Entity entity = AncientMagicks.MOB_LIST.get(index).create(level);
            if ( entity instanceof Mob sheep ) {
                sheep.moveTo(target.position());
                ForgeEventFactory.onFinalizeSpawn(sheep, serverLevel, serverLevel.getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
                serverLevel.tryAddFreshEntityWithPassengers(sheep);
                if ( sheep.isAddedToWorld() ) {
                    sheep.spawnAnim();
                    target.discard();
                    ShadowEvents.summonParticleLine(ParticleTypes.ENTITY_EFFECT, caster, ShadowEvents.getEntityCenter(caster), ShadowEvents.getEntityCenter(sheep),
                            0, 1, 85F / 255F, 1, 1);
                    state = true;
                }
            }
        }

        if ( state ) playMagicSound(level, center);

        return state;
    }
}

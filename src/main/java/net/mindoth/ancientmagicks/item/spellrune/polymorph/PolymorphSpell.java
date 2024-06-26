package net.mindoth.ancientmagicks.item.spellrune.polymorph;

import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PolymorphSpell extends SpellRuneItem {

    public PolymorphSpell(Properties pProperties, int tier) {
        super(pProperties, tier);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Vec3 casterPos = caster.getEyePosition(1.0F);
        playMagicSound(level, casterPos);

        float range = 14.0F;
        float size = 1.0F;

        LivingEntity target;
        if ( caster == owner ) target = (LivingEntity) ShadowEvents.getPointedEntity(level, caster, range, 0.25F, caster == owner, true);
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);

        if ( level instanceof ServerLevel serverLevel ) {
            if ( target instanceof Mob && !(target instanceof Sheep) ) {
                Sheep sheep = ((Mob)target).convertTo(EntityType.SHEEP, false);
                sheep.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
                state = true;

                /*Mob sheep = new Sheep(EntityType.SHEEP, serverLevel);
                ForgeEventFactory.onFinalizeSpawn(sheep, serverLevel, serverLevel.getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
                serverLevel.tryAddFreshEntityWithPassengers(sheep);
                if ( sheep.isAddedToWorld() ) {
                    sheep.moveTo(target.position());
                    sheep.spawnAnim();
                    target.discard();
                }*/
            }
        }
        return state;
    }
}

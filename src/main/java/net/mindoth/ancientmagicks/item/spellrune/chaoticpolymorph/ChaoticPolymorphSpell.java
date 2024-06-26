package net.mindoth.ancientmagicks.item.spellrune.chaoticpolymorph;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
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

public class ChaoticPolymorphSpell extends SpellRuneItem {

    public ChaoticPolymorphSpell(Properties pProperties, int tier) {
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

        if ( level instanceof ServerLevel serverLevel && target instanceof Mob ) {
            if ( AncientMagicks.MOB_LIST.isEmpty() ) AncientMagicks.createMobList(serverLevel);
            Random rand = new Random();
            Mob sheep = AncientMagicks.MOB_LIST.get(rand.nextInt(AncientMagicks.MOB_LIST.size()));
            if ( sheep != null ) {
                sheep.moveTo(target.position());
                ForgeEventFactory.onFinalizeSpawn(sheep, serverLevel, serverLevel.getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
                serverLevel.tryAddFreshEntityWithPassengers(sheep);
                if ( sheep.isAddedToWorld() ) {
                    sheep.spawnAnim();
                    target.discard();
                    state = true;
                }
            }
        }
        return state;
    }
}

package net.mindoth.ancientmagicks.item.spell.summonzombie;

import net.mindoth.ancientmagicks.item.SpellTabletItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonZombieTablet extends SpellTabletItem {

    public SummonZombieTablet(Properties pProperties, boolean isChannel, int cooldown) {
        super(pProperties, isChannel, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Mob minion = new Zombie(EntityType.ZOMBIE, level);

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

package net.mindoth.ancientmagicks.item.spellrune.lightning;

import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CallLightningSpell extends SpellRuneItem {

    public CallLightningSpell(Item.Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        Level level = caster.level();
        Vec3 point = ShadowEvents.getPoint(level, caster, 64.0F, 0, caster == owner, true, true, true);
        if ( level.canSeeSky(new BlockPos((int)point.x, (int)point.y, (int)point.z)) ) {
            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
            if ( lightningbolt != null ) {
                lightningbolt.moveTo(new Vec3(point.x, point.y - 1, point.z));
                lightningbolt.setCause(caster instanceof ServerPlayer ? (ServerPlayer)caster : null);
                level.addFreshEntity(lightningbolt);
                playLightningSummonSound(level, center);
            }
        }
        else playWhiffSound(level, center);
    }
}

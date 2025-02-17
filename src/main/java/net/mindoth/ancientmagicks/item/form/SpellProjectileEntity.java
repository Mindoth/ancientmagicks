package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSendCustomParticles;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

import java.util.List;
import java.util.Random;

public class SpellProjectileEntity extends AbstractSpellEntity {

    public SpellProjectileEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.SPELL_PROJECTILE.get(), level);
    }

    public SpellProjectileEntity(EntityType<SpellProjectileEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SpellProjectileEntity(Level level, LivingEntity owner, Entity caster, SpellItem spell) {
        super(AncientMagicksEntities.SPELL_PROJECTILE.get(), level, owner, caster, spell);
    }

    protected void doSpellOnTarget(Entity target) {
        if ( getSpell() != null ) {
            getSpell().castSpell(level(), this.owner, this.caster, target, getStats());
        }
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        Entity target = result.getEntity();
        if ( getAoe() > 0.0F ) doAoeTarget();
        else doSpellOnTarget(target);
    }

    @Override
    protected void doBlockEffects(BlockHitResult result) {
        if ( getAoe() > 0.0F ) doAoeTarget();
    }

    private void doAoeTarget() {
        List<Entity> list = ShadowEvents.getEntitiesAround(this, getAoe(), getAoe(), getAoe());
        for ( Entity entity : list ) doSpellOnTarget(entity);
        addEnchantParticles(this, this.entityData.get(RED), this.entityData.get(GREEN), this.entityData.get(BLUE), 0.15F, 8);
    }



    protected void addEnchantParticles(Entity target, int r, int g, int b, float size, int age) {
        double var = getAoe();
        double maxX = target.getBoundingBox().maxX + var;
        double minX = target.getBoundingBox().minX - var;
        double maxZ = target.getBoundingBox().maxZ + var;
        double minZ = target.getBoundingBox().minZ - var;
        double vecX = 0;
        double vecY = 0.25D;
        double vecZ = 0;
        int amount = 4 * (int)var;
        for ( int i = 0; i < amount; i++ ) {
            double randX = maxX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < amount; i++ ) {
            double randX = minX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < amount; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < amount; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = maxZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
    }
}

package net.mindoth.ancientmagicks.item.effect;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.config.ModCommonConfig;
import net.mindoth.ancientmagicks.item.SpellComponentItem;
import net.mindoth.ancientmagicks.mobeffect.MindControlEffect;
import net.mindoth.ancientmagicks.network.ModNetwork;
import net.mindoth.ancientmagicks.network.PacketSendCustomParticles;
import net.mindoth.ancientmagicks.registries.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SpellEffectItem extends SpellComponentItem {

    public SpellEffectItem(Properties pProperties, int cost) {
        super(pProperties, cost);
        this.cost = cost;
    }

    private final int cost;
    public int getCost() {
        return this.cost;
    }

    public boolean isHarmful(@Nullable String data) {
        return true;
    }

    public ParticleColor.IntWrapper getParticleColor(HashMap<String, Float> stats) {
        ParticleColor.IntWrapper color = new ParticleColor.IntWrapper(Mth.floor(stats.get(SpellComponentItem.RED)), Mth.floor(stats.get(SpellComponentItem.GREEN)), Mth.floor(stats.get(SpellComponentItem.BLUE)));
        if ( color.r < 0 || color.r > 255 || color.g < 0 || color.g > 255 || color.b < 0 || color.b > 255 ) {
            int r = new Random().nextInt(0, 256);
            int g = new Random().nextInt(0, 256);
            int b = new Random().nextInt(0, 256);
            return new ParticleColor.IntWrapper(r, g, b);
        }
        else return color;
    }

    public enum ColorCode {
        DARK_RED(new ParticleColor.IntWrapper(170, 25, 25)),
        RED(new ParticleColor.IntWrapper(255, 85, 85)),
        GOLD(new ParticleColor.IntWrapper(255, 170, 25)),
        YELLOW(new ParticleColor.IntWrapper(255, 255, 85)),
        DARK_GREEN(new ParticleColor.IntWrapper(25, 170, 25)),
        GREEN(new ParticleColor.IntWrapper(85, 225, 85)),
        AQUA(new ParticleColor.IntWrapper(85, 255, 255)),
        DARK_AQUA(new ParticleColor.IntWrapper(25, 170, 170)),
        DARK_BLUE(new ParticleColor.IntWrapper(25, 25, 170)),
        BLUE(new ParticleColor.IntWrapper(85, 85, 255)),
        LIGHT_PURPLE(new ParticleColor.IntWrapper(255, 85, 255)),
        DARK_PURPLE(new ParticleColor.IntWrapper(170, 25, 170)),
        WHITE(new ParticleColor.IntWrapper(255, 255, 255)),
        GRAY(new ParticleColor.IntWrapper(170, 170, 170)),
        DARK_GRAY(new ParticleColor.IntWrapper(85, 85, 85)),
        BLACK(new ParticleColor.IntWrapper(0, 0, 0));

        private ParticleColor.IntWrapper color;
        ColorCode(ParticleColor.IntWrapper color) {
            this.color = color;
        }
        public ParticleColor.IntWrapper getParticleColor() {
            return this.color;
        }
    }

    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats, String data) {
        return canApply(level, owner, caster, result, data);
    }

    protected boolean canApply(Level level, LivingEntity owner, Entity caster, HitResult result, String data) {
        return true;
    }

    private static BlockPos getPosOfFace(BlockPos blockPos, Direction face) {
        return switch (face) {
            case UP -> blockPos.above();
            case EAST -> blockPos.east();
            case WEST -> blockPos.west();
            case SOUTH -> blockPos.south();
            case NORTH -> blockPos.north();
            case DOWN -> blockPos.below();
        };
    }

    private static @NotNull List<BlockPos> getBlockList(SpellEffectItem component, HitResult result, BlockPos pos, int range) {
        List<BlockPos> blocks = Lists.newArrayList();
        if ( result instanceof EntityHitResult ) {
            for ( int xPos = pos.getX() - range; xPos <= pos.getX() + range; xPos++ ) {
                for ( int zPos = pos.getZ() - range; zPos <= pos.getZ() + range; zPos++ ) {
                    blocks.add(new BlockPos(xPos, pos.getY(), zPos));
                }
            }
        }
        else if ( result instanceof BlockHitResult bResult ) {
            if ( component instanceof BlockTargetEffect bte && !bte.isInside() ) pos = getPosOfFace(pos, bResult.getDirection());
            if ( bResult.getDirection() == Direction.UP || bResult.getDirection() == Direction.DOWN ) {
                for ( int xPos = pos.getX() - range; xPos <= pos.getX() + range; xPos++ ) {
                    for ( int zPos = pos.getZ() - range; zPos <= pos.getZ() + range; zPos++ ) {
                        blocks.add(new BlockPos(xPos, pos.getY(), zPos));
                    }
                }
            }
            else if ( bResult.getDirection() == Direction.NORTH || bResult.getDirection() == Direction.SOUTH ) {
                for ( int xPos = pos.getX() - range; xPos <= pos.getX() + range; xPos++ ) {
                    for ( int yPos = pos.getY() - range; yPos <= pos.getY() + range; yPos++ ) {
                        blocks.add(new BlockPos(xPos, yPos, pos.getZ()));
                    }
                }
            }
            else if ( bResult.getDirection() == Direction.EAST || bResult.getDirection() == Direction.WEST ) {
                for ( int yPos = pos.getY() - range; yPos <= pos.getY() + range; yPos++ ) {
                    for ( int zPos = pos.getZ() - range; zPos <= pos.getZ() + range; zPos++ ) {
                        blocks.add(new BlockPos(pos.getX(), yPos, zPos));
                    }
                }
            }
        }
        if ( !blocks.contains(pos) ) blocks.add(pos);
        return blocks;
    }

    //TODO: aoe particles
    public boolean castSpell(Level level, LivingEntity owner, Entity caster, HitResult result, float aoe, HashMap<String, Float> stats, String data) {
        if ( owner == null || caster == null ) return false;
        boolean state = false;
        Vec3 center = result.getLocation();
        if ( aoe > 0.0F ) {
            Vec3 start = new Vec3(center.x + aoe, center.y + aoe, center.z + aoe);
            Vec3 end = new Vec3(center.x - aoe, center.y - aoe, center.z - aoe);
            AABB box = new AABB(start, end);
            if ( this instanceof EntityTargetEffect ) {
                List<Entity> entities = level.getEntitiesOfClass(Entity.class, box);
                for ( Entity entity : entities ) {
                    EntityHitResult newResult = new EntityHitResult(entity);
                    if ( canApply(level, owner, caster, newResult, data) ) doSpell(level, owner, caster, newResult, stats, data);
                }
                state = true;
                aoeEntitySpellParticles(level, box, result, aoe, stats);
            }
            else if ( this instanceof BlockTargetEffect ) {
                List<BlockPos> blocks;
                boolean isInside = false;
                if ( result instanceof EntityHitResult entityHitResult ) blocks = getBlockList(this, result, entityHitResult.getEntity().getOnPos().above(), (int)aoe);
                else {
                    BlockHitResult blockHitResult = (BlockHitResult)result;
                    blocks = getBlockList(this, result, blockHitResult.getBlockPos(), (int)aoe);
                    isInside = blockHitResult.isInside();
                }
                for ( BlockPos position : blocks ) {
                    BlockHitResult newResult = new BlockHitResult(position.getCenter(), Direction.UP, position, isInside);
                    if ( canApply(level, owner, caster, newResult, data) ) doSpell(level, owner, caster, newResult, stats, data);
                }
                state = true;
                aoeBlockSpellParticles(level, blocks, stats);
            }
            else if ( canApply(level, owner, caster, result, data) ) state = doSpell(level, owner, caster, result, stats, data);
        }
        else if ( canApply(level, owner, caster, result, data) ) state = doSpell(level, owner, caster, result, stats, data);
        return state;
    }

    private void aoeEntitySpellParticles(Level level, AABB box, HitResult result, float range, HashMap<String, Float> stats) {
        Vec3 center = box.getCenter();
        BlockPos pos = new BlockPos(Mth.floor(center.x), Mth.floor(center.y), Mth.floor(center.z));
        if ( result instanceof BlockHitResult blockHitResult ) pos = getPosOfFace(blockHitResult.getBlockPos(), blockHitResult.getDirection());
        double tempY = center.y;
        for ( int i = pos.getY(); i >= Mth.floor(center.y - range); i-- ) {
            BlockPos tempPos = new BlockPos(pos.getX(), i, pos.getZ());
            if ( level.getBlockState(tempPos).isSolid() ) break;
            else tempY = i;
        }
        box = box.move(0, -(center.y - tempY), 0);
        addAoeParticles(false, level, box, 0.15F, 8, stats);
    }

    private void aoeBlockSpellParticles(Level level, List<BlockPos> blocks, HashMap<String, Float> stats) {
        BlockPos start = new BlockPos(blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ());
        BlockPos end = new BlockPos(blocks.get(blocks.size() - 1).getX() + 1, blocks.get(blocks.size() - 1).getY() + 1, blocks.get(blocks.size() - 1).getZ() + 1);
        AABB particleBox = new AABB(start, end);
        addAoeParticles(true, level, particleBox, 0.15F, 8, stats);
    }

    protected void addAoeParticles(boolean targetBlocks, Level level, AABB box, float size, int age, HashMap<String, Float> stats) {
        Vec3 center = box.getCenter();
        double maxX = box.maxX;
        double minX = box.minX;
        double maxY = box.maxY;
        double minY = box.minY;
        double maxZ = box.maxZ;
        double minZ = box.minZ;

        if ( targetBlocks ) {
            int amountX = 4 * (int)box.getXsize();
            int amountY = 4 * (int)box.getYsize();
            int amountZ = 4 * (int)box.getZsize();

            //VectorPos for each corner
            Vec3 pos0 = new Vec3(minX, minY, minZ);
            Vec3 pos1 = new Vec3(maxX, minY, minZ);
            Vec3 pos2 = new Vec3(minX, minY, maxZ);
            Vec3 pos3 = new Vec3(maxX, minY, maxZ);
            Vec3 pos4 = new Vec3(minX, maxY, minZ);
            Vec3 pos5 = new Vec3(maxX, maxY, minZ);
            Vec3 pos6 = new Vec3(minX, maxY, maxZ);
            Vec3 pos7 = new Vec3(maxX, maxY, maxZ);
            //Bottom corners
            generateParticles(pos0, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos1, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos2, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos3, center, level, size, age, 0, 0, 0, stats);
            //Top corners
            generateParticles(pos4, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos5, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos6, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos7, center, level, size, age, 0, 0, 0, stats);
            //Bottom edges
            summonParticleLine(pos0, pos1, amountX, center, level, size, age, stats);
            summonParticleLine(pos0, pos2, amountZ, center, level, size, age, stats);
            summonParticleLine(pos3, pos1, amountZ, center, level, size, age, stats);
            summonParticleLine(pos3, pos2, amountX, center, level, size, age, stats);
            //Middle edges
            summonParticleLine(pos0, pos4, amountY, center, level, size, age, stats);
            summonParticleLine(pos1, pos5, amountY, center, level, size, age, stats);
            summonParticleLine(pos2, pos6, amountY, center, level, size, age, stats);
            summonParticleLine(pos3, pos7, amountY, center, level, size, age, stats);
            //Top edges
            summonParticleLine(pos4, pos5, amountX, center, level, size, age, stats);
            summonParticleLine(pos4, pos6, amountZ, center, level, size, age, stats);
            summonParticleLine(pos7, pos5, amountZ, center, level, size, age, stats);
            summonParticleLine(pos7, pos6, amountX, center, level, size, age, stats);
        }
        else {
            int amount = 4 * Math.max((int)box.getYsize(), (int)box.getXsize());
            for ( int i = 0; i < amount; i++ ) {
                generateParticles(new Vec3(maxX, center.y - 0.5D + new Random().nextDouble(), minZ + (maxZ - minZ) * new Random().nextDouble()), center, level, size, age, 0, 0.15D, 0, stats);
                generateParticles(new Vec3(minX, center.y - 0.5D + new Random().nextDouble(), minZ + (maxZ - minZ) * new Random().nextDouble()), center, level, size, age, 0, 0.15D, 0, stats);
                generateParticles(new Vec3(minX + (maxX - minX) * new Random().nextDouble(), center.y - 0.5D + new Random().nextDouble(), minZ), center, level, size, age, 0, 0.15D, 0, stats);
                generateParticles(new Vec3(minX + (maxX - minX) * new Random().nextDouble(), center.y - 0.5D + new Random().nextDouble(), maxZ), center, level, size, age, 0, 0.15D, 0, stats);
            }
        }
    }

    private void summonParticleLine(Vec3 startPos, Vec3 endPos, int amount, Vec3 center, Level level, float size, int age, HashMap<String, Float> stats) {
        double startX = startPos.x;
        double startY = startPos.y;
        double startZ = startPos.z;
        double endX = endPos.x;
        double endY = endPos.y;
        double endZ = endPos.z;
        for (int k = 1; k < (1 + amount); k++ ) {
            double vecX = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
            double vecY = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
            double vecZ = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
            double lineX = startX * (1 - ((double) k / amount)) + endX * ((double) k / amount);
            double lineY = startY * (1 - ((double) k / amount)) + endY * ((double) k / amount);
            double lineZ = startZ * (1 - ((double) k / amount)) + endZ * ((double) k / amount);
            generateParticles(new Vec3(lineX, lineY, lineZ), center, level, size, age, vecX * 0.1D, vecY * 0.1D, vecZ * 0.1D, stats);
        }
    }

    private void generateParticles(Vec3 pos, Vec3 center, Level level, float size, int age, double vecX, double vecY, double vecZ, HashMap<String, Float> stats) {
        ParticleColor.IntWrapper color = getParticleColor(stats);
        ModNetwork.sendToNearby(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                pos.x, pos.y, pos.z, vecX, vecY, vecZ), level, center);
    }

    protected int getRenderType() {
        return 1;
    }

    protected void addEnchantParticles(Entity target, float size, int age, HashMap<String, Float> stats) {
        double var = 0.15D;
        double maxX = target.getBoundingBox().maxX + var;
        double minX = target.getBoundingBox().minX - var;
        double maxZ = target.getBoundingBox().maxZ + var;
        double minZ = target.getBoundingBox().minZ - var;
        double vecX = target.getDeltaMovement().x;
        double vecY = 0.25D;
        double vecZ = target.getDeltaMovement().z;
        for ( int i = 0; i < 4; i++ ) {
            double randX = maxX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = getParticleColor(stats);
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = getParticleColor(stats);
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = getParticleColor(stats);
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = maxZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = getParticleColor(stats);
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
    }

    public static boolean allyFilter(Entity owner, Entity target, boolean isHarmful) {
        return target instanceof LivingEntity && !(target instanceof ArmorStand)
                //&& (owner != target || !isHarmful)
                && (ModCommonConfig.SPELL_FREE_FOR_ALL.get()
                || ((SpellEffectItem.isAlly(owner, target) && !isHarmful) || (!SpellEffectItem.isAlly(owner, target) && isHarmful)));
    }

    public boolean mobTypeFilter(Entity target) {
        return true;
    }

    public static boolean isAlly(Entity owner, Entity target) {
        boolean flag = false;
        if ( owner != null && target != null ) {
            if ( target == owner ) flag = true;
            if ( owner.isAlliedTo(target) ) flag = true;
            if ( owner instanceof LivingEntity livingOwner ) {
                if ( target instanceof LivingEntity livingTarget && !(livingOwner.canAttack(livingTarget)) ) flag = true;
                if ( target instanceof TamableAnimal pet && pet.isOwnedBy(livingOwner) ) flag = true;
                if ( target instanceof Mob mob && isMinionsSummoner(livingOwner, mob) ) flag = true;
            }
        }
        return flag;
    }

    public static boolean isMinionsOwner(LivingEntity owner, Mob mob) {
        return mob.hasEffect(ModEffects.MIND_CONTROL.get()) && mob.getPersistentData().hasUUID(MindControlEffect.NBT_KEY_CONTROL)
                && mob.getPersistentData().getUUID(MindControlEffect.NBT_KEY_CONTROL).equals(owner.getUUID()) && mob.getTarget() != owner;
    }

    public static boolean isMinionsSummoner(LivingEntity owner, Mob mob) {
        return mob.hasEffect(ModEffects.MIND_CONTROL.get()) && mob.getPersistentData().hasUUID(MindControlEffect.NBT_KEY_CONTROL)
                && mob.getPersistentData().getUUID(MindControlEffect.NBT_KEY_CONTROL).equals(owner.getUUID()) && mob.getTarget() != owner
                && mob.getPersistentData().getBoolean(MindControlEffect.NBT_KEY_SUMMON);
    }

    public static boolean hasLineOfSight(Entity start, Entity target) {
        Vec3 vec3 = new Vec3(start.getX(), start.getEyeY(), start.getZ());
        Vec3 vec31 = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        return start.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, start)).getType() == HitResult.Type.MISS;
    }

    public static void attackEntity(LivingEntity owner, Entity target, Entity source, float amount) {
        if ( target instanceof LivingEntity ) target.hurt(target.damageSources().indirectMagic(source, owner), amount);
    }

    public static void attackEntityWithoutKnockback(LivingEntity owner, Entity caster, Entity target, float amount) {
        final double vx = target.getDeltaMovement().x;
        final double vy = target.getDeltaMovement().y;
        final double vz = target.getDeltaMovement().z;
        target.hurt(target.damageSources().indirectMagic(caster, owner), amount);
        target.setDeltaMovement(vx, vy, vz);
        target.hurtMarked = true;
    }

    public static boolean isPushable(Entity entity) {
        return ( entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof PrimedTnt || entity instanceof FallingBlockEntity );
    }

    protected void playSound(Level level, Vec3 center) {
        playMagicSound(level, center);
    }

    public static void playWhiffSound(Entity caster) {
        if ( caster instanceof Player player ) player.playNotifySound(SoundEvents.NOTE_BLOCK_SNARE.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
    }

    public static void playMagicSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.25F, 2.0F);
    }

    public static void playMagicShootSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.25F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.25F, 2.0F);
    }

    public static void playMagicSummonSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.25F, 2.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.25F, 2.0F);
    }

    public static void playFireShootSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.25F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.25F, 1.0F);
    }

    public static void playWindSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundSource.PLAYERS, 2.0F, 0.02F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundSource.PLAYERS, 2.0F, 0.03F);
    }

    public static void playStormShootSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.35F, 2.0F);
    }

    public static void playTeleportSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    public static void playXpSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.25F, (new Random().nextFloat() - new Random().nextFloat()) * 0.35F + 0.9F);
    }
}

package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.config.ModCommonConfig;
import net.mindoth.ancientmagicks.event.DimVec3;
import net.mindoth.ancientmagicks.item.rune.shelf.effect.SpellComponentItem;
import net.mindoth.ancientmagicks.item.rune.shelf.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.mobeffect.MindControlEffect;
import net.mindoth.ancientmagicks.network.ModNetwork;
import net.mindoth.ancientmagicks.network.PacketSendCustomParticles;
import net.mindoth.ancientmagicks.registries.ModEffects;
import net.mindoth.ancientmagicks.event.MultiBlockHitResult;
import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.item.rune.UseOnBlockTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RuneItem extends Item {
    public RuneItem(Properties pProperties) {
        super(pProperties);
    }

    public SpellData resolve(Entity caster, SpellData spellData) {
        return spellData;
    }

    public static final String NBT_KEY_EMPTY = "am_empty";
    public static final String NBT_KEY_COMPONENT_DATA = "am_component_data_string";

    public boolean isEncodeable() {
        return false;
    }

    private boolean isEncodedComponent(ItemStack stack) {
        return stack.getItem() instanceof RuneItem rune && rune.isEncodeable() && stack.hasTag() && stack.getTag().contains(NBT_KEY_COMPONENT_DATA);
    }

    public void decodeTooltipData(List<Component> tooltip, String data, Item item) {
    }

    public String encodeComponentData(ItemStack stack) {
        if ( isEncodeable() && stack.hasTag() && stack.getTag().contains(NBT_KEY_COMPONENT_DATA) ) return stack.getTag().getString(NBT_KEY_COMPONENT_DATA);
        return NBT_KEY_EMPTY;
    }

    public static BlockPos getPosOfFace(BlockPos blockPos, Direction face) {
        return switch (face) {
            case UP -> blockPos.above();
            case EAST -> blockPos.east();
            case WEST -> blockPos.west();
            case SOUTH -> blockPos.south();
            case NORTH -> blockPos.north();
            case DOWN -> blockPos.below();
        };
    }

    protected static @NotNull List<BlockPos> getBlockList(RuneItem component, BlockHitResult bResult, BlockPos pos, int range) {
        List<BlockPos> blocks = Lists.newArrayList();
        if ( component instanceof UseOnBlockTemplate bte && !bte.isInside() ) pos = getPosOfFace(pos, bResult.getDirection());
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
        if ( !blocks.contains(pos) ) blocks.add(pos);
        return blocks;
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
                double vec = 0.05D + (0.25D - 0.05D) * new Random().nextDouble();
                //double vec = 0.15D;
                generateParticles(new Vec3(maxX, center.y - 0.5D + new Random().nextDouble(), minZ + (maxZ - minZ) * new Random().nextDouble()), center, level, size, age, 0, vec, 0, stats);
                generateParticles(new Vec3(minX, center.y - 0.5D + new Random().nextDouble(), minZ + (maxZ - minZ) * new Random().nextDouble()), center, level, size, age, 0, vec, 0, stats);
                generateParticles(new Vec3(minX + (maxX - minX) * new Random().nextDouble(), center.y - 0.5D + new Random().nextDouble(), minZ), center, level, size, age, 0, vec, 0, stats);
                generateParticles(new Vec3(minX + (maxX - minX) * new Random().nextDouble(), center.y - 0.5D + new Random().nextDouble(), maxZ), center, level, size, age, 0, vec, 0, stats);
            }
        }
    }

    protected void summonParticleLine(Vec3 startPos, Vec3 endPos, int amount, Vec3 center, Level level, float size, int age, HashMap<String, Float> stats) {
        double startX = startPos.x;
        double startY = startPos.y;
        double startZ = startPos.z;
        double endX = endPos.x;
        double endY = endPos.y;
        double endZ = endPos.z;
        double speed = 0.05D;
        for ( int k = 1; k < (1 + amount); k++ ) {
            double vecX = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
            double vecY = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
            double vecZ = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
            double lineX = startX * (1 - ((double) k / amount)) + endX * ((double) k / amount);
            double lineY = startY * (1 - ((double) k / amount)) + endY * ((double) k / amount);
            double lineZ = startZ * (1 - ((double) k / amount)) + endZ * ((double) k / amount);
            generateParticles(new Vec3(lineX, lineY, lineZ), center, level, size, age, vecX * speed, vecY * speed, vecZ * speed, stats);
        }
    }

    private void generateParticles(Vec3 pos, Vec3 center, Level level, float size, int age, double vecX, double vecY, double vecZ, HashMap<String, Float> stats) {
        ParticleColor.IntWrapper color = new ParticleColor.IntWrapper(getParticleColor(stats));
        ModNetwork.sendToNearby(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                pos.x, pos.y, pos.z, vecX, vecY, vecZ), level, center);
    }

    public static HashMap<String, Float> defaultStats() {
        HashMap<String, Float> stats = new HashMap<>();
        stats.put("red", -1.0F);
        stats.put("green", -1.0F);
        stats.put("blue", -1.0F);
        return stats;
    }

    public static ParticleColor getParticleColor(HashMap<String, Float> stats) {
        ParticleColor color = new ParticleColor(Mth.floor(stats.get(SpellComponentItem.RED)), Mth.floor(stats.get(SpellComponentItem.GREEN)), Mth.floor(stats.get(SpellComponentItem.BLUE)));
        if ( color.getRed() < 0 || color.getRed() > 255 || color.getGreen() < 0 || color.getGreen() > 255 || color.getBlue() < 0 || color.getBlue() > 255 ) {
            int r = new Random().nextInt(0, 256);
            int g = new Random().nextInt(0, 256);
            int b = new Random().nextInt(0, 256);
            return new ParticleColor(r, g, b);
        }
        else return color;
    }

    protected int getRenderType() {
        return 1;
    }

    protected void aoeEntitySpellParticles(Level level, AABB box, float range, HashMap<String, Float> stats) {
        Vec3 center = box.getCenter();
        BlockPos pos = new BlockPos(Mth.floor(center.x), Mth.floor(center.y), Mth.floor(center.z));
        double tempY = center.y;
        for ( int i = pos.getY(); i >= Mth.floor(center.y - range); i-- ) {
            BlockPos tempPos = new BlockPos(pos.getX(), i, pos.getZ());
            if ( level.getBlockState(tempPos).isSolid() ) break;
            else tempY = i;
        }
        box = box.move(0, -(center.y - tempY), 0);
        addAoeParticles(false, level, box, 0.15F, 8, stats);
    }

    protected void addEnchantParticles(Entity target, float size, int age, HashMap<String, Float> stats) {
        double var = 0.15D;
        double maxX = target.getBoundingBox().maxX + var;
        double minX = target.getBoundingBox().minX - var;
        double maxZ = target.getBoundingBox().maxZ + var;
        double minZ = target.getBoundingBox().minZ - var;
        //double vecX = target.getDeltaMovement().x;
        double vecX = 0.0D;
        double vecY = 0.25D;
        //double vecZ = target.getDeltaMovement().z;
        double vecZ = 0.0D;
        for ( int i = 0; i < 4; i++ ) {
            double randX = maxX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = new ParticleColor.IntWrapper(getParticleColor(stats));
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = new ParticleColor.IntWrapper(getParticleColor(stats));
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = new ParticleColor.IntWrapper(getParticleColor(stats));
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = maxZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = new ParticleColor.IntWrapper(getParticleColor(stats));
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
    }

    protected void aoeBlockSpellParticles(Level level, List<BlockPos> blocks, HashMap<String, Float> stats) {
        BlockPos start = new BlockPos(blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ());
        BlockPos end = new BlockPos(blocks.get(blocks.size() - 1).getX() + 1, blocks.get(blocks.size() - 1).getY() + 1, blocks.get(blocks.size() - 1).getZ() + 1);
        AABB particleBox = new AABB(start, end);
        addAoeParticles(true, level, particleBox, 0.15F, 8, stats);
    }

    public static MultiBlockHitResult getPOVHitResult(Vec3 position, Vec3 direction, Entity caster, Level level, ClipContext.Fluid pFluidMode, float range) {
        direction = direction.multiply(range, range, range);
        Vec3 vec31 = position.add(direction);
        BlockHitResult result = level.clip(new ClipContext(position, vec31, ClipContext.Block.OUTLINE, pFluidMode, caster));
        return new MultiBlockHitResult(result.getLocation(), result.getDirection(), result.getBlockPos(), result.isInside(),
                Collections.singletonList(result.getBlockPos()), new DimVec3(result.getLocation(), level));
    }

    public static Vec3 getPoint(Vec3 position, Vec3 direction, Entity caster, Level level, float range, float error, boolean centerBlock, boolean stopsAtEntity, boolean stopsAtSolid, boolean stopsAtLiquid) {
        direction = direction.multiply(range, range, range);
        Vec3 center = position.add(direction);
        Vec3 returnPoint = center;
        double playerX = position.x();
        double playerY = position.y();
        double playerZ = position.z();
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(position.distanceToSqr(center));
        for ( int k = 1; k < 1 + particleInterval; ++k ) {
            double lineX = playerX * (1.0D - (double)k / (double)particleInterval) + listedEntityX * ((double)k / (double)particleInterval);
            double lineY = playerY * (1.0D - (double)k / (double)particleInterval) + listedEntityY * ((double)k / (double)particleInterval);
            double lineZ = playerZ * (1.0D - (double)k / (double)particleInterval) + listedEntityZ * ((double)k / (double)particleInterval);
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( closestSoFar instanceof LivingEntity) {
                    double testDistance = closestSoFar.distanceToSqr(center);
                    if ( testDistance < lowestSoFar ) target = closestSoFar;
                }
            }
            if ( stopsAtEntity && target != null ) {
                if ( centerBlock ) {
                    BlockPos pos = target.blockPosition();
                    returnPoint = pos.getCenter();
                }
                break;
            }
            if ( stopsAtLiquid && level.getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).getBlock() instanceof LiquidBlock) {
                if ( centerBlock ) {
                    BlockPos pos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
                    returnPoint = pos.getCenter();
                }
                break;
            }
            if ( stopsAtSolid && level.getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).isSolid() ) {
                if ( centerBlock ) {
                    BlockPos pos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
                    returnPoint = pos.getCenter();
                }
                break;
            }
            returnPoint = new Vec3(lineX, lineY, lineZ);
        }
        return returnPoint;
    }

    public static boolean allyFilter(Entity owner, Entity target, boolean isHarmful) {
        return target instanceof LivingEntity && !(target instanceof ArmorStand)
                //&& (owner != target || !isHarmful)
                && (ModCommonConfig.SPELL_FREE_FOR_ALL.get()
                || ((SpellEffectItem.isAlly(owner, target) && !isHarmful) || (!SpellEffectItem.isAlly(owner, target) && isHarmful)));
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
}

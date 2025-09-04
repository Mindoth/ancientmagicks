package net.mindoth.ancientmagicks.revamp.item.rune;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.SpellComponentItem;
import net.mindoth.ancientmagicks.network.ModNetwork;
import net.mindoth.ancientmagicks.network.PacketSendCustomParticles;
import net.mindoth.ancientmagicks.registries.ModItems;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RuneItem extends Item {
    public RuneItem(Properties pProperties) {
        super(pProperties);
    }

    //ONLY FOR TESTING
    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(hand));
        if ( !level.isClientSide ) {
            List<Item> runeList = Lists.newArrayList();

            runeList.add(ModItems.SELF_RUNE_ITEM.get());
            runeList.add(ModItems.LOOK_DIRECTION_RUNE_ITEM.get());
            runeList.add(ModItems.SELF_RUNE_ITEM.get());
            runeList.add(ModItems.TARGET_FACE_RUNE_ITEM.get());
            runeList.add(ModItems.SELF_RUNE_ITEM.get());
            runeList.add(ModItems.BLOCK_POSITION_RUNE_ITEM.get());
            runeList.add(ModItems.TELEPORT_RUNE_ITEM.get());

            resolveStack(player, runeList);
        }
        return result;
    }

    public static void resolveStack(Entity caster, List<Item> runeList) {
        SpellData spellData = new SpellData();
        for ( Item item : runeList ) if ( item instanceof RuneItem rune ) {
            spellData = rune.resolve(caster, spellData);
            if ( !spellData.isValid() ) {
                if ( caster instanceof Player player ) player.displayClientMessage(Component.literal("FAILED SPELL"), false);
                break;
            }
        }
    }

    public SpellData resolve(Entity caster, SpellData spellData) {
        return spellData;
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

    public HashMap<String, Float> defaultStats() {
        HashMap<String, Float> stats = new HashMap<>();
        stats.put("red", -1.0F);
        stats.put("green", -1.0F);
        stats.put("blue", -1.0F);
        return stats;
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

    protected void aoeBlockSpellParticles(Level level, List<BlockPos> blocks, HashMap<String, Float> stats) {
        BlockPos start = new BlockPos(blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ());
        BlockPos end = new BlockPos(blocks.get(blocks.size() - 1).getX() + 1, blocks.get(blocks.size() - 1).getY() + 1, blocks.get(blocks.size() - 1).getZ() + 1);
        AABB particleBox = new AABB(start, end);
        addAoeParticles(true, level, particleBox, 0.15F, 8, stats);
    }
}

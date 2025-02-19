package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.mindoth.ancientmagicks.item.spell.BlockTargetSpell;
import net.mindoth.ancientmagicks.item.spell.EntityTargetSpell;
import net.mindoth.ancientmagicks.item.temp.mindcontrol.MindControlEffect;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSendCustomParticles;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SpellItem extends Item {

    public boolean castMagic(LivingEntity owner, Entity caster, Vec3 center, int useTime) {
        return false;
    }

    public static final String POWER = "power";
    public static final String LIFE = "life";
    public static final String AOE = "aoe";
    public static final String REACH = "reach";
    public static final String GRAVITY = "gravity";

    public static HashMap<String, Float> createStatsList() {
        HashMap<String, Float> stats = new HashMap<>();
        stats.merge(POWER, 1.0F, Float::sum);
        stats.merge(LIFE, 160.0F, Float::sum);
        stats.merge(AOE, 0.0F, Float::sum);
        stats.merge(REACH, 0.0F, Float::sum);
        stats.merge(GRAVITY, 0.0F, Float::sum);
        return stats;
    }

    public static HashMap<String, Float> createSpellStats(List<SpellModifierItem> modifiers) {
        HashMap<String, Float> stats = createStatsList();
        for ( SpellModifierItem item : modifiers ) item.addStatsToMap(stats);
        return stats;
    }

    protected boolean canApply(Level level, LivingEntity owner, Entity caster, HitResult result) {
        return true;
    }

    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats) {
        return canApply(level, owner, caster, result);
    }

    public boolean castSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats) {
        boolean state = false;
        float range = 0.0F;
        Vec3 center = result.getLocation();
        if ( stats.containsKey(SpellItem.AOE) && stats.get(SpellItem.AOE) > 0.0F ) range = stats.get(SpellItem.AOE);
        if ( this instanceof EntityTargetSpell ) {
            if ( range > 0.0F ) {
                range *= 2;
                Vec3 start = new Vec3(center.x + range, center.y + range, center.z + range);
                Vec3 end = new Vec3(center.x - range, center.y - range, center.z - range);
                AABB box = new AABB(start, end);
                List<Entity> entities = level.getEntitiesOfClass(Entity.class, box);
                for ( Entity entity : entities ) {
                    EntityHitResult entityHitResult = new EntityHitResult(entity);
                    if ( canApply(level, owner, caster, entityHitResult) ) doSpell(level, owner, caster, entityHitResult, stats);
                }
                state = true;
                aoeEntitySpellParticles(level, result, caster, center, range);
            }
            else if ( canApply(level, owner, caster, result) ) state = doSpell(level, owner, caster, result, stats);
        }
        else if ( this instanceof BlockTargetSpell ) {
            if ( range > 0.0F ) {
                if ( result instanceof BlockHitResult || result instanceof EntityHitResult ) {
                    List<BlockPos> blocks;
                    if ( result instanceof EntityHitResult entityHitResult ) blocks = getBlockList(entityHitResult.getEntity().getOnPos(), (int)range);
                    else blocks = getBlockList(((BlockHitResult)result).getBlockPos(), (int)range);
                    for ( BlockPos position : blocks ) {
                        BlockHitResult newResult = new BlockHitResult(position.getCenter(), Direction.UP, position, true);
                        if ( canApply(level, owner, caster, result) ) doSpell(level, owner, caster, newResult, stats);
                    }
                    state = true;
                    for ( int i = -(int)range; i <= range; i++ ) aoeBlockSpellParticles(caster, center, range, i);
                }
            }
            else if ( canApply(level, owner, caster, result) ) state = doSpell(level, owner, caster, result, stats);
        }
        return state;
    }

    private static @NotNull List<BlockPos> getBlockList(BlockPos pos, int range) {
        List<BlockPos> blocks = Lists.newArrayList();

        for ( int xPos = pos.getX() - range; xPos <= pos.getX() + range; xPos++ )
            for ( int yPos = pos.getY() - range; yPos <= pos.getY() + range; yPos++ )
                for ( int zPos = pos.getZ() - range; zPos <= pos.getZ() + range; zPos++ ) {
                    blocks.add(new BlockPos(xPos, yPos, zPos));
                }
        blocks.add(pos);
        return blocks;
    }

    private final int manaCost;
    public int getManaCost() {
        return this.manaCost;
    }

    private final int cooldown;
    public int getCooldown() {
        return this.cooldown;
    }

    public boolean isChannel() {
        return false;
    }

    public boolean isHarmful() {
        return true;
    }

    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.DARK_PURPLE.getParticleColor();
    }

    public SpellItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties);
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.spellTier = spellTier;
    }
    private final int spellTier;
    public int getSpellTier() {
        return this.spellTier;
    }

    public enum SpellType {
        ATTACK(1),
        BUFF(2),
        SUMMON(2),
        SPECIAL(1);

        private int multiplier;
        SpellType(int i) {
            this.multiplier = i;
        }
        int getMultiplier() {
            return this.multiplier;
        }
        SpellType getType() {
            return this;
        }
    }

    public SpellItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties);
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.spellTier = 0;
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

    public static int rollForPower(int power, int die) {
        int finalHit = 0;
        for ( int i = 0; i < power; i++ ) {
            Random rand = new Random();
            int roll = (rand.nextInt(die) + 1);
            finalHit += roll;
        }
        return finalHit;
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

    public boolean allyFilter(Entity owner, Entity target) {
        return target instanceof LivingEntity && !(target instanceof ArmorStand)
                /*&& (owner != target || !isHarmful())*/
                && (AncientMagicksCommonConfig.SPELL_FREE_FOR_ALL.get()
                || ((SpellItem.isAlly(owner, target) && !isHarmful()) || (!SpellItem.isAlly(owner, target) && isHarmful())));
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
        return mob.hasEffect(AncientMagicksEffects.MIND_CONTROL.get()) && mob.getPersistentData().hasUUID(MindControlEffect.NBT_KEY_CONTROL)
                && mob.getPersistentData().getUUID(MindControlEffect.NBT_KEY_CONTROL).equals(owner.getUUID()) && mob.getTarget() != owner;
    }

    public static boolean isMinionsSummoner(LivingEntity owner, Mob mob) {
        return mob.hasEffect(AncientMagicksEffects.MIND_CONTROL.get()) && mob.getPersistentData().hasUUID(MindControlEffect.NBT_KEY_CONTROL)
                && mob.getPersistentData().getUUID(MindControlEffect.NBT_KEY_CONTROL).equals(owner.getUUID()) && mob.getTarget() != owner
                && mob.getPersistentData().getBoolean(MindControlEffect.NBT_KEY_SUMMON);
    }

    public static boolean hasLineOfSight(Entity start, Entity target) {
        Vec3 vec3 = new Vec3(start.getX(), start.getEyeY(), start.getZ());
        Vec3 vec31 = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        return start.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, start)).getType() == HitResult.Type.MISS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack tablet, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player && tablet.getItem() instanceof SpellItem spellItem) {
            CastingItem.doSpell(player, player, tablet, spellItem, getUseDuration(tablet) - timeLeft);
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    protected int getRenderType() {
        return 1;
    }

    private void aoeEntitySpellParticles(Level level, HitResult result, Entity caster, Vec3 oldCenter, float range) {
        BlockPos pos = new BlockPos(Mth.floor(oldCenter.x), Mth.floor(oldCenter.y), Mth.floor(oldCenter.z));
        if ( result instanceof BlockHitResult blockHitResult ) pos = getPosOfFace(blockHitResult.getBlockPos(), blockHitResult.getDirection());
        Vec3 center = oldCenter;
        for ( int i = pos.getY(); i >= Mth.floor(oldCenter.y - range); i-- ) {
            BlockPos tempPos = new BlockPos(pos.getX(), i, pos.getZ());
            if ( level.getBlockState(tempPos).isSolid() ) break;
            else center = new Vec3(oldCenter.x, i, oldCenter.z);
        }
        Vec3 particleStart = new Vec3(center.x + range, center.y + range, center.z + range);
        Vec3 particleEnd = new Vec3(center.x - range, center.y - range, center.z - range);
        AABB particleBox = new AABB(particleStart, particleEnd);
        addAoeParticles(caster, particleBox, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8, 0.15D);
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

    private void aoeBlockSpellParticles(Entity caster, Vec3 center, float range, int addition) {
        Vec3 start = new Vec3(Mth.ceil(center.x + range), Mth.ceil(center.y + range + addition), Mth.ceil(center.z + range));
        Vec3 end = new Vec3(Mth.floor(center.x - range), Mth.floor(center.y - range + addition), Mth.floor(center.z - range));
        AABB particleBox = new AABB(start, end);
        addAoeParticles(caster, particleBox, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8, 0.0D);
    }

    protected void addAoeParticles(Entity caster, AABB box, int r, int g, int b, float size, int age, double lift) {
        Vec3 center = box.getCenter();
        double maxX = box.maxX;
        double minX = box.minX;
        double maxZ = box.maxZ;
        double minZ = box.minZ;
        double vecX = 0;
        double vecY = lift;
        double vecZ = 0;
        int amount = 4 * (int)box.getYsize();
        for ( int i = 0; i < amount; i++ ) {
            double randX = maxX;
            double randY = center.y - 0.5D + new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), caster, true);
        }
        for ( int i = 0; i < amount; i++ ) {
            double randX = minX;
            double randY = center.y - 0.5D + new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), caster, true);
        }
        for ( int i = 0; i < amount; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = center.y - 0.5D + new Random().nextDouble();
            double randZ = minZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), caster, true);
        }
        for ( int i = 0; i < amount; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = center.y - 0.5D + new Random().nextDouble();
            double randZ = maxZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), caster, true);
        }
    }

    protected void addEnchantParticles(Entity target, int r, int g, int b, float size, int age) {
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
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = maxZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        SpellItem spell = this;
        if ( ColorRuneItem.CURRENT_COMBO_MAP.containsKey(spell) && Minecraft.getInstance().player != null ) {
            StringBuilder tooltipString = new StringBuilder();
            List<ColorRuneItem> list = ColorRuneItem.stringListToActualList(ColorRuneItem.CURRENT_COMBO_MAP.get(spell).toString());
            for ( ColorRuneItem rune : list ) {
                String color = rune.color + "0" + "\u00A7r";
                tooltipString.append(color);
            }
            tooltip.add(Component.literal(String.valueOf(tooltipString)));
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }
}

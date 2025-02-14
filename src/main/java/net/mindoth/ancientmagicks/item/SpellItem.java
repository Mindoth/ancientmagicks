package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlEffect;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSendCustomParticles;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class SpellItem extends Item {

    public boolean castMagic(LivingEntity owner, Entity caster, Vec3 center, int useTime) {
        return false;
    }

    private final SpellType spellType;

    private final int spellTier;
    public int getSpellTier() {
        return this.spellTier;
    }

    private final int manaCost;
    public int getManaCost() {
        return this.manaCost;
    }

    private final int cooldown;
    public int getCooldown() {
        return this.cooldown;
    }

    /*private final int spellXpTier;
    public int getSpellXpTier() {
        return this.spellXpTier;
    }

    private final int spellXpCost;
    public int getSpellXpCost() {
        return this.spellXpCost;
    }*/

    public boolean isCraftable() {
        return true;
    }

    public boolean isChannel() {
        return false;
    }

    public boolean isHarmful() {
        return this.spellType.getType() != SpellType.BUFF;
    }

    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.DARK_PURPLE.getParticleColor();
    }

    public SpellItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties);
        this.spellTier = spellTier;
        if ( manaCost < 0 ) this.manaCost = isChannel() ? spellTier * 2 : spellTier * spellType.getMultiplier();
        else this.manaCost = manaCost;
        this.cooldown = cooldown * 20;
        this.spellType = spellType;
        /*int spellXpTier = 0;
        int spellXpCost = switch (spellTier) {
            case 1, 2, 3 -> {
                spellXpTier = 5;
                yield 1;
            }
            case 4, 5, 6 -> {
                spellXpTier = 15;
                yield 2;
            }
            case 7, 8, 9 -> {
                spellXpTier = 30;
                yield 3;
            }
            default -> 0;
        };
        this.spellXpTier = spellXpTier;
        this.spellXpCost = spellXpCost;*/
    }

    public enum SpellType {
        ATTACK(5),
        BUFF(10),
        SUMMON(10),
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

    //Similar to AbstractSpellEntity filter() method. CHANGE BOTH WHEN EDITING!
    public boolean filter(Entity owner, Entity target) {
        return target instanceof LivingEntity && !(target instanceof ArmorStand) && (owner != target || !isHarmful())
                && (AncientMagicksCommonConfig.SPELL_FREE_FOR_ALL.get()
                || ((SpellItem.isAlly(owner, target) && !isHarmful()) || (!SpellItem.isAlly(owner, target) && isHarmful())));
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

    protected void addEnchantParticles(Entity target, int r, int g, int b, float size, int age, int renderType) {
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
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, renderType,
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, renderType,
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, renderType,
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = maxZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, renderType,
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
}

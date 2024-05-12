package net.mindoth.ancientmagicks.item.spellrune.raisedead;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonedMinion;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonerHelper;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.goal.*;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class SkeletonMinionEntity extends Skeleton implements SummonedMinion {

    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;

    public SkeletonMinionEntity(EntityType<? extends Skeleton> pEntityType, Level pLevel) {
        super(AncientMagicksEntities.SKELETON_MINION.get(), pLevel);
        xpReward = 0;
    }

    public SkeletonMinionEntity(Level pLevel, LivingEntity owner) {
        this(AncientMagicksEntities.SKELETON_MINION.get(), pLevel);
        setSummoner(owner);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(1, new GenericSummonerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericSummonerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopySummonerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
        this.goalSelector.addGoal(5, new GenericFollowSummonerGoal(this, this::getSummoner, 0.9f, 15, 5, false, 25));
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes().build();
    }

    @Override
    public boolean isPreventingPlayerRest(Player pPlayer) {
        return !this.isAlliedTo(pPlayer);
    }

    @Override
    public LivingEntity getSummoner() {
        return SummonerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if ( owner != null ) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    @Override
    public void onRemovedFromWorld() {
        this.onRemovedHelper(this, AncientMagicksEffects.SKELETON_TIMER.get());
        super.onRemovedFromWorld();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.summonerUUID = SummonerHelper.deserializeOwner(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        SummonerHelper.serializeOwner(compoundTag, summonerUUID);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedSummon(pEntity);
    }

    @Override
    public void onUnSummon() {
        if ( !level().isClientSide ) {
            for ( int i = 0; i < 6; i++ ) {
                ItemStack itemstack;
                if ( i == 0 ) itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
                else if ( i == 1 ) itemstack = this.getItemBySlot(EquipmentSlot.OFFHAND);
                else if ( i == 2 ) itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
                else if ( i == 3 ) itemstack = this.getItemBySlot(EquipmentSlot.CHEST);
                else if ( i == 4 ) itemstack = this.getItemBySlot(EquipmentSlot.LEGS);
                else itemstack = this.getItemBySlot(EquipmentSlot.FEET);
                if ( !itemstack.isEmpty() ) this.spawnAtLocation(itemstack);
            }
            this.spawnAnim();
            discard();
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pRandom, pDifficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
    }

    @Override
    public boolean canPickUpLoot() {
        return true;
    }
}

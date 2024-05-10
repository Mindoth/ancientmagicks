package net.mindoth.ancientmagicks.item.spellrune.raisedead;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonedMinion;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonerHelper;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.goal.*;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class SkeletonMinionEntity extends SkeletonEntity implements SummonedMinion {

    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;

    public SkeletonMinionEntity(EntityType<? extends SkeletonEntity> pEntityType, World pLevel) {
        super(AncientMagicksEntities.SKELETON_MINION.get(), pLevel);
        xpReward = 0;
    }

    public SkeletonMinionEntity(World pLevel, LivingEntity owner) {
        this(AncientMagicksEntities.SKELETON_MINION.get(), pLevel);
        setSummoner(owner);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, MobEntity.class, 8.0F));

        this.targetSelector.addGoal(1, new GenericSummonerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericSummonerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopySummonerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
        this.goalSelector.addGoal(5, new GenericFollowSummonerGoal(this, this::getSummoner, 0.9f, 15, 5, false, 25));
    }

    public static AttributeModifierMap setAttributes() {
        return MonsterEntity.createMonsterAttributes().build();
    }

    @Override
    public boolean isPreventingPlayerRest(PlayerEntity pPlayer) {
        return !this.isAlliedTo(pPlayer);
    }

    @Override
    public LivingEntity getSummoner() {
        return SummonerHelper.getAndCacheOwner(level, cachedSummoner, summonerUUID);
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
    public void readAdditionalSaveData(CompoundNBT compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.summonerUUID = SummonerHelper.deserializeOwner(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        SummonerHelper.serializeOwner(compoundTag, summonerUUID);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedSummon(pEntity);
    }

    @Override
    public void onUnSummon() {
        if ( !level.isClientSide ) {
            for ( int i = 0; i < 6; i++ ) {
                ItemStack itemstack;
                if ( i == 0 ) itemstack = this.getItemBySlot(EquipmentSlotType.MAINHAND);
                else if ( i == 1 ) itemstack = this.getItemBySlot(EquipmentSlotType.OFFHAND);
                else if ( i == 2 ) itemstack = this.getItemBySlot(EquipmentSlotType.HEAD);
                else if ( i == 3 ) itemstack = this.getItemBySlot(EquipmentSlotType.CHEST);
                else if ( i == 4 ) itemstack = this.getItemBySlot(EquipmentSlotType.LEGS);
                else itemstack = this.getItemBySlot(EquipmentSlotType.FEET);
                if ( !itemstack.isEmpty() ) this.spawnAtLocation(itemstack);
            }
            this.spawnAnim();
            remove();
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pDifficulty);
        this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
    }

    @Override
    public boolean canPickUpLoot() {
        return true;
    }
}

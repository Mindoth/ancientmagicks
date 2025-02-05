package net.mindoth.ancientmagicks.item.spell.summondeathknight;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellSummon;
import net.mindoth.ancientmagicks.item.spell.abstractspell.ColorCode;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class SummonDeathKnightItem extends AbstractSpellSummon {

    public SummonDeathKnightItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    public static final String NBT_KEY_UNDEAD_ENDURANCE = "am_is_summon_undead";

    @Override
    public ParticleColor.IntWrapper getColor() {
        return AbstractSpellEntity.getSpellColor(ColorCode.BLACK);
    }

    @Override
    protected void populateSlots(Mob minion) {
        minion.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        minion.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        minion.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        minion.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        minion.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        minion.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
    }

    @Override
    protected Mob getMinion(Level level) {
        Mob minion = new WitherSkeleton(EntityType.WITHER_SKELETON, level);
        minion.getPersistentData().putBoolean(NBT_KEY_UNDEAD_ENDURANCE, true);
        return minion;
    }

    @SubscribeEvent
    public static void onDeathKnightLethal(final LivingDamageEvent event) {
        if ( event.getEntity().level().isClientSide ) return;
        LivingEntity living = event.getEntity();
        CompoundTag tag = living.getPersistentData();
        if ( !(living instanceof WitherSkeleton) ) return;
        if ( tag.getBoolean(NBT_KEY_UNDEAD_ENDURANCE) && tag.getBoolean(MindControlEffect.NBT_KEY_SUMMON) ) {
            if ( event.getAmount() >= living.getHealth() && event.getAmount() < living.getMaxHealth() ) {
                float newAmount = event.getAmount() + (living.getHealth() - event.getAmount() - 1);
                event.setAmount(newAmount);
                tag.remove(NBT_KEY_UNDEAD_ENDURANCE);
                living.level().playSound(null, living.getOnPos(), SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 1.0F, 0.5F);
            }
        }
    }
}

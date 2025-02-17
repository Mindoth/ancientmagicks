package net.mindoth.ancientmagicks.item.temp.summonskeletonwarrior;

import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellSummon;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SummonSkeletonWarriorItem extends AbstractSpellSummon {

    public SummonSkeletonWarriorItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    protected void populateSlots(Mob minion) {
        minion.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        minion.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        minion.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
        minion.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        minion.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
        minion.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
    }

    @Override
    protected Mob getMinion(Level level) {
        Mob minion = new Skeleton(EntityType.SKELETON, level);
        return minion;
    }
}

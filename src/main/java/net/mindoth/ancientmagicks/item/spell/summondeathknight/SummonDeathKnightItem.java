package net.mindoth.ancientmagicks.item.spell.summondeathknight;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellSummon;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SummonDeathKnightItem extends AbstractSpellSummon {

    public SummonDeathKnightItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected Mob getMinion(Level level) {
        Mob minion = new Zombie(EntityType.ZOMBIE, level);
        minion.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        minion.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        minion.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        minion.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        minion.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        minion.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
        return minion;
    }
}

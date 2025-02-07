package net.mindoth.ancientmagicks.item.spell.summonskeletoncaster;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellSummon;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SummonSkeletonCasterItem extends AbstractSpellSummon {

    public SummonSkeletonCasterItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    protected void populateSlots(Mob minion) {
        minion.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(AncientMagicksItems.WOODEN_STAFF.get()));
        minion.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        minion.setItemSlot(EquipmentSlot.HEAD, new ItemStack(AncientMagicksItems.SIMPLE_HOOD.get()));
        minion.setItemSlot(EquipmentSlot.CHEST, new ItemStack(AncientMagicksItems.SIMPLE_ROBE_TOP.get()));
        minion.setItemSlot(EquipmentSlot.LEGS, new ItemStack(AncientMagicksItems.SIMPLE_ROBE_BOTTOM.get()));
        minion.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
    }

    @Override
    protected Mob getMinion(Level level) {
        Mob minion = new Skeleton(EntityType.SKELETON, level);
        int i = 20;
        if ( minion.level().getDifficulty() != Difficulty.HARD ) i = 40;
        //TODO rework so goal stays on world reload
        minion.goalSelector.addGoal(4, new RangedMagickAttackGoal<>((Skeleton) minion, 1.0D, i, 15.0F));
        return minion;
    }
}

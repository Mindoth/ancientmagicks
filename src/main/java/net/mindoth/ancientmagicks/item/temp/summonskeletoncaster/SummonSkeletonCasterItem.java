package net.mindoth.ancientmagicks.item.temp.summonskeletoncaster;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellSummon;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
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
        minion.setItemSlot(EquipmentSlot.HEAD, new ItemStack(AncientMagicksItems.ROBE_HOOD.get()));
        minion.setItemSlot(EquipmentSlot.CHEST, new ItemStack(AncientMagicksItems.ROBE_TOP.get()));
        minion.setItemSlot(EquipmentSlot.LEGS, new ItemStack(AncientMagicksItems.ROBE_BOTTOM.get()));
        minion.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
    }

    @Override
    protected Mob getMinion(Level level) {
        Mob minion = new Skeleton(EntityType.SKELETON, level);
        //SpellItem spell = (SpellItem)AncientMagicksItems.FIRE_BOLT_ITEM.get();
        //TODO rework so goal stays on world reload
        //minion.goalSelector.addGoal(4, new RangedMagickAttackGoal<>((Skeleton)minion, 1.0D, spell.getCooldown(), 15.0F, spell));
        return minion;
    }
}

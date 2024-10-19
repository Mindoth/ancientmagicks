package net.mindoth.ancientmagicks.item.spell.summondeathknight;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonDeathKnightItem extends SpellItem {

    public SummonDeathKnightItem(Properties pProperties, int spellLevel) {
        super(pProperties, spellLevel);
    }

    @Override
    public int getCooldown() {
        return 600;
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Mob minion = new Zombie(EntityType.ZOMBIE, level);
        minion.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        minion.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        minion.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        minion.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        minion.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        minion.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));

        Vec3 pos = caster.position();
        Vec3 newPos = null;
        while ( newPos == null || !minion.position().equals(newPos) ) {
            double d3 = pos.x + (minion.getRandom().nextDouble() - 0.5D) * 3.0D;
            double d4 = Mth.clamp(pos.y + (double)(minion.getRandom().nextInt(4) - 2),
                    (double)owner.level().getMinBuildHeight(), (double)(owner.level().getMinBuildHeight() + ((ServerLevel)owner.level()).getLogicalHeight() - 1));
            double d5 = pos.z + (minion.getRandom().nextDouble() - 0.5D) * 3.0D;
            net.minecraftforge.event.entity.EntityTeleportEvent.ChorusFruit event = net.minecraftforge.event.ForgeEventFactory.onChorusFruitTeleport(owner, d3, d4, d5);
            newPos = new Vec3(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            minion.randomTeleport(newPos.x, newPos.y, newPos.z, true);
        }
        summonMinion(minion, owner, owner.level());
        state = true;

        if ( state ) playMagicSummonSound(level, center);

        return state;
    }
}

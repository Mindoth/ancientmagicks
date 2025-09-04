package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlockPositionRuneItem extends UseOnBlockTemplate {
    public BlockPositionRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData result(Entity caster, SpellData spellData, BlockPos pos, Level level) {
        spellData.addVector(new Vec3(pos.getX(), pos.getY(), pos.getZ()));
        return spellData;
    }
}

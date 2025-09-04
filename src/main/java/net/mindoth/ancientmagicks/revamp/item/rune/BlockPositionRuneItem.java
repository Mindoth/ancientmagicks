package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlockPositionRuneItem extends RuneItem {
    public BlockPositionRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getBlocks().isEmpty() ) {
            BlockPos blockPos = spellData.getLatestBlock().getBlockPos();
            spellData.purgeBlocks(1);
            spellData.addVector(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        }
        else spellData.setValid(false);
        return spellData;
    }
}

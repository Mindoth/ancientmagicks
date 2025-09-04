package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.MultiBlockHitResult;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class UseOnBlockTemplate extends RuneItem {
    public UseOnBlockTemplate(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getBlocks().isEmpty() ) {
            if ( spellData.getLatestBlock() != null ) {
                MultiBlockHitResult result = spellData.getLatestBlock();
                spellData.purgeBlocks(1);

                final SpellData copyData = SpellData.clone(spellData);
                spellData = result(caster, spellData, result.getBlocks().get(0), result.getLevel());
                boolean state = spellData.isValid();
                for ( int i = 1; i < result.getBlocks().size(); i++ ) {
                    final SpellData tempData = SpellData.clone(copyData);
                    BlockPos blockPos = result.getBlocks().get(i);
                    result(caster, tempData, blockPos, result.getLevel());
                    if ( !state && tempData.isValid() ) state = true;
                }
                spellData.setValid(state);
            }
        }
        else spellData.setValid(false);
        return spellData;
    }

    protected SpellData result(Entity caster, SpellData spellData, BlockPos blockPos, Level level) {
        return spellData;
    }

    protected boolean isInside() {
        return true;
    }
}

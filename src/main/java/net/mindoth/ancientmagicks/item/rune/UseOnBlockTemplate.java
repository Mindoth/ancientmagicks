package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.MultiBlockHitResult;
import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.item.RuneItem;
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
                spellData = result(caster, spellData, result, result.getBlocks().get(0));
                boolean state = spellData.isValid();
                for ( int i = 1; i < result.getBlocks().size(); i++ ) {
                    final SpellData tempData = SpellData.clone(copyData);
                    BlockPos blockPos = result.getBlocks().get(i);
                    result(caster, tempData, result, blockPos);
                    if ( !state && tempData.isValid() ) state = true;
                }
                spellData.setValid(state);
            }
            else spellData.purgeBlocks(1);
        }
        else spellData.setValid(false);
        return spellData;
    }

    protected SpellData result(Entity caster, SpellData spellData, MultiBlockHitResult result, BlockPos blockPos) {
        return spellData;
    }

    public boolean isInside() {
        return true;
    }
}

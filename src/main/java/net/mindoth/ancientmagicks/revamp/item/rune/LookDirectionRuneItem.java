package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

public class LookDirectionRuneItem extends RuneItem {
    public LookDirectionRuneItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() ) {
            Entity entity = spellData.getEntities().get(spellData.getEntities().size() - 1);
            spellData.addVector(entity.getLookAngle());
            spellData.purgeEntities(1);
        }
        else spellData.setValid(false);
        return spellData;
    }
}

package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;

public class LookDirectionRuneItem extends RuneItem {
    public LookDirectionRuneItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() ) {
            Entity entity = spellData.getLatestEntity().getEntity();
            spellData.addVector(entity.getLookAngle());
            spellData.purgeEntities(1);
        }
        else spellData.setValid(false);
        return spellData;
    }
}

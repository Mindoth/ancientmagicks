package net.mindoth.ancientmagicks.revamp.item;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.entity.Entity;

public class TargetEntityRuneItem extends RuneItem {
    public TargetEntityRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() ) {
            Entity entity = spellData.getEntities().get(spellData.getEntities().size() - 1);
            spellData.purgeEntities(1);
            if ( entity != null ) {
                Entity target = ShadowEvents.getPointedEntity(entity.level(), entity, 4.5F, 0, true, null);
                if ( target != entity ) spellData.addEntity(target);
                else spellData.addEntity(null);
            }
            else spellData.addEntity(null);
        }
        else spellData.setValid(false);
        return spellData;
    }
}

package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityPositionRuneItem extends UseOnEntityTemplate {
    public EntityPositionRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected SpellData result(Entity caster, SpellData spellData, Entity entity) {
        spellData.addVector(new Vec3(entity.getX(), entity.getY(), entity.getZ()));
        return spellData;
    }
}

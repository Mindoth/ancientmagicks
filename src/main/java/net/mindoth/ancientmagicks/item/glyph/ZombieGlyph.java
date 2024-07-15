package net.mindoth.ancientmagicks.item.glyph;

import net.mindoth.ancientmagicks.item.MinionGlyphItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class ZombieGlyph extends MinionGlyphItem {
    public ZombieGlyph(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected Mob getMob(Level level) {
        return new Zombie(EntityType.ZOMBIE, level);
    }
}

package net.mindoth.ancientmagicks.item.glyph;

import net.mindoth.ancientmagicks.item.MinionGlyphItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;

public class SkeletonGlyph extends MinionGlyphItem {
    public SkeletonGlyph(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected Mob getMob(Level level) {
        return new Skeleton(EntityType.SKELETON, level);
    }
}

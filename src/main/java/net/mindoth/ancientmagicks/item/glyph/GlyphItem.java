package net.mindoth.ancientmagicks.item.glyph;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class GlyphItem extends Item {
    public String tag;
    public GlyphItem(Properties pProperties, String tag) {
        super(pProperties);
        this.tag = tag;
    }

    public void onEntityHit(Level level, EntityHitResult result) {
    }

    public void onBlockHit(Level level, BlockHitResult result) {
    }
}

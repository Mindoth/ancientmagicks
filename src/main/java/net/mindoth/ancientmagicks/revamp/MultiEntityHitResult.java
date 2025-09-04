package net.mindoth.ancientmagicks.revamp;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class MultiEntityHitResult extends EntityHitResult {

    private final List<Entity> entities;
    public List<Entity> getEntities() {
        return this.entities;
    }

    public MultiEntityHitResult(Entity pEntity, Vec3 pLocation, List<Entity> entities) {
        super(pEntity, pLocation);
        this.entities = entities;
    }
}

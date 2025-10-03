package net.mindoth.ancientmagicks.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MultiEntityHitResult extends EntityHitResult {

    private final List<Entity> entities;
    public List<Entity> getEntities() {
        return this.entities;
    }

    private final DimVec3 pos;
    public DimVec3 getPos() {
        return this.pos;
    }

    public MultiEntityHitResult(Entity pEntity, Vec3 pLocation, List<Entity> entities, DimVec3 pos) {
        super(pEntity, pLocation);
        this.entities = entities;
        this.pos = pos;
    }
}

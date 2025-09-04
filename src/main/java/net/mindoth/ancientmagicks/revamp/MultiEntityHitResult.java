package net.mindoth.ancientmagicks.revamp;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class MultiEntityHitResult extends EntityHitResult {

    public MultiEntityHitResult(Entity pEntity, Vec3 pLocation) {
        super(pEntity, pLocation);
        List<Entity> tempList = Lists.newArrayList();
        tempList.add(pEntity);
        this.entities = tempList;
    }

    private final List<Entity> entities;

    public MultiEntityHitResult(Entity pEntity, Vec3 pLocation, Level level, AABB box) {
        super(pEntity, pLocation);
        this.entities = level.getEntitiesOfClass(Entity.class, box);
    }

    public List<Entity> getEntities() {
        return this.entities;
    }
}

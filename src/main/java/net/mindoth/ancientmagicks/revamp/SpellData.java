package net.mindoth.ancientmagicks.revamp;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SpellData {

    private List<Entity> entities;
    public List<Entity> getEntities() {
        return this.entities;
    }
    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }
    public void purgeEntities(int amount) {
        for ( int i = 0; i < amount; i++ ) if ( !this.entities.isEmpty() ) {
            int lastIndex = this.entities.size() - 1;
            this.entities.remove(lastIndex);
        }
    }

    private List<BlockHitResult> blocks;
    public List<BlockHitResult> getBlocks() {
        return this.blocks;
    }
    public void addBlock(BlockHitResult block) {
        this.blocks.add(block);
    }
    public void purgeBlocks(int amount) {
        for ( int i = 0; i < amount; i++ ) if ( !this.blocks.isEmpty() ) {
            int lastIndex = this.blocks.size() - 1;
            this.blocks.remove(lastIndex);
        }
    }

    private List<Vec3> vectors;
    public List<Vec3> getVectors() {
        return this.vectors;
    }
    public void addVector(Vec3 vector) {
        this.vectors.add(vector);
    }
    public void purgeVectors(int amount) {
        for ( int i = 0; i < amount; i++ ) if ( !this.vectors.isEmpty() ) {
            int lastIndex = this.vectors.size() - 1;
            this.vectors.remove(lastIndex);
        }
    }

    private List<Level> dimensions;
    public List<Level> getDimensions() {
        return this.dimensions;
    }
    public void addDimension(Level dimension) {
        this.dimensions.add(dimension);
    }
    public void purgeDimensions(int amount) {
        for ( int i = 0; i < amount; i++ ) if ( !this.dimensions.isEmpty() ) {
            int lastIndex = this.dimensions.size() - 1;
            this.dimensions.remove(lastIndex);
        }
    }

    public SpellData() {
        this.entities = Lists.newArrayList();
        this.blocks = Lists.newArrayList();
        this.vectors = Lists.newArrayList();
        this.dimensions = Lists.newArrayList();

        this.valid = true;
    }

    private boolean valid;
    public boolean isValid() {
        return this.valid;
    }
    public void setValid(boolean bool) {
        this.valid = bool;
    }
}

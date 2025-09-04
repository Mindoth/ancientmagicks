package net.mindoth.ancientmagicks.revamp;

import com.google.common.collect.Lists;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SpellData {

    private List<MultiEntityHitResult> entities;
    public List<MultiEntityHitResult> getEntities() {
        return this.entities;
    }
    public void addEntity(MultiEntityHitResult entity) {
        this.entities.add(entity);
    }
    public void purgeEntities(int amount) {
        for ( int i = 0; i < amount; i++ ) if ( !this.entities.isEmpty() ) {
            int lastIndex = this.entities.size() - 1;
            this.entities.remove(lastIndex);
        }
    }
    public MultiEntityHitResult getLatestEntity() {
        return getEntities().get(getEntities().size() - 1);
    }

    private List<MultiBlockHitResult> blocks;
    public List<MultiBlockHitResult> getBlocks() {
        return this.blocks;
    }
    public void addBlock(MultiBlockHitResult block) {
        this.blocks.add(block);
    }
    public void purgeBlocks(int amount) {
        for ( int i = 0; i < amount; i++ ) if ( !this.blocks.isEmpty() ) {
            int lastIndex = this.blocks.size() - 1;
            this.blocks.remove(lastIndex);
        }
    }
    public MultiBlockHitResult getLatestBlock() {
        return getBlocks().get(getBlocks().size() - 1);
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
    public Vec3 getLatestVector() {
        return getVectors().get(getVectors().size() - 1);
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
    public Level getLatestDimension() {
        return getDimensions().get(getDimensions().size() - 1);
    }

    private List<Integer> integers;
    public List<Integer> getIntegers() {
        return this.integers;
    }
    public void addInteger(Integer integer) {
        this.integers.add(integer);
    }
    public void purgeIntegers(int amount) {
        for ( int i = 0; i < amount; i++ ) if ( !this.integers.isEmpty() ) {
            int lastIndex = this.integers.size() - 1;
            this.integers.remove(lastIndex);
        }
    }
    public Integer getLatestInteger() {
        return getIntegers().get(getIntegers().size() - 1);
    }

    public SpellData() {
        this.entities = Lists.newArrayList();
        this.blocks = Lists.newArrayList();
        this.vectors = Lists.newArrayList();
        this.dimensions = Lists.newArrayList();
        this.integers = Lists.newArrayList();

        this.valid = true;
    }

    public static SpellData clone(SpellData spellData) {
        SpellData newData = new SpellData();
        for ( MultiEntityHitResult entity : spellData.getEntities() ) newData.addEntity(entity);
        for ( MultiBlockHitResult block : spellData.getBlocks() ) newData.addBlock(block);
        for ( Vec3 vec3 : spellData.getVectors() ) newData.addVector(vec3);
        for ( Level level : spellData.getDimensions() ) newData.addDimension(level);
        for ( int integer : spellData.getIntegers() ) newData.addInteger(integer);
        return newData;
    }

    private boolean valid;
    public boolean isValid() {
        return this.valid;
    }
    public void setValid(boolean bool) {
        this.valid = bool;
    }
}

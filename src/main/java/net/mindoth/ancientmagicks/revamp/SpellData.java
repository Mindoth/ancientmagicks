package net.mindoth.ancientmagicks.revamp;

import com.google.common.collect.Lists;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SpellData {

    private List<Object> stackList;
    public List<Object> getStackList() {
        return this.stackList;
    }
    public void addObject(Object object) {
        this.stackList.add(object);
    }
    public Object getLatestObject() {
        return this.stackList.get(this.stackList.size() - 1);
    }

    public List<MultiEntityHitResult> getEntities() {
        List<MultiEntityHitResult> entities = Lists.newArrayList();
        for ( Object object : this.stackList ) if ( object instanceof MultiEntityHitResult mEntityHitResult ) entities.add(mEntityHitResult);
        return entities;
    }
    public void purgeEntities(int amount) {
        for ( int i = 0; i < amount; i++ ) {
            for ( int j = this.stackList.size() - 1; j >= 0; j-- ) if ( !getEntities().isEmpty() ) {
                Object object = this.stackList.get(j);
                if ( object instanceof MultiEntityHitResult ) {
                    this.stackList.remove(j);
                    break;
                }
            }
        }
    }
    public MultiEntityHitResult getLatestEntity() {
        return getEntities().get(getEntities().size() - 1);
    }

    public List<MultiBlockHitResult> getBlocks() {
        List<MultiBlockHitResult> blocks = Lists.newArrayList();
        for ( Object object : this.stackList ) if ( object instanceof MultiBlockHitResult mBlockHitResult ) blocks.add(mBlockHitResult);
        return blocks;
    }
    public void purgeBlocks(int amount) {
        for ( int i = 0; i < amount; i++ ) {
            for ( int j = this.stackList.size() - 1; j >= 0; j-- ) if ( !getBlocks().isEmpty() ) {
                Object object = this.stackList.get(j);
                if ( object instanceof MultiBlockHitResult ) {
                    this.stackList.remove(j);
                    break;
                }
            }
        }
    }
    public MultiBlockHitResult getLatestBlock() {
        return getBlocks().get(getBlocks().size() - 1);
    }

    public List<Vec3> getVectors() {
        List<Vec3> vectors = Lists.newArrayList();
        for ( Object object : this.stackList ) if ( object instanceof Vec3 vector ) vectors.add(vector);
        return vectors;
    }
    public void purgeVectors(int amount) {
        for ( int i = 0; i < amount; i++ ) {
            for ( int j = this.stackList.size() - 1; j >= 0; j-- ) if ( !getVectors().isEmpty() ) {
                Object object = this.stackList.get(j);
                if ( object instanceof Vec3 ) {
                    this.stackList.remove(j);
                    break;
                }
            }
        }
    }
    public Vec3 getLatestVector() {
        return getVectors().get(getVectors().size() - 1);
    }

    public List<Level> getDimensions() {
        List<Level> dimensions = Lists.newArrayList();
        for ( Object object : this.stackList ) if ( object instanceof Level dimension ) dimensions.add(dimension);
        return dimensions;
    }
    public void purgeDimensions(int amount) {
        for ( int i = 0; i < amount; i++ ) {
            for ( int j = this.stackList.size() - 1; j >= 0; j-- ) if ( !getDimensions().isEmpty() ) {
                Object object = this.stackList.get(j);
                if ( object instanceof Level ) {
                    this.stackList.remove(j);
                    break;
                }
            }
        }
    }
    public Level getLatestDimension() {
        return getDimensions().get(getDimensions().size() - 1);
    }

    public List<Integer> getIntegers() {
        List<Integer> integers = Lists.newArrayList();
        for ( Object object : this.stackList ) if ( object instanceof Integer integer ) integers.add(integer);
        return integers;
    }
    public void purgeIntegers(int amount) {
        for ( int i = 0; i < amount; i++ ) {
            for ( int j = this.stackList.size() - 1; j >= 0; j-- ) if ( !getIntegers().isEmpty() ) {
                Object object = this.stackList.get(j);
                if ( object instanceof Integer ) {
                    this.stackList.remove(j);
                    break;
                }
            }
        }
    }
    public Integer getLatestInteger() {
        return getIntegers().get(getIntegers().size() - 1);
    }

    public SpellData() {
        this.stackList = Lists.newArrayList();
        this.valid = true;
    }

    public static SpellData clone(SpellData spellData) {
        SpellData newData = new SpellData();
        for ( Object object : spellData.getStackList() ) newData.addObject(object);
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

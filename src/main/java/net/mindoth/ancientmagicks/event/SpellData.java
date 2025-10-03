package net.mindoth.ancientmagicks.event;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.RuneItem;
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
        for ( Object object : this.stackList ) if ( object == null || object instanceof MultiEntityHitResult ) entities.add((MultiEntityHitResult)object);
        return entities;
    }
    public void purgeEntities(int amount) {
        for ( int i = 0; i < amount; i++ ) {
            for ( int j = this.stackList.size() - 1; j >= 0; j-- ) if ( !getEntities().isEmpty() ) {
                Object object = this.stackList.get(j);
                if ( object == null || object instanceof MultiEntityHitResult ) {
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
        for ( Object object : this.stackList ) if ( object == null || object instanceof MultiBlockHitResult ) blocks.add((MultiBlockHitResult)object);
        return blocks;
    }
    public void purgeBlocks(int amount) {
        for ( int i = 0; i < amount; i++ ) {
            for ( int j = this.stackList.size() - 1; j >= 0; j-- ) if ( !getBlocks().isEmpty() ) {
                Object object = this.stackList.get(j);
                if ( object == null || object instanceof MultiBlockHitResult ) {
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
        for ( Object object : this.stackList ) if ( object == null || object instanceof Vec3 ) vectors.add((Vec3)object);
        return vectors;
    }
    public void purgeVectors(int amount) {
        for ( int i = 0; i < amount; i++ ) {
            for ( int j = this.stackList.size() - 1; j >= 0; j-- ) if ( !getVectors().isEmpty() ) {
                Object object = this.stackList.get(j);
                if ( object == null || object instanceof Vec3 ) {
                    this.stackList.remove(j);
                    break;
                }
            }
        }
    }
    public Vec3 getLatestVector() {
        return getVectors().get(getVectors().size() - 1);
    }

    public List<DimVec3> getPositions() {
        List<DimVec3> positions = Lists.newArrayList();
        for ( Object object : this.stackList ) if ( object == null || object instanceof DimVec3 ) positions.add((DimVec3)object);
        return positions;
    }
    public void purgePositions(int amount) {
        for ( int i = 0; i < amount; i++ ) {
            for ( int j = this.stackList.size() - 1; j >= 0; j-- ) if ( !getPositions().isEmpty() ) {
                Object object = this.stackList.get(j);
                if ( object == null || object instanceof DimVec3 ) {
                    this.stackList.remove(j);
                    break;
                }
            }
        }
    }
    public DimVec3 getLatestPosition() {
        return getPositions().get(getPositions().size() - 1);
    }

    public List<Integer> getIntegers() {
        List<Integer> integers = Lists.newArrayList();
        for ( Object object : this.stackList ) if ( object == null || object instanceof Integer ) integers.add((Integer)object);
        return integers;
    }
    public void purgeIntegers(int amount) {
        for ( int i = 0; i < amount; i++ ) {
            for ( int j = this.stackList.size() - 1; j >= 0; j-- ) if ( !getIntegers().isEmpty() ) {
                Object object = this.stackList.get(j);
                if ( object == null || object instanceof Integer ) {
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
        this.runeList = Lists.newArrayList();
        this.dataList = Lists.newArrayList();
    }

    public static SpellData clone(SpellData spellData) {
        SpellData newData = new SpellData();
        for ( Object object : spellData.getStackList() ) newData.addObject(object);
        newData.valid = spellData.isValid();
        newData.runeList = spellData.getRuneList();
        newData.dataList = spellData.getDataList();
        return newData;
    }

    private boolean valid;
    public boolean isValid() {
        return this.valid;
    }
    public void setValid(boolean bool) {
        this.valid = bool;
    }

    private List<RuneItem> runeList;
    public List<RuneItem> getRuneList() {
        return this.runeList;
    }
    public void addRune(RuneItem rune) {
        this.runeList.add(rune);
    }

    private List<String> dataList;
    public List<String> getDataList() {
        return this.dataList;
    }
    public void addData(String string) {
        this.dataList.add(string);
    }
}

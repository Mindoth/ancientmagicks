package net.mindoth.ancientmagicks.network.capabilities.numbnessdamage;

import net.minecraft.nbt.CompoundTag;

public class NumbnessDamage {

    private float damage;
    public float getDamage() {
        return this.damage;
    }
    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void copyFrom(NumbnessDamage source) {
        this.damage = source.damage;
    }

    public void saveNBTData(CompoundTag tag) {
        tag.putFloat("am_numbness_damage", this.damage);
    }

    public void loadNBTData(CompoundTag tag) {
        this.damage = tag.getFloat("am_numbness_damage");
    }
}

package net.mindoth.ancientmagicks.capabilities.playermagic;

import net.minecraft.nbt.CompoundTag;

public class PlayerMagic {

    public static final String AM_MAGIC = "am_magic";
    public static final String AM_CURRENT_MANA = "am_current_mana";

    private double currentMana;
    public double getCurrentMana() {
        return currentMana;
    }
    public void setCurrentMana(double currentMana) {
        this.currentMana = currentMana;
    }

    public void copyFrom(PlayerMagic source) {
        this.currentMana = source.currentMana;
    }

    public void saveNBTData(CompoundTag tag) {
        tag.putDouble(AM_CURRENT_MANA, this.currentMana);
    }

    public void loadNBTData(CompoundTag tag) {
        this.currentMana = tag.getDouble(AM_CURRENT_MANA);
    }
}

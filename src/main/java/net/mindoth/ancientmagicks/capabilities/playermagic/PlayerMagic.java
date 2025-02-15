package net.mindoth.ancientmagicks.capabilities.playermagic;

import net.minecraft.nbt.CompoundTag;

public class PlayerMagic {

    public static final String AM_MAGIC = "am_magic";
    public static final String AM_SPELL = "am_spell";
    public static final String AM_KNOWN_SPELLS = "am_known_spells";
    public static final String AM_CURRENT_MANA = "am_current_mana";

    private String currentSpell;
    public String getCurrentSpell() {
        return this.currentSpell;
    }
    public void setCurrentSpell(String currentSpell) {
        this.currentSpell = currentSpell;
    }

    private String knownSpells;
    public String getKnownSpells() {
        return this.knownSpells;
    }
    public void setKnownSpells(String knownSpells) {
        this.knownSpells = knownSpells;
    }

    private double currentMana;
    public double getCurrentMana() {
        return currentMana;
    }
    public void setCurrentMana(double currentMana) {
        this.currentMana = currentMana;
    }

    public void copyFrom(PlayerMagic source) {
        this.knownSpells = source.knownSpells;
        this.currentSpell = source.currentSpell;
        this.currentMana = source.currentMana;
    }

    public void saveNBTData(CompoundTag tag) {
        tag.putString(AM_SPELL, this.currentSpell);
        tag.putString(AM_KNOWN_SPELLS, this.knownSpells);
        tag.putDouble(AM_CURRENT_MANA, this.currentMana);
    }

    public void loadNBTData(CompoundTag tag) {
        this.currentSpell = tag.getString(AM_SPELL);
        this.knownSpells = tag.getString(AM_KNOWN_SPELLS);
        this.currentMana = tag.getDouble(AM_CURRENT_MANA);
    }
}
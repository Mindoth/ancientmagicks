package net.mindoth.ancientmagicks.util.capabilities.playermagic;

import net.minecraft.nbt.CompoundTag;

public class PlayerMagic {

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
        tag.putString("am_spell", this.currentSpell);
        tag.putString("am_known_spells", this.knownSpells);
        tag.putDouble("am_current_mana", this.currentMana);
    }

    public void loadNBTData(CompoundTag tag) {
        this.currentSpell = tag.getString("am_spell");
        this.knownSpells = tag.getString("am_known_spells");
        this.currentMana = tag.getDouble("am_current_mana");
    }
}

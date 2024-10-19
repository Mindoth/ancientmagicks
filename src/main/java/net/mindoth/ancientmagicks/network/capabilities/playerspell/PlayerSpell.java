package net.mindoth.ancientmagicks.network.capabilities.playerspell;

import net.minecraft.nbt.CompoundTag;

public class PlayerSpell {

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

    private boolean blue_rune;
    public boolean getBlue() {
        return this.blue_rune;
    }
    public void setBlue(boolean blue_rune) {
        this.blue_rune = blue_rune;
    }

    private boolean purple_rune;
    public boolean getPurple() {
        return this.purple_rune;
    }
    public void setPurple(boolean purple_rune) {
        this.purple_rune = purple_rune;
    }

    private boolean yellow_rune;
    public boolean getYellow() {
        return this.yellow_rune;
    }
    public void setYellow(boolean yellow_rune) {
        this.yellow_rune = yellow_rune;
    }

    private boolean green_rune;
    public boolean getGreen() {
        return this.green_rune;
    }
    public void setGreen(boolean green_rune) {
        this.green_rune = green_rune;
    }

    private boolean black_rune;
    public boolean getBlack() {
        return this.black_rune;
    }
    public void setBlack(boolean black_rune) {
        this.black_rune = black_rune;
    }

    private boolean white_rune;
    public boolean getWhite() {
        return this.white_rune;
    }
    public void setWhite(boolean white_rune) {
        this.white_rune = white_rune;
    }

    /*private boolean brown_rune;
    public boolean getBrown() {
        return this.brown_rune;
    }
    public void setBrown(boolean brown_rune) {
        this.brown_rune = brown_rune;
    }

    private boolean red_rune;
    public boolean getRed() {
        return this.red_rune;
    }
    public void setRed(boolean red_rune) {
        this.red_rune = red_rune;
    }*/

    public void copyFrom(PlayerSpell source) {
        this.knownSpells = source.knownSpells;
        this.currentSpell = source.currentSpell;
        this.blue_rune = source.blue_rune;
        this.purple_rune = source.purple_rune;
        this.yellow_rune = source.yellow_rune;
        this.green_rune = source.green_rune;
        this.black_rune = source.black_rune;
        this.white_rune = source.white_rune;
        /*this.brown_rune = source.brown_rune;
        this.red_rune = source.red_rune;*/
    }

    public void saveNBTData(CompoundTag tag) {
        tag.putString("am_spell", this.currentSpell);
        tag.putString("am_known_spells", this.knownSpells);
        tag.putBoolean("am_blue_rune", this.blue_rune);
        tag.putBoolean("am_purple_rune", this.purple_rune);
        tag.putBoolean("am_yellow_rune", this.yellow_rune);
        tag.putBoolean("am_green_rune", this.green_rune);
        tag.putBoolean("am_black_rune", this.black_rune);
        tag.putBoolean("am_white_rune", this.white_rune);
        /*tag.putBoolean("am_brown_rune", this.brown_rune);
        tag.putBoolean("am_red_rune", this.red_rune);*/
    }

    public void loadNBTData(CompoundTag tag) {
        this.currentSpell = tag.getString("am_spell");
        this.knownSpells = tag.getString("am_known_spells");
        this.blue_rune = tag.getBoolean("am_blue_rune");
        this.purple_rune = tag.getBoolean("am_purple_rune");
        this.yellow_rune = tag.getBoolean("am_yellow_rune");
        this.green_rune = tag.getBoolean("am_green_rune");
        this.black_rune = tag.getBoolean("am_black_rune");
        this.white_rune = tag.getBoolean("am_white_rune");
        /*this.brown_rune = tag.getBoolean("am_brown_rune");
        this.red_rune = tag.getBoolean("am_red_rune");*/
    }
}

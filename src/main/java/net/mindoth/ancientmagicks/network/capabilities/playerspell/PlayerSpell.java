package net.mindoth.ancientmagicks.network.capabilities.playerspell;

import net.minecraft.nbt.CompoundTag;

public class PlayerSpell {

    private String spell;
    public String getSpell() {
        return this.spell;
    }
    public void setSpell(String spell) {
        this.spell = spell;
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

    public void copyFrom(PlayerSpell source) {
        this.spell = source.spell;
        this.blue_rune = source.blue_rune;
        this.purple_rune = source.purple_rune;
        this.yellow_rune = source.yellow_rune;
        this.green_rune = source.green_rune;
        this.black_rune = source.black_rune;
        this.white_rune = source.white_rune;
    }

    public void saveNBTData(CompoundTag tag) {
        tag.putString("am_spell", this.spell);
        tag.putBoolean("am_blue_rune", this.blue_rune);
        tag.putBoolean("am_purple_rune", this.purple_rune);
        tag.putBoolean("am_yellow_rune", this.yellow_rune);
        tag.putBoolean("am_green_rune", this.green_rune);
        tag.putBoolean("am_black_rune", this.black_rune);
        tag.putBoolean("am_white_rune", this.white_rune);
    }

    public void loadNBTData(CompoundTag tag) {
        this.spell = tag.getString("am_spell");
        this.blue_rune = tag.getBoolean("am_blue_rune");
        this.purple_rune = tag.getBoolean("am_purple_rune");
        this.yellow_rune = tag.getBoolean("am_yellow_rune");
        this.green_rune = tag.getBoolean("am_green_rune");
        this.black_rune = tag.getBoolean("am_black_rune");
        this.white_rune = tag.getBoolean("am_white_rune");
    }
}

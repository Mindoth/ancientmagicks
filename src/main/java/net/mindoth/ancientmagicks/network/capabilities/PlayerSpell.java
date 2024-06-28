package net.mindoth.ancientmagicks.network.capabilities;

import net.minecraft.nbt.CompoundTag;

public class PlayerSpell {
    private String spell;

    public String getSpell() {
        return this.spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public void copyFrom(PlayerSpell source) {
        this.spell = source.spell;
    }

    public void saveNBTData(CompoundTag tag) {
        tag.putString("am_spell", this.spell);
    }

    public void loadNBTData(CompoundTag tag) {
        this.spell = tag.getString("am_spell");
    }
}

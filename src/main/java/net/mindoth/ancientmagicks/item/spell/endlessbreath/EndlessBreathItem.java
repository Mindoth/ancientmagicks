package net.mindoth.ancientmagicks.item.spell.endlessbreath;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EndlessBreathItem extends SpellItem {

    public EndlessBreathItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public boolean isChannel() {
        return true;
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = caster == owner && owner.getAirSupply() < owner.getMaxAirSupply();

        if ( state ) {
            if ( useTime % 10 == 0 ) {
                owner.setAirSupply(Math.min(owner.getAirSupply() + 39, owner.getMaxAirSupply()));
            }
        }

        return state;
    }
}

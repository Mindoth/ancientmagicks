package net.mindoth.ancientmagicks.item.spell.waterbolt;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellShoot;
import net.mindoth.ancientmagicks.item.spell.abstractspell.ColorCode;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WaterBoltItem extends AbstractSpellShoot {

    public WaterBoltItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    public ParticleColor.IntWrapper getColor() {
        return AbstractSpellEntity.getSpellColor(ColorCode.DARK_BLUE);
    }

    @Override
    protected AbstractSpellEntity getProjectile(Level level, Player owner, Entity caster) {
        return new WaterBoltEntity(level, owner, caster, this);
    }
}

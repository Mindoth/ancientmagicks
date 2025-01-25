package net.mindoth.ancientmagicks.item.spell.summonzombie;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellSummon;
import net.mindoth.ancientmagicks.item.spell.abstractspell.ColorCode;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class SummonZombieItem extends AbstractSpellSummon {

    public SummonZombieItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    public ParticleColor.IntWrapper getColor() {
        return AbstractSpellEntity.getSpellColor(ColorCode.BLACK);
    }

    @Override
    protected Mob getMinion(Level level) {
        return new Zombie(EntityType.ZOMBIE, level);
    }
}

package net.mindoth.ancientmagicks.item.spell.summonzombie;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellMinion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class SummonZombieItem extends AbstractSpellMinion {

    public SummonZombieItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected Mob getMinion(Level level) {
        return new Zombie(EntityType.ZOMBIE, level);
    }
}

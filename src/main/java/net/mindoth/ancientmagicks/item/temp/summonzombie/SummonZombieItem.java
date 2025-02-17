package net.mindoth.ancientmagicks.item.temp.summonzombie;

import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellSummon;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.level.Level;

public class SummonZombieItem extends AbstractSpellSummon {

    public SummonZombieItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    protected Mob getMinion(Level level) {
        return new Husk(EntityType.HUSK, level);
    }
}

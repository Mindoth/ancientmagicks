package net.mindoth.ancientmagicks.item.temp.summonbees;

import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellSummon;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;

public class SummonBeesItem extends AbstractSpellSummon {

    public SummonBeesItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    protected int getAmount() {
        return 2;
    }

    @Override
    protected Mob getMinion(Level level) {
        Bee minion = new Bee(EntityType.BEE, level);
        for ( Goal goal : minion.goalSelector.getAvailableGoals() ) minion.goalSelector.removeGoal(goal);
        //TODO rework so goals stay on world reload
        minion.goalSelector.addGoal(0, new MeleeAttackGoal(minion, 1.4D, true));
        minion.goalSelector.addGoal(1, new FloatGoal(minion));
        return minion;
    }
}

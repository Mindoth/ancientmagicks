package net.mindoth.ancientmagicks.item.spell.summonbees;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellSummon;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellSchool;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;

public class SummonBeesItem extends AbstractSpellSummon {

    public SummonBeesItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    protected int getAmount() {
        return 4;
    }

    @Override
    protected Mob getMinion(Level level) {
        Bee mob = new Bee(EntityType.BEE, level);
        for ( Goal goal : mob.goalSelector.getAvailableGoals() ) mob.goalSelector.removeGoal(goal);
        mob.goalSelector.addGoal(0, new MeleeAttackGoal(mob, 1.4D, true));
        mob.goalSelector.addGoal(1, new FloatGoal(mob));
        return mob;
    }
}

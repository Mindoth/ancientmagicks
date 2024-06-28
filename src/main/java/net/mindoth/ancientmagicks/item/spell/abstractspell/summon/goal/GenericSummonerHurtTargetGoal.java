package net.mindoth.ancientmagicks.item.spell.abstractspell.summon.goal;


import net.mindoth.ancientmagicks.item.spell.abstractspell.summon.SummonedMinion;
import net.mindoth.ancientmagicks.item.spell.abstractspell.summon.SummonerGetter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class GenericSummonerHurtTargetGoal extends TargetGoal {
    private final Mob entity;
    private final SummonerGetter summoner;
    private LivingEntity summonerLastHurt;
    private int timestamp;

    public GenericSummonerHurtTargetGoal(Mob entity, SummonerGetter summonerGetter) {
        super(entity, false);
        this.entity = entity;
        this.summoner = summonerGetter;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        LivingEntity summoner = this.summoner.get();
        if (summoner == null) {
            return false;
        } else {
            //mob.getLastHurtByMobTimestamp() == mob.tickCount - 1
            this.summonerLastHurt = summoner.getLastHurtMob();
            int i = summoner.getLastHurtMobTimestamp();


            return i != this.timestamp && this.canAttack(this.summonerLastHurt, TargetingConditions.DEFAULT) && !(this.summonerLastHurt instanceof SummonedMinion summon && summon.getSummoner() == summoner);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.mob.setTarget(this.summonerLastHurt);

        LivingEntity summoner = this.summoner.get();
        if (summoner != null) {
            this.timestamp = summoner.getLastHurtMobTimestamp();
        }

        super.start();
    }
}

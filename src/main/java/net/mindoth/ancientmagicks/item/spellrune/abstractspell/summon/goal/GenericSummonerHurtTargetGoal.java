package net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.goal;


import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonedMinion;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonerGetter;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

public class GenericSummonerHurtTargetGoal extends TargetGoal {
    private final MobEntity entity;
    private final SummonerGetter summoner;
    private LivingEntity summonerLastHurt;
    private int timestamp;

    public GenericSummonerHurtTargetGoal(MobEntity entity, SummonerGetter summonerGetter) {
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


            return i != this.timestamp && this.canAttack(this.summonerLastHurt, EntityPredicate.DEFAULT) && !(this.summonerLastHurt instanceof SummonedMinion && ((SummonedMinion)this.summonerLastHurt).getSummoner() == summoner);
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

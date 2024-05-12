package net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.goal;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonedMinion;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonerGetter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class GenericSummonerHurtByTargetGoal extends TargetGoal {
    private final Mob entity;
    private final SummonerGetter summoner;
    private LivingEntity summonerLastHurtBy;
    private int timestamp;

    public GenericSummonerHurtByTargetGoal(Mob entity, SummonerGetter getSummoner) {
        super(entity, false);
        this.entity = entity;
        this.summoner = getSummoner;
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
            this.summonerLastHurtBy = summoner.getLastHurtByMob();
            if (summonerLastHurtBy == null || summonerLastHurtBy.isAlliedTo(mob))
                return false;
            int i = summoner.getLastHurtByMobTimestamp();
            return i != this.timestamp && this.canAttack(this.summonerLastHurtBy, TargetingConditions.DEFAULT) && !(this.summonerLastHurtBy instanceof SummonedMinion summon && summon.getSummoner() == summoner);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.mob.setTarget(this.summonerLastHurtBy);
        this.mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, this.summonerLastHurtBy, 200L);
        LivingEntity summoner = this.summoner.get();
        if (summoner != null) {
            this.timestamp = summoner.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}

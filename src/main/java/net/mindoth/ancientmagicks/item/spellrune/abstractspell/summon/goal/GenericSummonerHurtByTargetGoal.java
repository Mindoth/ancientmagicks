package net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.goal;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonedMinion;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonerGetter;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

public class GenericSummonerHurtByTargetGoal extends TargetGoal {
    private final MobEntity entity;
    private final SummonerGetter summoner;
    private LivingEntity summonerLastHurtBy;
    private int timestamp;

    public GenericSummonerHurtByTargetGoal(MobEntity entity, SummonerGetter getSummoner) {
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
            return i != this.timestamp && this.canAttack(this.summonerLastHurtBy, EntityPredicate.DEFAULT) && !(this.summonerLastHurtBy instanceof SummonedMinion && ((SummonedMinion)this.summonerLastHurtBy).getSummoner() == summoner);
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

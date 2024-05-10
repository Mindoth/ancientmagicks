package net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.goal;


import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonedMinion;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonerGetter;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.TargetGoal;

public class GenericCopySummonerTargetGoal extends TargetGoal {
    private final SummonerGetter summonerGetter;

    public GenericCopySummonerTargetGoal(CreatureEntity pMob, SummonerGetter summonerGetter) {
        super(pMob, false);
        this.summonerGetter = summonerGetter;

    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        return summonerGetter.get() instanceof MobEntity
                && ((MobEntity)summonerGetter.get()).getTarget() != null
                && !(((MobEntity)summonerGetter.get()).getTarget() instanceof SummonedMinion
                && ((SummonedMinion)((MobEntity)summonerGetter.get()).getTarget()).getSummoner() == summonerGetter.get());
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        LivingEntity target = ((MobEntity)summonerGetter.get()).getTarget();
        mob.setTarget(target);
        this.mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, target, 200L);

        super.start();
    }
}

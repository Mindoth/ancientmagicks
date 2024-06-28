package net.mindoth.ancientmagicks.item.spell.abstractspell.summon.goal;


import net.mindoth.ancientmagicks.item.spell.abstractspell.summon.SummonedMinion;
import net.mindoth.ancientmagicks.item.spell.abstractspell.summon.SummonerGetter;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class GenericCopySummonerTargetGoal extends TargetGoal {
    private final SummonerGetter summonerGetter;

    public GenericCopySummonerTargetGoal(PathfinderMob pMob, SummonerGetter summonerGetter) {
        super(pMob, false);
        this.summonerGetter = summonerGetter;

    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        return summonerGetter.get() instanceof Mob summoner
                && summoner.getTarget() != null
                && !(summoner.getTarget() instanceof SummonedMinion summon
                && summon.getSummoner() == summoner);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        var target = ((Mob) summonerGetter.get()).getTarget();
        mob.setTarget(target);
        this.mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, target, 200L);

        super.start();
    }
}

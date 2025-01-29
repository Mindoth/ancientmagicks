package net.mindoth.ancientmagicks.item.spell.polymorph;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PolymorphItem extends AbstractSpellRayCast {

    public PolymorphItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, Entity target) {
        return level instanceof ServerLevel && target instanceof Mob && !(target instanceof Sheep);
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, Entity target) {
        Sheep sheep = ((Mob)target).convertTo(EntityType.SHEEP, false);
        sheep.finalizeSpawn((ServerLevel)level, ((ServerLevel)level).getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
        addEnchantParticles(sheep, getColor().r, getColor().g, getColor().b, 0.15F, 8, hasMask());
    }
}

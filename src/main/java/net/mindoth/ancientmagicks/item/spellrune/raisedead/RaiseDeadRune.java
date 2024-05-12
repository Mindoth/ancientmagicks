package net.mindoth.ancientmagicks.item.spellrune.raisedead;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;

public class RaiseDeadRune extends SpellRuneItem {

    public RaiseDeadRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void shootMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
        Level level = caster.level();
        playMagicSummonSound(level, center);
        HashMap<String, Float> valueMap = new HashMap<>();
        Mob minion = new SkeletonMinionEntity(level, owner);

        valueMap.put("life", 0.0F);
        valueMap.put("blockPierce", 0.0F);
        for ( ModifierRuneItem rune : modifierList ) rune.addModifiersToValues(valueMap);

        float life = 600.0F;
        if ( valueMap.get("life") != 0 ) life += ((valueMap.get("life") * 160));
        float range = (float)caster.getEyePosition(1.0F).distanceTo(center);

        minion.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(4.0F * 1);
        minion.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(4.0F * 5);
        minion.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.25F);
        minion.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(6.0F * 2);
        for ( ModifierRuneItem rune : modifierList ) rune.addModifiersToMinionEntity(minion);
        minion.addEffect(new MobEffectInstance(AncientMagicksEffects.SKELETON_TIMER.get(), (int)life, 0, false, false, true));

        minion.moveTo(ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, true, true, valueMap.get("blockPierce") == 0));
        minion.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(minion.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(minion);
        minion.spawnAnim();
    }
}

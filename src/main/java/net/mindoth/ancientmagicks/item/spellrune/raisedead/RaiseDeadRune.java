package net.mindoth.ancientmagicks.item.spellrune.raisedead;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.List;

public class RaiseDeadRune extends SpellRuneItem {

    public RaiseDeadRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void shootMagic(PlayerEntity owner, Entity caster, Vector3d center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
        World level = caster.level;
        playMagicSummonSound(level, center);
        HashMap<String, Float> valueMap = new HashMap<>();
        MobEntity minion = new SkeletonMinionEntity(level, owner);

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
        minion.addEffect(new EffectInstance(AncientMagicksEffects.SKELETON_TIMER.get(), (int)life, 0, false, false, true));

        minion.moveTo(ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, true, true, valueMap.get("blockPierce") == 0));
        minion.finalizeSpawn((ServerWorld)level, level.getCurrentDifficultyAt(minion.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
        level.addFreshEntity(minion);
        minion.spawnAnim();
    }
}

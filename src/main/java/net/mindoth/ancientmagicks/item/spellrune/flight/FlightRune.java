package net.mindoth.ancientmagicks.item.spellrune.flight;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;

public class FlightRune extends SpellRuneItem {

    public FlightRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void shootMagic(PlayerEntity owner, Entity caster, Vector3d center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
        World level = caster.level;
        Vector3d casterPos = caster.getEyePosition(1.0F);
        playMagicSound(level, casterPos);
        HashMap<String, Float> valueMap = new HashMap<>();

        int life = 200;
        valueMap.put("life", 0.0F);
        valueMap.put("size", 1.0F);
        for ( ModifierRuneItem rune : modifierList ) rune.addModifiersToValues(valueMap);
        if ( valueMap.get("life") > 0 ) life *= valueMap.get("life");
        else if ( valueMap.get("life") < 0 ) life /= (-1 * valueMap.get("life") + 1);
        if ( valueMap.get("size") <= 0 ) valueMap.put("size", 1.0F);

        LivingEntity target;
        if ( caster == owner ) {
            if ( (float)casterPos.distanceTo(center) != 0 ) {
                target = (LivingEntity)ShadowEvents.getPointedEntity(level, caster, (float)casterPos.distanceTo(center), 1, caster == owner);
            }
            else target = owner;
        }
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, valueMap.get("size"), null);

        if ( target instanceof PlayerEntity ) {
            target.addEffect(new EffectInstance(AncientMagicksEffects.FLIGHT.get(), life, 0, false, false));
        }
        else {
            target.addEffect(new EffectInstance(Effects.LEVITATION, life, 0, false, false));
        }
    }
}

package net.mindoth.ancientmagicks.item.spellrune.dynamite;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DynamiteRune extends SpellRuneItem {

    public DynamiteRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void shootMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
        Level level = caster.level();
        playMagicSummonSound(level, center);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        HashMap<String, Float> valueMap = new HashMap<>();
        Vec3 point = ShadowEvents.getPoint(level, caster, (float)caster.getEyePosition(1.0F).distanceTo(center),
                0, caster == owner, false, true, true);
        DynamiteEntity tnt = new DynamiteEntity(level, point.x, point.y, point.z, owner);

        valueMap.put("life", 0.0F);
        valueMap.put("power", 0.0F);
        valueMap.put("speed", 0.0F);
        valueMap.put("size", 0.0F);
        //valueMap.put("blockPierce", 0.0F);
        valueMap.put("homing", 0.0F);
        for ( ModifierRuneItem rune : modifierList ) rune.addModifiersToValues(valueMap);

        if ( valueMap.get("life") != 0 ) tnt.life += (valueMap.get("life") * 20);
        if ( valueMap.get("power") != 0 ) tnt.power += valueMap.get("power");
        if ( valueMap.get("speed") != 0 ) tnt.speed += (valueMap.get("speed") * 0.1F);
        if ( valueMap.get("size") != 0 ) tnt.size += valueMap.get("size");
        //tnt.blockPierce = valueMap.get("blockPierce");
        if ( valueMap.get("homing") == 1.0F ) tnt.homing = true;
        tnt.setFuse(tnt.life);
        tnt.speed = Math.max(0, tnt.speed);

        level.addFreshEntity(tnt);
    }
}

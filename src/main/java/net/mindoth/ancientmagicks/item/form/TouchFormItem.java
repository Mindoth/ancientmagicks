package net.mindoth.ancientmagicks.item.form;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;

public class TouchFormItem extends SpellFormItem {

    public TouchFormItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean formSpell(LivingEntity owner, Entity caster, List<ComponentItem> spellStack, List<String> data) {
        List<ComponentItem> newList = Lists.newArrayList();
        List<String> newData = Lists.newArrayList();
        List<SpellModifierItem> formModifiers = Lists.newArrayList();
        boolean form = false;
        for ( int i = 0; i < spellStack.size(); i++ ) {
            ComponentItem item = spellStack.get(i);
            if ( !form ) {
                if ( item instanceof SpellFormItem ) form = true;
                if ( item instanceof SpellModifierItem modifier ) formModifiers.add(modifier);
            }
            else {
                newList.add(item);
                newData.add(data.get(i));
            }
        }
        HashMap<String, Float> formStats = ComponentItem.createDefaultStats();
        Level defLevel = caster.level();
        for ( SpellModifierItem modifier : formModifiers ) modifier.addStatsToMap(formStats);
        float range = formStats.get(REACH);
        Vec3 defPosVec = getTouchPos(defLevel, caster, range);
        for ( int i = 0; i < formModifiers.size(); i++ ) {
            SpellModifierItem modifier = formModifiers.get(i);
            SpellModifierItem.EncodeableData ed = modifier.addDataFromEncodeable(data.get(i), defLevel, defPosVec);
            defLevel = ed.level;
            defPosVec = ed.posVec;
        }
        List<Boolean> boolist = Lists.newArrayList();
        HashMap<String, Float> stats = ComponentItem.createDefaultStats();
        List<SpellModifierItem> modifiers = Lists.newArrayList();
        List<String> modifierData = Lists.newArrayList();
        for ( int i = 0; i < newList.size(); i++ ) {
            ComponentItem item = newList.get(i);
            if ( item instanceof SpellModifierItem modifier ) {
                modifiers.add(modifier);
                modifierData.add(newData.get(i));
            }
            else if ( item instanceof SpellEffectItem effect ) {
                Level level = defLevel;
                Vec3 posVec = defPosVec;
                for ( int j = 0; j < modifiers.size(); j++ ) {
                    SpellModifierItem modifier = modifiers.get(j);
                    modifier.addStatsToMap(stats);
                    if ( modifier.isEncodeable() ) {
                        SpellModifierItem.EncodeableData ed = modifier.addDataFromEncodeable(modifierData.get(j), level, posVec);
                        level = ed.level;
                        posVec = ed.posVec;
                    }
                }
                HitResult hitResult = createHitResult(level, posVec, caster, range);

                boolist.add(effect.castSpell(level, owner, caster, hitResult, formStats.get(AOE), stats, newData.get(i)));
                stats = ComponentItem.createDefaultStats();
                modifiers = Lists.newArrayList();
                modifierData = Lists.newArrayList();
            }
        }
        for ( boolean bool : boolist ) if ( bool ) return true;
        return false;
    }

    public Vec3 getTouchPos(Level level, Entity caster, float range) {
        Vec3 state;
        Entity target = ShadowEvents.getPointedEntity(level, caster, range, 0.0F, true, null);
        if ( target == caster ) {
            BlockPos bPos = getCasterPOVHitResult(level, caster, ClipContext.Fluid.SOURCE_ONLY, range).getBlockPos();
            state = new Vec3(bPos.getX(), bPos.getY(), bPos.getZ());
        }
        else state = ShadowEvents.getPoint(level, caster, range, 0.0F, false, true, true, false);
        return state;
    }

    protected HitResult createHitResult(Level level, Vec3 posVec, Entity caster, float range) {
        HitResult hitResult;
        Entity target = ShadowEvents.getPointedEntity(level, caster, range, 0.0F, true, null);
        if ( target == caster ) {
            BlockHitResult temp = getCasterPOVHitResult(level, caster, ClipContext.Fluid.SOURCE_ONLY, range);
            hitResult = new BlockHitResult(posVec, temp.getDirection(), new BlockPos(Mth.floor(posVec.x), Mth.floor(posVec.y), Mth.floor(posVec.z)), temp.isInside());
        }
        else hitResult = new EntityHitResult(target, posVec);

        return hitResult;
    }

    public static BlockHitResult getCasterPOVHitResult(Level pLevel, Entity caster, ClipContext.Fluid pFluidMode, float range) {
        float f = caster.getXRot();
        float f1 = caster.getYRot();
        Vec3 vec3 = caster.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = range;
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, pFluidMode, caster));
    }
}

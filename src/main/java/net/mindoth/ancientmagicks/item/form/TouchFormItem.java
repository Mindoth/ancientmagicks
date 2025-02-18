package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.mindoth.ancientmagicks.item.spell.BlockTargetSpell;
import net.mindoth.ancientmagicks.item.spell.EntityTargetSpell;
import net.mindoth.shadowizardlib.event.ShadowEvents;
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
    public boolean formSpell(SpellItem spell, LivingEntity owner, Entity caster, List<SpellModifierItem> modifiers) {
        boolean state = false;
        Level level = caster.level();
        HashMap<String, Float> stats = SpellItem.createSpellStats(modifiers);
        float range = 4.5F;

        HitResult hitResult;
        Entity target = ShadowEvents.getPointedEntity(level, caster, range, 0.0F, true, null);
        Vec3 point = ShadowEvents.getPoint(level, caster, range, 0.0F, false, true, true, false);
        if ( target == caster ) hitResult = getCasterPOVHitResult(level, caster, ClipContext.Fluid.SOURCE_ONLY, range);
        else hitResult = new EntityHitResult(target, point);
        state = true;

        if ( state ) spell.castSpell(level, owner, caster, hitResult, stats);

        return state;
    }

    protected static BlockHitResult getCasterPOVHitResult(Level pLevel, Entity caster, ClipContext.Fluid pFluidMode, float range) {
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

package net.mindoth.ancientmagicks.item.spell.witcharmor;

import net.mindoth.ancientmagicks.item.armor.AncientMagicsArmorMaterials;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WitchArmorItem extends AbstractSpellRayCast {

    public WitchArmorItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    protected int getLife() {
        return 72000;
    }

    @Override
    protected float getRange() {
        return 7.0F;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, LivingEntity target) {
        return isAlly(owner, target) && !hasHeavyArmor(target);
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        target.addEffect(new MobEffectInstance(AncientMagicksEffects.WITCH_ARMOR.get(), getLife(), 0, false, false));
    }

    public static boolean hasHeavyArmor(LivingEntity living) {
        for ( ItemStack slot : living.getArmorSlots() ) {
            if ( slot.getItem() instanceof ArmorItem armor
                    && armor.getMaterial() != ArmorMaterials.LEATHER
                    && armor.getMaterial() != AncientMagicsArmorMaterials.CLOTH ) return true;
        }
        return false;
    }
}

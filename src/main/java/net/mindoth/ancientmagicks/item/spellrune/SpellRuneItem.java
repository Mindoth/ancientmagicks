package net.mindoth.ancientmagicks.item.spellrune;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.List;

public class SpellRuneItem extends RuneItem {

    public static List<SpellRuneItem> SPELL_RUNES = Lists.newArrayList();
    public static void init() {
        for ( Item item : AncientMagicks.ITEM_LIST ) if ( item instanceof SpellRuneItem ) SPELL_RUNES.add((SpellRuneItem)item);
    }

    public SpellRuneItem(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    public static boolean isAlly(LivingEntity owner, LivingEntity target) {
        if ( target instanceof Player && !AncientMagicksCommonConfig.PVP.get() ) return true;
        else return target == owner || !target.canAttack(owner) || target.isAlliedTo(owner) || (target instanceof TamableAnimal && ((TamableAnimal)target).isOwnedBy(owner));
    }

    public static boolean isPushable(Entity entity) {
        return ( entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof PrimedTnt || entity instanceof FallingBlockEntity );
    }
}

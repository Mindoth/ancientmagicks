package net.mindoth.ancientmagicks.revamp.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.registries.ModItems;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

public class RuneItem extends Item {
    public RuneItem(Properties pProperties) {
        super(pProperties);
    }

    //ONLY FOR TESTING
    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(hand));
        if ( !level.isClientSide ) {
            List<Item> runeList = Lists.newArrayList();

            runeList.add(ModItems.SELF_RUNE_ITEM.get());
            runeList.add(ModItems.TARGET_BLOCK_RUNE_ITEM.get());
            runeList.add(ModItems.MINE_RUNE_ITEM.get());

            resolveStack(player, runeList);
        }
        return result;
    }

    public static void resolveStack(Entity caster, List<Item> runeList) {
        SpellData spellData = new SpellData();
        for ( Item item : runeList ) if ( item instanceof RuneItem rune ) {
            spellData = rune.resolve(caster, spellData);
            if ( !spellData.isValid() ) {
                if ( caster instanceof Player player ) player.displayClientMessage(Component.literal("FAILED SPELL"), false);
                break;
            }
        }
    }

    public SpellData resolve(Entity caster, SpellData spellData) {
        return spellData;
    }
}

package net.mindoth.ancientmagicks.item.rune;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.event.MultiEntityHitResult;
import net.mindoth.ancientmagicks.event.SpellData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class EntityFilterItemRuneItem extends UseOnEntityTemplate {
    public EntityFilterItemRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.entity").append(Component.literal(", "))
                .append(Component.translatable("tooltip.ancientmagicks.boolean"))
                .append(Component.literal(" -> ")).append(Component.translatable("tooltip.ancientmagicks.entity")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    protected SpellData result(Entity caster, SpellData spellData, MultiEntityHitResult result, Entity entity) {
        if ( !spellData.getBooleans().isEmpty() ) {
            if ( spellData.getLatestBoolean() != null ) {
                boolean bool = spellData.getLatestBoolean();
                spellData.purgeBooleans(1);
                List<Entity> entities = Lists.newArrayList();
                for ( Entity entry : result.getEntities() ) {
                    if ( bool ) {
                        if ( entry instanceof ItemEntity ) entities.add(entry);
                    }
                    else {
                        if ( !(entry instanceof ItemEntity) ) entities.add(entry);
                    }
                }
                MultiEntityHitResult newResult = new MultiEntityHitResult(result.getEntity(), result.getLocation(), entities, result.getPos());
                spellData.addObject(newResult);
            }
            else spellData.purgeBooleans(1);
        }
        else spellData.setValid(false);
        return spellData;
    }
}

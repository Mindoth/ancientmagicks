package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.CastingValidator;
import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.entity.ProjectileSpellEntity;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ProjectileRuneItem extends RuneItem {
    public ProjectileRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.vector").append(Component.literal(" | "))
                .append(Component.translatable("tooltip.ancientmagicks.vector")).append(Component.literal(" | "))
                .append(Component.literal("(")).append(Component.translatable("tooltip.ancientmagicks.dimension")).append(Component.literal(")"))
                .append(Component.literal(" -> ")).append(Component.translatable("tooltip.ancientmagicks.entity")).append(Component.literal(" / "))
                .append(Component.translatable("tooltip.ancientmagicks.block")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getVectors().isEmpty() && spellData.getVectors().size() >= 2 ) {
            Vec3 position = spellData.getLatestVector();
            spellData.purgeVectors(1);
            Vec3 direction = spellData.getLatestVector();
            spellData.purgeVectors(1);
            Level level;
            if ( spellData.getDimensions().isEmpty() || spellData.getLatestDimension() == null ) level = caster.level();
            else level = spellData.getLatestDimension();
            spellData.purgeDimensions(1);

            ProjectileSpellEntity projectile = new ProjectileSpellEntity(level, caster);
            projectile.ignoredEntities.put(caster.getId(), projectile.tickCount);
            projectile.setNoGravity(true);
            projectile.getEntityData().set(AbstractSpellEntity.SPELLSTACK, CastingValidator.getStringFromSpellStack(spellData.getRuneList()));
            projectile.getEntityData().set(AbstractSpellEntity.DATA, CastingValidator.getStringFromDataList(spellData.getDataList()));
            projectile.spellData = spellData;
            projectile.setPos(position);
            projectile.shoot(direction.x, direction.y, direction.z, projectile.getSpeed(), 0.0F);
            level.addFreshEntity(projectile);
        }
        else spellData.setValid(false);
        return spellData;
    }
}

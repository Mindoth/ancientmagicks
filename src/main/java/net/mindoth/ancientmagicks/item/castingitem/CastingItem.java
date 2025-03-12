package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.event.MagickEvents;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.mindoth.ancientmagicks.item.CastingValidator;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CastingItem extends Item {

    public CastingItem(Properties pProperties) {
        super(pProperties);
    }

    public static void doSpell(LivingEntity owner, Entity caster, @Nullable ItemStack stack, ItemStack scroll) {
        //Handling for players
        if ( caster instanceof ServerPlayer serverPlayer ) {
            serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                List<ComponentItem> componentList = CastingValidator.getSpellStackFromScroll(scroll);
                int manaCost = 0;
                int coolDown = 0;
                for ( ComponentItem item : componentList ) {
                    manaCost += item.getManaCost();
                    coolDown += item.getCooldown();
                }
                if ( CastingValidator.calculateSpellRecipes(scroll, owner, caster) ) {
                    handleCooldownsAndStuff(caster, stack, Math.max(1, 10 + coolDown));
                    if ( !serverPlayer.isCreative() ) MagickEvents.changeMana(caster, -manaCost);
                }
                else whiffSpell(caster);
            });
        }
        //If caster is not a player do the spell anyway
        else CastingValidator.calculateSpellRecipes(scroll, owner, caster);
    }

    private static void handleCooldownsAndStuff(Entity caster, @Nullable ItemStack castingItem, int cooldown) {
        for ( Item item : ForgeRegistries.ITEMS.getValues() ) if ( item instanceof StaffItem ) addCastingCooldown(caster, item, cooldown);
        if ( caster instanceof LivingEntity living ) {
            if ( castingItem != null && castingItem.getItem() instanceof StaffItem ) addItemDamage(castingItem, 1, living);
            if ( cooldown > 5 ) living.stopUsingItem();
        }
    }

    public static void addItemDamage(ItemStack castingItem, int amount, LivingEntity living) {
        castingItem.hurtAndBreak(amount, living, (holder) -> holder.broadcastBreakEvent(living.getUsedItemHand()));
    }

    public static void addCastingCooldown(Entity entity, Item item, int cooldown) {
        if ( entity instanceof Player player ) player.getCooldowns().addCooldown(item, cooldown);
    }

    public static void whiffSpell(Entity caster) {
        SpellEffectItem.playWhiffSound(caster);
        if ( caster instanceof LivingEntity living ) {
            living.stopUsingItem();
            for ( Item item : ForgeRegistries.ITEMS.getValues() ) if ( item instanceof StaffItem ) addCastingCooldown(caster, item, 20);
        }
    }

    public static @Nonnull ItemStack getHeldStaff(LivingEntity playerEntity) {
        ItemStack staff = playerEntity.getMainHandItem().getItem() instanceof StaffItem ? playerEntity.getMainHandItem() : ItemStack.EMPTY;
        if ( staff == ItemStack.EMPTY ) staff = playerEntity.getOffhandItem().getItem() instanceof StaffItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY;
        return staff;
    }

    public static @Nonnull ItemStack getHeldCastingItem(LivingEntity playerEntity) {
        ItemStack staff = isValidCastingItem(playerEntity.getMainHandItem()) ? playerEntity.getMainHandItem() : null;
        return staff == null ? (isValidCastingItem(playerEntity.getOffhandItem()) ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : staff;
    }

    public static boolean isValidCastingItem(ItemStack castingItem) {
        return castingItem.getItem() instanceof StaffItem;
    }

    public static boolean canOpenWheel(Player player) {
        return !CastingItem.getHeldStaff(player).isEmpty() && !SpellBookItem.getSpellBookSlot(player).isEmpty();
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}

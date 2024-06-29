package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.network.capabilities.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class CastingItem extends Item {

    public CastingItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack staff = player.getItemInHand(handIn);
            if ( isValidCastingItem(staff) ) {
                player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spell.getSpell()));
                    if ( item instanceof TabletItem tabletItem && !player.isUsingItem() && !player.getCooldowns().isOnCooldown(tabletItem) ) {
                        if ( player.totalExperience >= tabletItem.tier || player.isCreative() ) player.startUsingItem(handIn);
                        else {
                            addCastingCooldown(player, tabletItem, 20);
                            RuneItem.playWhiffSound(level, ShadowEvents.getEntityCenter(player));
                        }
                    }
                });
            }
        }
        return result;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack wand, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player ) {
            if ( isValidCastingItem(wand) ) {
                player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spell.getSpell()));
                    if ( item instanceof TabletItem tabletItem ) doSpell(player, player, wand, tabletItem, getUseDuration(wand) - timeLeft, player.isCreative());
                });
            }
        }
    }

    public static void doSpell(Player owner, Entity caster, ItemStack wand, TabletItem spell, int useTime, boolean isFree) {
        float xRot = caster.getXRot();
        float yRot = caster.getYRot();
        Vec3 center;
        float distance = 0.5F;

        if ( distance > 0 ) {

            //Adjusters are there to flip rotation if the caster is not a LivingEntity. Don't ask why this works like this...
            int adjuster = 1;
            if ( caster != owner ) adjuster = -1;
            Vec3 direction = ShadowEvents.calculateViewVector(xRot * adjuster, yRot * adjuster).normalize();
            direction = direction.multiply(distance, distance, distance);
            center = caster.getEyePosition().add(direction);
        }
        else center = caster.getEyePosition();

        if ( caster == owner ) {
            //Check if player is creating tablets or not
            if ( (owner.getMainHandItem().getItem() == AncientMagicksItems.STONE_SLATE.get() || owner.getOffhandItem().getItem() == AncientMagicksItems.STONE_SLATE.get()) ) {
                ItemStack tabletStack;
                if ( owner.getMainHandItem().getItem() == AncientMagicksItems.STONE_SLATE.get() ) tabletStack = owner.getMainHandItem();
                else tabletStack = owner.getOffhandItem();
                int stackCount = 0;
                for ( int i = 0; i < tabletStack.getCount(); i++ ) {
                    if ( stackCount >= tabletStack.getCount() ) break;
                    boolean canAfford = false;
                    if ( isFree ) canAfford = true;
                    else if ( owner.totalExperience >= spell.tier ) {
                        canAfford = true;
                        owner.giveExperiencePoints(-spell.tier);
                    }
                    if ( canAfford ) stackCount += 1;
                }
                tabletStack.shrink(stackCount);
                ItemStack dropStack = new ItemStack(spell, stackCount);
                ItemEntity drop = new ItemEntity(owner.level(), ShadowEvents.getEntityCenter(owner).x, ShadowEvents.getEntityCenter(owner).y, ShadowEvents.getEntityCenter(owner).z, dropStack);
                drop.setDeltaMovement(0, 0, 0);
                drop.setNoPickUpDelay();
                caster.level().addFreshEntity(drop);
                addCastingCooldown(owner, spell, 20);
            }
            else {
                //This actually casts the given Spell
                if ( spell.castMagic(owner, caster, center, xRot, yRot, useTime) ) {
                    if ( wand.getItem() instanceof TabletItem && !owner.isCreative() ) wand.shrink(1);
                    //These are cost, cooldown and channelling related handling
                    addCastingCooldown(owner, spell, spell.tier * 20);
                    if ( !isFree ) owner.giveExperiencePoints(-spell.tier);
                }
                else {
                    addCastingCooldown(owner, spell, 20);
                    RuneItem.playWhiffSound(caster.level(), center);
                }
            }
        }
        owner.stopUsingItem();
    }

    public static void addCastingCooldown(Player player, TabletItem spell, int cooldown) {
        player.getCooldowns().addCooldown(spell, cooldown);
    }

    public static @Nonnull ItemStack getHeldCastingItem(Player playerEntity) {
        ItemStack staff = playerEntity.getMainHandItem().getItem() instanceof CastingItem ? playerEntity.getMainHandItem() : null;
        return staff == null ? (playerEntity.getOffhandItem().getItem() instanceof CastingItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : staff;
    }

    public static boolean isValidCastingItem(ItemStack castingItem) {
        return castingItem.getItem() instanceof CastingItem;
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
}

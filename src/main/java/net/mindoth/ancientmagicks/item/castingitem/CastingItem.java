package net.mindoth.ancientmagicks.item.castingitem;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.mindoth.ancientmagicks.client.gui.inventory.WandManager;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.AncientMagicksGroup;
import net.mindoth.ancientmagicks.item.modifierrune.AbridgeRune;
import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.modifierrune.ReachRune;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CastingItem extends Item {
    public WandType tier;
    public int cooldown;
    protected static List<CastingItem> CASTING_ITEMS = Lists.newArrayList();

    public static void init() {
        for ( Item item : AncientMagicks.ITEM_LIST ) if ( item instanceof CastingItem ) CASTING_ITEMS.add((CastingItem)item);
    }

    public CastingItem(WandType tier, int cooldown) {
        super(new Item.Properties().tab(AncientMagicksGroup.RUNIC_ITEMS_TAB).stacksTo(1));
        this.tier = tier;
        this.cooldown = cooldown;
    }

    @Override
    public void onUseTick(World level, LivingEntity living, ItemStack wand, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity)living;
            List<ItemStack> wandList = getWandList(wand);
            if ( timeLeft % 2 == 0 ) doSpell(player, player, wand, wandList, getUseDuration(wand) - timeLeft);
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> use(World level, PlayerEntity player, @Nonnull Hand handIn) {
        ActionResult<ItemStack> result = ActionResult.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack wand = player.getItemInHand(handIn);
            if ( isValidCastingItem(wand) ) {
                player.startUsingItem(handIn);
            }
        }
        return result;
    }

    public static void doSpell(PlayerEntity owner, Entity caster, ItemStack wand, List<ItemStack> wandList, int useTime) {
        if ( caster == owner && owner.getCooldowns().isOnCooldown(wand.getItem()) && !owner.isUsingItem() ) return;
        boolean hasTablet = false;
        ItemStack tabletItem = null;

        //Checking if player has EMPTY tablet in either hand
        if ( caster == owner && !(wand.getItem() instanceof SpellTabletItem)
                && (owner.getItemBySlot(EquipmentSlotType.OFFHAND).getItem() instanceof SpellTabletItem
                || owner.getItemBySlot(EquipmentSlotType.MAINHAND).getItem() instanceof SpellTabletItem) ) {
            if ( owner.getItemBySlot(EquipmentSlotType.OFFHAND).getItem() instanceof SpellTabletItem ) {
                tabletItem = owner.getItemBySlot(EquipmentSlotType.OFFHAND);
            }
            else tabletItem = owner.getItemBySlot(EquipmentSlotType.MAINHAND);
            if ( !CastingItem.isValidCastingItem(tabletItem) ) hasTablet = true;
        }

        //If not then casting normally
        if ( !hasTablet ) {
            int spellRunesToCast = 1;
            List<ModifierRuneItem> modifierList = Lists.newArrayList();
            int spellCooldown = ((CastingItem)wand.getItem()).cooldown;
            for ( ItemStack itemStack : wandList ) {
                if ( spellRunesToCast < 1 ) break;
                if ( itemStack.getItem() instanceof SpellRuneItem ) spellRunesToCast -= 1;
                if ( itemStack.getItem() instanceof SpellRuneItem ) {
                    SpellRuneItem rune = (SpellRuneItem)itemStack.getItem();
                    spellCooldown += rune.cooldown;
                    float xRot = caster.xRot;
                    float yRot = caster.yRot;
                    Vector3d center;
                    float distance = 0;
                    for ( ModifierRuneItem distanceRune : modifierList ) {
                        if ( distanceRune instanceof ReachRune ) distance += 3.5F;
                        if ( distanceRune instanceof AbridgeRune ) distance -= 3.5F;
                    }
                    distance = Math.max(0, distance);
                    if ( distance > 0 ) {

                        //Adjusters are there to flip rotation if the caster is not a LivingEntity, don't ask why this works like this...
                        int adjuster = 1;
                        if ( caster != owner ) adjuster = -1;
                        Vector3d direction = ShadowEvents.calculateViewVector(xRot * adjuster, yRot * adjuster).normalize();
                        direction = direction.multiply(distance, distance, distance);
                        center = caster.getEyePosition(1).add(direction);
                    }
                    else center = caster.getEyePosition(1.0F);

                    //This actually casts the given Spell Rune
                    rune.shootMagic(owner, caster, center, xRot, yRot, useTime, modifierList);

                    //These are cooldown and channeling related handling
                    if ( caster == owner ) addCastingCooldown(owner, spellCooldown);
                    modifierList.clear();
                    if ( spellCooldown > 2 ) owner.stopUsingItem();
                }
                else if ( itemStack.getItem() instanceof ModifierRuneItem ) {
                    ModifierRuneItem rune = (ModifierRuneItem)itemStack.getItem();
                    modifierList.add(rune);
                    spellCooldown += rune.cooldown;
                }
            }
        }
        else {
            //If player has EMPTY tablet in hand, populate its slots
            if ( !wandList.isEmpty() ) {
                ItemStack spellTablet = new ItemStack(tabletItem.getItem());
                if ( !owner.isCreative() ) tabletItem.shrink(1);
                WandData data = CastingItem.getData(spellTablet);
                if ( CastingItem.isValidCastingItem(spellTablet) ) {
                    for ( int i = 0; i < wandList.size(); i++ ) {
                        if ( !(wandList.get(i).getItem() instanceof SpellTabletItem) ) data.getHandler().insertItem(i, new ItemStack(wandList.get(i).getItem()), false);
                    }
                }
                String spellName = wand.getHoverName().getString();
                if ( !wand.hasCustomHoverName() ) {
                    for ( ItemStack wandListItem : wandList ) {
                        if ( wandListItem.getItem() instanceof SpellRuneItem ) {
                            spellName = new ItemStack(wandListItem.getItem()).getHoverName().getString();
                            break;
                        }
                    }
                }
                spellTablet.setHoverName(new TranslationTextComponent(
                        owner.getScoreboardName() + "'s " + spellName).withStyle(TextFormatting.GREEN));
                spellTablet.getTag().putInt("CustomModelData", 1);
                owner.drop(spellTablet, false);
                owner.stopUsingItem();
                addCastingCooldown(owner, 20);
            }
        }
    }

    public static void addCastingCooldown(PlayerEntity player, int runeCooldown) {
        CASTING_ITEMS.forEach((castingItem) -> player.getCooldowns().addCooldown(castingItem, runeCooldown));
    }

    public static List<ItemStack> getWandList(ItemStack wand) {
        List<ItemStack> wandList = Lists.newArrayList();
        if ( isValidCastingItem(wand) ) {
            WandData data = CastingItem.getData(wand);
            for ( int i = 0; i < data.getHandler().getSlots(); i++ ) {
                ItemStack itemStack = data.getHandler().getStackInSlot(i);
                if ( itemStack.getItem() instanceof RuneItem || itemStack.getItem() instanceof SpellTabletItem ) wandList.add(itemStack);
            }
        }
        return wandList;
    }

    public static List<ItemStack> getStaffList(ItemStack staff) {
        List<ItemStack> tabletList = Lists.newArrayList();
        if ( isValidCastingItem(staff) ) {
            WandData data = CastingItem.getData(staff);
            for ( int i = 0; i < data.getHandler().getSlots(); i++ ) {
                ItemStack tablet = data.getHandler().getStackInSlot(i);
                if ( tablet.getItem() instanceof SpellTabletItem && isValidCastingItem(tablet) ) tabletList.add(tablet);
            }
        }
        return tabletList;
    }

    public static @Nonnull ItemStack getHeldCastingItem(PlayerEntity playerEntity) {
        ItemStack wand = playerEntity.getMainHandItem().getItem() instanceof CastingItem ? playerEntity.getMainHandItem() : null;
        return wand == null ? (playerEntity.getOffhandItem().getItem() instanceof CastingItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : wand;
    }

    public static boolean isValidCastingItem(ItemStack castingItem) {
        return castingItem.hasTag() && castingItem.getTag().contains("UUID") && CastingItem.getData(castingItem).getUuid() != null;
    }

    public static WandData getData(ItemStack stack) {
        if ( !(stack.getItem() instanceof CastingItem) ) return null;
        UUID uuid;
        CompoundNBT tag = stack.getOrCreateTag();
        if ( !tag.contains("UUID") ) {
            uuid = UUID.randomUUID();
            tag.putUUID("UUID", uuid);
        }
        else uuid = tag.getUUID("UUID");
        return WandManager.get().getOrCreateWand(uuid, ((CastingItem)stack.getItem()).tier);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAction getUseAnimation(ItemStack pStack) {
        return UseAction.BOW;
    }

    @Override
    @Nonnull
    public Rarity getRarity(@Nonnull ItemStack stack) {
        return tier.rarity;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new WandCaps(stack);
    }

    static class WandCaps implements ICapabilityProvider {
        private final ItemStack stack;

        public WandCaps(ItemStack stack) {
            this.stack = stack;
        }

        private LazyOptional<IItemHandler> optional = LazyOptional.empty();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if ( cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) {
                if ( !optional.isPresent() ) optional = WandManager.get().getCapability(stack);
                return optional.cast();
            }
            else return LazyOptional.empty();
        }
    }
}

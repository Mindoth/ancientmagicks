package net.mindoth.ancientmagicks.item.spell.abstractspell;

import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.SpellStorageItem;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlEffect;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketItemActivationAnimation;
import net.mindoth.ancientmagicks.network.PacketSendCustomParticles;
import net.mindoth.ancientmagicks.network.PacketUpdateKnownSpells;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SpellItem extends Item {
    public final SpellSchool spellSchool;
    public final int spellTier;
    public final int manaCost;
    public final int cooldown;

    public SpellItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties);
        this.spellSchool = spellSchool;
        this.spellTier = spellTier;
        this.manaCost = manaCost;
        this.cooldown = cooldown * 20;
    }

    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( stack.getItem() instanceof SpellItem spellItem ) {
            if ( !Screen.hasShiftDown() ) {
                String spellSchool = "tooltip.ancientmagicks." + this.spellSchool.toString();
                tooltip.add(Component.translatable("tooltip.ancientmagicks.spell_school").withStyle(ChatFormatting.GRAY).append(Component.literal(": "))
                        .append(Component.translatable(spellSchool)));
                int spellTier = spellItem.spellTier;
                tooltip.add(Component.translatable("tooltip.ancientmagicks.tier").append(Component.literal(": " + spellTier)).withStyle(ChatFormatting.GRAY));
                if ( spellItem.isChannel() ) {
                    int manaCost = spellItem.manaCost * 2;
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.mana_cost").append(Component.literal(": " + manaCost)
                            .append(Component.literal("/s"))).withStyle(ChatFormatting.GRAY));
                }
                else {
                    int manaCost = spellItem.manaCost;
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.mana_cost").append(Component.literal(": " + manaCost)).withStyle(ChatFormatting.GRAY));
                }
                int cooldown = spellItem.cooldown / 20;
                tooltip.add(Component.translatable("tooltip.ancientmagicks.cooldown").append(Component.literal(": " + cooldown + "s")).withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable("tooltip.ancientmagicks.shift"));
            }
            else {
                String modid = ForgeRegistries.ITEMS.getKey(spellItem).toString().split(":")[0];
                if ( modid != null ) tooltip.add(Component.translatable("tooltip." + modid + "." + stack.getItem()).withStyle(ChatFormatting.GRAY));
            }
            if ( ColorRuneItem.CURRENT_COMBO_MAP.containsKey(spellItem) && Minecraft.getInstance().player != null ) {
                StringBuilder tooltipString = new StringBuilder();
                List<ColorRuneItem> list = ColorRuneItem.stringListToActualList(ColorRuneItem.CURRENT_COMBO_MAP.get(this).toString());
                for ( ColorRuneItem rune : list ) {
                    String color = rune.color + "0" + "\u00A7r";
                    tooltipString.append(color);
                }

                tooltip.add(Component.literal(String.valueOf(tooltipString)));
            }
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    public boolean isChannel() {
        return false;
    }

    protected boolean isHarmful() {
        return true;
    }

    protected int getRed() {
        int r = 255;
        if ( this.spellSchool.equals(SpellSchool.FROST) ) r = 85;
        if ( this.spellSchool.equals(SpellSchool.ARCANE) ) r = 170;
        if ( this.spellSchool.equals(SpellSchool.FIRE) ) r = 255;
        if ( this.spellSchool.equals(SpellSchool.NATURE) ) r = 85;
        if ( this.spellSchool.equals(SpellSchool.SHADOW) ) r = 0;
        if ( this.spellSchool.equals(SpellSchool.LIGHT) ) r = 255;
        return r;
    }

    protected int getGreen() {
        int r = 255;
        if ( this.spellSchool.equals(SpellSchool.FROST) ) r = 255;
        if ( this.spellSchool.equals(SpellSchool.ARCANE) ) r = 25;
        if ( this.spellSchool.equals(SpellSchool.FIRE) ) r = 170;
        if ( this.spellSchool.equals(SpellSchool.NATURE) ) r = 255;
        if ( this.spellSchool.equals(SpellSchool.SHADOW) ) r = 0;
        if ( this.spellSchool.equals(SpellSchool.LIGHT) ) r = 255;
        return r;
    }

    protected int getBlue() {
        int r = 255;
        if ( this.spellSchool.equals(SpellSchool.FROST) ) r = 255;
        if ( this.spellSchool.equals(SpellSchool.ARCANE) ) r = 170;
        if ( this.spellSchool.equals(SpellSchool.FIRE) ) r = 25;
        if ( this.spellSchool.equals(SpellSchool.NATURE) ) r = 85;
        if ( this.spellSchool.equals(SpellSchool.SHADOW) ) r = 0;
        if ( this.spellSchool.equals(SpellSchool.LIGHT) ) r = 255;
        return r;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack tablet, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player && tablet.getItem() instanceof SpellItem spellItem) {
            CastingItem.doSpell(player, player, tablet, spellItem, getUseDuration(tablet) - timeLeft);
        }
    }

    protected void addEnchantParticles(Entity target, int r, int g, int b, float size, int age, boolean mask) {
        double var = 0.15D;
        double maxX = target.getBoundingBox().maxX + var;
        double minX = target.getBoundingBox().minX - var;
        double maxZ = target.getBoundingBox().maxZ + var;
        double minZ = target.getBoundingBox().minZ - var;
        double vecX = target.getDeltaMovement().x;
        double vecY = 0.25D;
        double vecZ = target.getDeltaMovement().z;
        for ( int i = 0; i < 4; i++ ) {
            double randX = maxX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, mask,
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, mask,
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, mask,
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = maxZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            AncientMagicksNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(r, g, b, size, age, false, mask,
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
    }

    public static float getPowerInRange(float minPower, float maxPower) {
        Random r = new Random();
        return minPower + r.nextFloat() * (maxPower - minPower);
    }

    public static void attackEntity(LivingEntity owner, LivingEntity target, Entity source, float amount) {
        target.hurt(target.damageSources().indirectMagic(source, owner), amount);
    }

    public static void attackEntityWithoutKnockback(LivingEntity owner, Entity caster, Entity target, float amount) {
        final double vx = target.getDeltaMovement().x;
        final double vy = target.getDeltaMovement().y;
        final double vz = target.getDeltaMovement().z;
        target.hurt(target.damageSources().indirectMagic(caster, owner), amount);
        target.setDeltaMovement(vx, vy, vz);
        target.hurtMarked = true;
    }

    public static boolean isAlly(LivingEntity owner, LivingEntity target) {
        if ( owner == null || target == null ) return false;
        if ( target instanceof Player && !AncientMagicksCommonConfig.PVP.get() ) return true;
        else return target == owner || !owner.canAttack(target) || owner.isAlliedTo(target)
                || (target instanceof TamableAnimal pet && pet.isOwnedBy(owner)) || (target instanceof Mob mob && isMinionsOwner(owner, mob));
    }

    public static boolean isMinionsOwner(LivingEntity owner, Mob mob) {
        return mob.hasEffect(AncientMagicksEffects.MIND_CONTROL.get()) && mob.getPersistentData().hasUUID(MindControlEffect.NBT_KEY)
                && mob.getPersistentData().getUUID(MindControlEffect.NBT_KEY).equals(owner.getUUID()) && mob.getTarget() != owner;
    }

    public static boolean isPushable(Entity entity) {
        return ( entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof PrimedTnt || entity instanceof FallingBlockEntity );
    }

    public static boolean hasLineOfSight(Entity start, Entity target) {
        Vec3 vec3 = new Vec3(start.getX(), start.getEyeY(), start.getZ());
        Vec3 vec31 = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        return start.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, start)).getType() == HitResult.Type.MISS;
    }

    /*@Override
    public void onDestroyed(ItemEntity entity, DamageSource damageSource) {
        Level level = entity.level();
        if ( level.isClientSide ) return;
        if ( damageSource.type() != entity.damageSources().lava().type() && damageSource.type() != entity.damageSources().onFire().type() ) return;
        SpellItem spellItem = (SpellItem)entity.getItem().getItem();
        int amount = ThreadLocalRandom.current().nextInt(1, spellItem.spellTier + 1);
        float randX = ThreadLocalRandom.current().nextFloat(-0.3F, 0.3F);
        float randZ = ThreadLocalRandom.current().nextFloat(-0.3F, 0.3F);
        ItemStack newStack = new ItemStack(AncientMagicksItems.ARCANE_DUST.get(), amount);
        ItemEntity drop = new ItemEntity(level, entity.position().x, entity.position().y, entity.position().z, newStack);
        drop.setDeltaMovement(randX, 0.3F, randZ);
        drop.setPickUpDelay(40);
        level.addFreshEntity(drop);
    }*/

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack tablet = player.getItemInHand(handIn);
            if ( tablet.getItem() instanceof SpellItem spellTablet && !player.isUsingItem() && !player.getCooldowns().isOnCooldown(spellTablet) ) {
                if ( player instanceof ServerPlayer serverPlayer && tablet.getItem() instanceof SpellItem) learnSpell(serverPlayer, tablet);
            }
        }
        return result;
    }

    public static void learnSpell(ServerPlayer serverPlayer, ItemStack stack) {
        SpellItem stackItem = null;
        if ( stack.getItem() instanceof SpellItem ) stackItem = (SpellItem)stack.getItem();
        else if ( stack.getItem() instanceof SpellStorageItem ) {
            if ( stack.getTag() != null && stack.getTag().contains(SpellStorageItem.TAG_STORED_SPELL) ) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(stack.getTag().getString(SpellStorageItem.TAG_STORED_SPELL)));
                if ( item instanceof SpellItem spell ) stackItem = spell;
            }
        }
        final SpellItem spellTablet = stackItem;
        if ( spellTablet == null ) return;
        final String spellString = ForgeRegistries.ITEMS.getKey(spellTablet).toString();
        serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(spell -> {
            CompoundTag tag = new CompoundTag();
            tag.putString("am_secretspell", spellString);
            if ( Objects.equals(spell.getKnownSpells(), "") ) {
                spell.setKnownSpells(spellString);
                AncientMagicksNetwork.sendToPlayer(new PacketUpdateKnownSpells(tag), serverPlayer);
                playLearnEffects(serverPlayer, stack);
            }
            else if ( !ClientMagicData.stringListToSpellList(spell.getKnownSpells()).contains(spellTablet) ) {
                spell.setKnownSpells(spell.getKnownSpells() + "," + spellString);
                AncientMagicksNetwork.sendToPlayer(new PacketUpdateKnownSpells(tag), serverPlayer);
                playLearnEffects(serverPlayer, stack);
            }
        });
    }

    public static void playLearnEffects(Player player, ItemStack stack) {
        player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 0.75F);
        player.playNotifySound(SoundEvents.FIREWORK_ROCKET_BLAST_FAR, SoundSource.PLAYERS, 1.0F, 0.75F);
        player.playNotifySound(SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR, SoundSource.PLAYERS, 1.0F, 0.75F);
        if ( player instanceof ServerPlayer ) AncientMagicksNetwork.sendToPlayer(new PacketItemActivationAnimation(stack, player), (ServerPlayer)player);
        stack.shrink(1);
    }

    public static void playItemActivationAnimation(ItemStack itemStack, Entity entity) {
        Minecraft mc = Minecraft.getInstance();
        if ( entity == mc.player ) mc.gameRenderer.displayItemActivation(itemStack);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    protected void playSound(Level level, Vec3 center) {
        playMagicSound(level, center);
    }

    public static void playWhiffSound(Entity caster) {
        if ( caster instanceof Player player ) player.playNotifySound(SoundEvents.NOTE_BLOCK_SNARE.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
    }

    public static void playMagicSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.25F, 2.0F);
    }

    public static void playMagicShootSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.25F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.25F, 2.0F);
    }

    public static void playMagicSummonSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.25F, 2.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.25F, 2.0F);
    }

    public static void playFireShootSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.25F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.25F, 1.0F);
    }

    public static void playWindSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundSource.PLAYERS, 2.0F, 0.02F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundSource.PLAYERS, 2.0F, 0.03F);
    }

    public static void playStormShootSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.35F, 2.0F);
    }

    public static void playTeleportSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    public static void playXpSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.25F, (new Random().nextFloat() - new Random().nextFloat()) * 0.35F + 0.9F);
    }
}

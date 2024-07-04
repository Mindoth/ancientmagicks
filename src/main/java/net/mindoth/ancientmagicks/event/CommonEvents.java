package net.mindoth.ancientmagicks.event;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientSpell;
import net.mindoth.ancientmagicks.network.PacketSyncSpellCombos;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        if ( event.getEntity() instanceof ServerPlayer player ) {
            AncientMagicksNetwork.sendToPlayer(new PacketSyncSpellCombos(ColorRuneItem.CURRENT_COMBO_TAG), player);
            player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                CompoundTag tag = new CompoundTag();
                if ( spell.getSpell() != null ) tag.putString("am_spell", spell.getSpell());
                else spell.setSpell("minecraft:air");
                AncientMagicksNetwork.sendToPlayer(new PacketSyncClientSpell(tag), player);
            });
        }
    }

    @SubscribeEvent
    public static void hideWithGhostwalk(LivingEvent.LivingVisibilityEvent event) {
        if ( event.getEntity().hasEffect(AncientMagicksEffects.GHOSTWALK.get()) ) event.modifyVisibility(0);
    }

    @SubscribeEvent
    public static void onLivingFallSpook(LivingFallEvent event) {
        LivingEntity living = event.getEntity();
        if ( living.hasEffect(AncientMagicksEffects.SPOOK.get()) ) {
            if ( calculateFallDamage(living, event.getDistance(), event.getDamageMultiplier()) < living.getHealth() ) {
                event.setCanceled(true);
            }
        }
    }

    //This is here to check if falldamage is lethal in onLivingFallSpook
    protected static int calculateFallDamage(LivingEntity living, float pFallDistance, float pDamageMultiplier) {
        if ( living.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE) ) return 0;
        else {
            MobEffectInstance mobeffectinstance = living.getEffect(MobEffects.JUMP);
            float f = mobeffectinstance == null ? 0.0F : (float)(mobeffectinstance.getAmplifier() + 1);
            return Mth.ceil((pFallDistance - 3.0F - f) * pDamageMultiplier);
        }
    }

    public static Vec3 getBlockPoint(Entity caster, float range, boolean isPlayer) {
        int adjuster = 1;
        if ( !isPlayer ) adjuster = -1;

        Vec3 direction = ShadowEvents.calculateViewVector(caster.getXRot() * (float)adjuster, caster.getYRot() * (float)adjuster).normalize();
        direction = direction.multiply(range, range, range);
        Vec3 center = caster.getEyePosition(1.0F).add(direction);
        Vec3 returnPoint = center;
        double playerX = ShadowEvents.getEntityCenter(caster).x;
        double playerY = caster.getEyePosition(1.0F).y;
        double playerZ = ShadowEvents.getEntityCenter(caster).z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));

        for ( int k = 1; k < 1 + particleInterval; ++k ) {
            double lineX = playerX * (1.0 - (double)k / (double)particleInterval) + listedEntityX * ((double)k / (double)particleInterval);
            double lineY = playerY * (1.0 - (double)k / (double)particleInterval) + listedEntityY * ((double)k / (double)particleInterval);
            double lineZ = playerZ * (1.0 - (double)k / (double)particleInterval) + listedEntityZ * ((double)k / (double)particleInterval);

            returnPoint = new Vec3(lineX, lineY, lineZ);
            BlockPos blockPos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
            if ( caster.level().getBlockState(blockPos).isSolid() ) break;
        }
        return returnPoint;
    }

    @SubscribeEvent
    public static void disableInteraction(final PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if ( !CastingItem.getHeldCastingItem(player).isEmpty() || !CastingItem.getHeldTabletItem(player).isEmpty() ) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onStopChannellingSpell(final LivingEntityUseItemEvent.Stop event) {
        if ( event.getEntity() instanceof Player player ) {
            if ( !player.level().isClientSide ) {
                if ( event.getItem().getItem() instanceof CastingItem ) {
                    player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spell.getSpell()));
                        if ( item instanceof TabletItem tabletItem ) {
                            player.getCooldowns().addCooldown(tabletItem, tabletItem.cooldown);
                        }
                    });
                }
                else if ( event.getItem().getItem() instanceof TabletItem tabletItem ) {
                    player.getCooldowns().addCooldown(tabletItem, tabletItem.cooldown);
                }
            }
        }
    }

    public static void summonParticleLine(SimpleParticleType type, Entity caster, Entity target, int count, double xOff, double yOff, double zOff, double speed) {
        double playerX = ShadowEvents.getEntityCenter(caster).x;
        double playerY = ShadowEvents.getEntityCenter(caster).y;
        double playerZ = ShadowEvents.getEntityCenter(caster).z;
        double listedEntityX = target.getEyePosition().x;
        double listedEntityY = target.getEyePosition().y;
        double listedEntityZ = target.getEyePosition().z;
        int particleInterval = (int)Math.round(caster.distanceToSqr(target.position()));
        ServerLevel level = (ServerLevel)caster.level();
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            level.sendParticles(type, lineX, lineY, lineZ, count, xOff, yOff, zOff, speed);
        }
    }
}

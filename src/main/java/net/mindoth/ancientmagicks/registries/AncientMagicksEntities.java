package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.form.SpellProjectileEntity;
import net.mindoth.ancientmagicks.item.temp.abstractspell.spellpearl.SpellPearlEntity;
import net.mindoth.ancientmagicks.item.temp.acidarrow.AcidArrowEntity;
import net.mindoth.ancientmagicks.item.temp.blizzard.IcicleEntity;
import net.mindoth.ancientmagicks.item.temp.burnlance.BurnLanceEntity;
import net.mindoth.ancientmagicks.item.temp.fireball.FireballEntity;
import net.mindoth.ancientmagicks.item.temp.firebolt.FireBoltEntity;
import net.mindoth.ancientmagicks.item.temp.freezelance.FreezeLanceEntity;
import net.mindoth.ancientmagicks.item.temp.waterbolt.WaterBoltEntity;
import net.mindoth.ancientmagicks.item.temp.witcharrow.WitchArrowEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AncientMagicks.MOD_ID);

    public static final RegistryObject<EntityType<SpellProjectileEntity>> SPELL_PROJECTILE
            = registerEntity(EntityType.Builder.<SpellProjectileEntity>of(SpellProjectileEntity::new,
            MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(SpellProjectileEntity::new), "spell_projectile");

    public static final RegistryObject<EntityType<WitchArrowEntity>> WITCH_ARROW
            = registerEntity(EntityType.Builder.<WitchArrowEntity>of(WitchArrowEntity::new,
            MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(WitchArrowEntity::new), "witch_arrow");

    public static final RegistryObject<EntityType<FireballEntity>> FIREBALL
            = registerEntity(EntityType.Builder.<FireballEntity>of(FireballEntity::new,
            MobCategory.MISC).sized(0.8F, 0.8F).setCustomClientFactory(FireballEntity::new), "fireball");

    public static final RegistryObject<EntityType<WaterBoltEntity>> WATER_BOLT
            = registerEntity(EntityType.Builder.<WaterBoltEntity>of(WaterBoltEntity::new,
            MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(WaterBoltEntity::new), "water_bolt");

    public static final RegistryObject<EntityType<SpellPearlEntity>> SPELL_PEARL
            = registerEntity(EntityType.Builder.<SpellPearlEntity>of(SpellPearlEntity::new,
            MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(SpellPearlEntity::new), "spell_pearl");

    public static final RegistryObject<EntityType<IcicleEntity>> ICICLE
            = registerEntity(EntityType.Builder.<IcicleEntity>of(IcicleEntity::new,
            MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(IcicleEntity::new), "icicle");

    public static final RegistryObject<EntityType<FreezeLanceEntity>> FREEZE_LANCE
            = registerEntity(EntityType.Builder.<FreezeLanceEntity>of(FreezeLanceEntity::new,
            MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(FreezeLanceEntity::new), "freeze_lance");

    public static final RegistryObject<EntityType<BurnLanceEntity>> BURN_LANCE
            = registerEntity(EntityType.Builder.<BurnLanceEntity>of(BurnLanceEntity::new,
            MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(BurnLanceEntity::new), "burn_lance");

    public static final RegistryObject<EntityType<FireBoltEntity>> FIRE_BOLT
            = registerEntity(EntityType.Builder.<FireBoltEntity>of(FireBoltEntity::new,
            MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(FireBoltEntity::new), "fire_bolt");

    public static final RegistryObject<EntityType<AcidArrowEntity>> ACID_ARROW
            = registerEntity(EntityType.Builder.<AcidArrowEntity>of(AcidArrowEntity::new,
            MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(AcidArrowEntity::new), "acid_arrow");


    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }
}

package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.acidarrow.AcidArrowEntity;
import net.mindoth.ancientmagicks.item.spell.burnlance.BurnLanceEntity;
import net.mindoth.ancientmagicks.item.spell.fireball.FireballEntity;
import net.mindoth.ancientmagicks.item.spell.firebolt.FireBoltEntity;
import net.mindoth.ancientmagicks.item.spell.freezelance.FreezeLanceEntity;
import net.mindoth.ancientmagicks.item.spell.icicle.IcicleEntity;
import net.mindoth.ancientmagicks.item.spell.slimeball.SlimeballEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.spellpearl.SpellPearlEntity;
import net.mindoth.ancientmagicks.item.spell.thunderball.ThunderballEntity;
import net.mindoth.ancientmagicks.item.spell.witcharrow.WitchArrowEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AncientMagicks.MOD_ID);

    public static final RegistryObject<EntityType<WitchArrowEntity>> WITCH_ARROW
            = registerEntity(EntityType.Builder.<WitchArrowEntity>of(WitchArrowEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(WitchArrowEntity::new), "witch_arrow");

    public static final RegistryObject<EntityType<FireballEntity>> FIREBALL
            = registerEntity(EntityType.Builder.<FireballEntity>of(FireballEntity::new,
            MobCategory.MISC).sized(0.8F, 0.8F).setCustomClientFactory(FireballEntity::new), "fireball");

    public static final RegistryObject<EntityType<SlimeballEntity>> SLIMEBALL
            = registerEntity(EntityType.Builder.<SlimeballEntity>of(SlimeballEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(SlimeballEntity::new), "slimeball");

    public static final RegistryObject<EntityType<SpellPearlEntity>> SPELL_PEARL
            = registerEntity(EntityType.Builder.<SpellPearlEntity>of(SpellPearlEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(SpellPearlEntity::new), "spell_pearl");

    public static final RegistryObject<EntityType<IcicleEntity>> ICICLE
            = registerEntity(EntityType.Builder.<IcicleEntity>of(IcicleEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(IcicleEntity::new), "icicle");

    public static final RegistryObject<EntityType<FreezeLanceEntity>> FREEZE_LANCE
            = registerEntity(EntityType.Builder.<FreezeLanceEntity>of(FreezeLanceEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(FreezeLanceEntity::new), "freeze_lance");

    public static final RegistryObject<EntityType<BurnLanceEntity>> BURN_LANCE
            = registerEntity(EntityType.Builder.<BurnLanceEntity>of(BurnLanceEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(BurnLanceEntity::new), "burn_lance");

    public static final RegistryObject<EntityType<ThunderballEntity>> THUNDERBALL
            = registerEntity(EntityType.Builder.<ThunderballEntity>of(ThunderballEntity::new,
            MobCategory.MISC).sized(0.8F, 0.8F).setCustomClientFactory(ThunderballEntity::new), "thunderball");

    public static final RegistryObject<EntityType<FireBoltEntity>> FIRE_BOLT
            = registerEntity(EntityType.Builder.<FireBoltEntity>of(FireBoltEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(FireBoltEntity::new), "fire_bolt");

    public static final RegistryObject<EntityType<AcidArrowEntity>> ACID_ARROW
            = registerEntity(EntityType.Builder.<AcidArrowEntity>of(AcidArrowEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(AcidArrowEntity::new), "acid_arrow");


    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }
}

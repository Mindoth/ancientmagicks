package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.dynamite.DynamiteEntity;
import net.mindoth.ancientmagicks.item.spell.experiencestream.ExperienceStreamEntity;
import net.mindoth.ancientmagicks.item.spell.fireball.FireballEntity;
import net.mindoth.ancientmagicks.item.spell.icicle.IcicleEntity;
import net.mindoth.ancientmagicks.item.spell.slimeball.SlimeballEntity;
import net.mindoth.ancientmagicks.item.spell.spellpearl.SpellPearlEntity;
import net.mindoth.ancientmagicks.item.spell.witchspark.WitchSparkEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AncientMagicks.MOD_ID);

    public static final RegistryObject<EntityType<WitchSparkEntity>> WITCH_SPARK
            = registerEntity(EntityType.Builder.<WitchSparkEntity>of(WitchSparkEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(WitchSparkEntity::new), "witch_spark");

    public static final RegistryObject<EntityType<FireballEntity>> FIREBALL
            = registerEntity(EntityType.Builder.<FireballEntity>of(FireballEntity::new,
            MobCategory.MISC).sized(0.8F, 0.8F).setCustomClientFactory(FireballEntity::new), "fireball");

    public static final RegistryObject<EntityType<DynamiteEntity>> DYNAMITE
            = registerEntity(EntityType.Builder.<DynamiteEntity>of(DynamiteEntity::new,
            MobCategory.MISC).sized(1.0F, 1.0F).setCustomClientFactory(DynamiteEntity::new), "dynamite");

    public static final RegistryObject<EntityType<SlimeballEntity>> SLIMEBALL
            = registerEntity(EntityType.Builder.<SlimeballEntity>of(SlimeballEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(SlimeballEntity::new), "slimeball");

    //This might need to be 0.5F, 0.5F sized
    public static final RegistryObject<EntityType<ExperienceStreamEntity>> EXPERIENCE_BEAM
            = registerEntity(EntityType.Builder.<ExperienceStreamEntity>of(ExperienceStreamEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(ExperienceStreamEntity::new), "experience_beam");

    public static final RegistryObject<EntityType<SpellPearlEntity>> SPELL_PEARL
            = registerEntity(EntityType.Builder.<SpellPearlEntity>of(SpellPearlEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(SpellPearlEntity::new), "spell_pearl");

    public static final RegistryObject<EntityType<IcicleEntity>> ICICLE
            = registerEntity(EntityType.Builder.<IcicleEntity>of(IcicleEntity::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(IcicleEntity::new), "icicle");


    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }
}

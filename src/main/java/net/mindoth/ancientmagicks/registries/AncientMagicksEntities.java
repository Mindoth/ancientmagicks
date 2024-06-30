package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.deafeningblast.DeafeningBlastEntity;
import net.mindoth.ancientmagicks.item.spell.dynamite.DynamiteEntity;
import net.mindoth.ancientmagicks.item.spell.experiencebeam.ExperienceStreamEntity;
import net.mindoth.ancientmagicks.item.spell.fireball.FireballEntity;
import net.mindoth.ancientmagicks.item.spell.raisedead.SkeletonMinionEntity;
import net.mindoth.ancientmagicks.item.spell.slimeball.SlimeballEntity;
import net.mindoth.ancientmagicks.item.spell.witchspark.WitchSparkEntity;
import net.minecraft.resources.ResourceLocation;
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

    public static final RegistryObject<EntityType<DeafeningBlastEntity>> DEAFENING_BLAST
            = registerEntity(EntityType.Builder.<DeafeningBlastEntity>of(DeafeningBlastEntity::new,
            MobCategory.MISC).sized(1.0F, 0.4F).setCustomClientFactory(DeafeningBlastEntity::new), "deafening_blast");

    public static final RegistryObject<EntityType<ExperienceStreamEntity>> EXPERIENCE_BEAM
            = registerEntity(EntityType.Builder.<ExperienceStreamEntity>of(ExperienceStreamEntity::new,
            MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(ExperienceStreamEntity::new), "experience_beam");


    public static final RegistryObject<EntityType<SkeletonMinionEntity>> SKELETON_MINION =
            ENTITIES.register("skeleton_minion", () -> EntityType.Builder.<SkeletonMinionEntity>of(SkeletonMinionEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(AncientMagicks.MOD_ID, "skeleton_minion").toString()));


    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }
}

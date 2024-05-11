package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spellrune.blackhole.BlackHoleEntity;
import net.mindoth.ancientmagicks.item.spellrune.dynamite.DynamiteEntity;
import net.mindoth.ancientmagicks.item.spellrune.enderbolt.EnderBoltEntity;
import net.mindoth.ancientmagicks.item.spellrune.fireball.FireballEntity;
import net.mindoth.ancientmagicks.item.spellrune.raisedead.SkeletonMinionEntity;
import net.mindoth.ancientmagicks.item.spellrune.slimeball.SlimeballEntity;
import net.mindoth.ancientmagicks.item.spellrune.tornado.TornadoEntity;
import net.mindoth.ancientmagicks.item.spellrune.witchspark.WitchSparkEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AncientMagicksEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, AncientMagicks.MOD_ID);

    public static final RegistryObject<EntityType<WitchSparkEntity>> WITCH_SPARK
            = registerEntity(EntityType.Builder.<WitchSparkEntity>of(WitchSparkEntity::new,
            EntityClassification.MISC).sized(0.4F, 0.4F).setCustomClientFactory(WitchSparkEntity::new), "witch_spark");

    public static final RegistryObject<EntityType<FireballEntity>> FIREBALL
            = registerEntity(EntityType.Builder.<FireballEntity>of(FireballEntity::new,
            EntityClassification.MISC).sized(0.8F, 0.8F).setCustomClientFactory(FireballEntity::new), "fireball");

    public static final RegistryObject<EntityType<DynamiteEntity>> DYNAMITE
            = registerEntity(EntityType.Builder.<DynamiteEntity>of(DynamiteEntity::new,
            EntityClassification.MISC).sized(1.0F, 1.0F).setCustomClientFactory(DynamiteEntity::new), "dynamite");

    public static final RegistryObject<EntityType<EnderBoltEntity>> ENDER_BOLT
            = registerEntity(EntityType.Builder.<EnderBoltEntity>of(EnderBoltEntity::new,
            EntityClassification.MISC).sized(0.4F, 0.4F).setCustomClientFactory(EnderBoltEntity::new), "ender_bolt");

    public static final RegistryObject<EntityType<BlackHoleEntity>> BLACK_HOLE
            = registerEntity(EntityType.Builder.<BlackHoleEntity>of(BlackHoleEntity::new,
            EntityClassification.MISC).sized(1.0F, 1.0F).setCustomClientFactory(BlackHoleEntity::new), "black_hole");

    public static final RegistryObject<EntityType<SlimeballEntity>> SLIMEBALL
            = registerEntity(EntityType.Builder.<SlimeballEntity>of(SlimeballEntity::new,
            EntityClassification.MISC).sized(0.4F, 0.4F).setCustomClientFactory(SlimeballEntity::new), "slimeball");


    public static final RegistryObject<EntityType<SkeletonMinionEntity>> SKELETON_MINION =
            ENTITIES.register("skeleton_minion", () -> EntityType.Builder.<SkeletonMinionEntity>of(SkeletonMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(AncientMagicks.MOD_ID, "skeleton_minion").toString()));


    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }
}

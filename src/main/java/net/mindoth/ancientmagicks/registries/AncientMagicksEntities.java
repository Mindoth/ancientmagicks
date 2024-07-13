package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.entity.projectile.WitchSpark;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AncientMagicks.MOD_ID);

    public static final RegistryObject<EntityType<WitchSpark>> WITCH_SPARK
            = registerEntity(EntityType.Builder.<WitchSpark>of(WitchSpark::new,
            MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(WitchSpark::new), "witch_spark");

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }
}

package net.mindoth.ancientmagicks.registries.attribute;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, AncientMagicks.MOD_ID);

    public static final RegistryObject<Attribute> MAGICK = ATTRIBUTES.register("magick_max",
            () -> (new MagickAttribute("attribute.ancientmagicks.magick", 20.0D, 0.0D, Integer.MAX_VALUE).setSyncable(true)));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> event.add(entity, attribute.get())));
    }
}

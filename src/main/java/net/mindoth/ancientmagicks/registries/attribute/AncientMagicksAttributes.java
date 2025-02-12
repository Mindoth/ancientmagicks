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
public class AncientMagicksAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, AncientMagicks.MOD_ID);

    public static final RegistryObject<Attribute> MP_MAX = ATTRIBUTES.register("magick_max",
            () -> (new MagicAttribute("attribute.ancientmagicks.magick_max", 100.0D, 0.0D, Integer.MAX_VALUE).setSyncable(true)));

    public static final RegistryObject<Attribute> MP_REG = ATTRIBUTES.register("magick_regen",
            () -> (new MagicAttribute("attribute.ancientmagicks.magick_regen", 1.0D, 0.0D, Integer.MAX_VALUE).setSyncable(true)));

    public static final RegistryObject<Attribute> SPELL_POWER = ATTRIBUTES.register("magick_power",
            () -> (new MagicAttribute("attribute.ancientmagicks.magick_power", 0.0D, Integer.MIN_VALUE, Integer.MAX_VALUE).setSyncable(true)));

    public static final RegistryObject<Attribute> CAST_TIER = ATTRIBUTES.register("magick_tier",
            () -> (new MagicAttribute("attribute.ancientmagicks.magick_tier", 0.0D, 0.0D, Integer.MAX_VALUE).setSyncable(true)));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> event.add(entity, attribute.get())));
    }
}

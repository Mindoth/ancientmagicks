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

    public static final RegistryObject<Attribute> SPELL_POWER = ATTRIBUTES.register("spell_power",
            () -> (new MagicAttribute("attribute.ancientmagicks.spell_power", 1.0D, -100, 100.0D).setSyncable(true)));

    public static final RegistryObject<Attribute> MANA_REGENERATION = ATTRIBUTES.register("mana_regeneration",
            () -> (new MagicAttribute("attribute.ancientmagicks.mana_regen", 1.0D, 0.0D, 100.0D).setSyncable(true)));

    public static final RegistryObject<Attribute> MANA_MAXIMUM = ATTRIBUTES.register("mana_maximum",
            () -> (new MagicAttribute("attribute.ancientmagicks.max_mana", 100.0D, 0.0D, 10000.0D).setSyncable(true)));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> event.add(entity, attribute.get())));
    }
}

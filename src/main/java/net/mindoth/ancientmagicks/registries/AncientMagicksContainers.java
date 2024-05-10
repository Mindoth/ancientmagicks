package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.gui.inventory.WandContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AncientMagicksContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, AncientMagicks.MOD_ID);
    public static final RegistryObject<ContainerType<WandContainer>> WAND_CONTAINER = CONTAINERS.register("wand_container", () -> IForgeContainerType.create(WandContainer::fromNetwork));
}

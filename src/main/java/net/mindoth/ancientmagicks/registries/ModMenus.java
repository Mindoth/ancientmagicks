package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.gui.menu.RuneCraftingMenu;
import net.mindoth.ancientmagicks.client.gui.menu.SpellCraftingMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, AncientMagicks.MOD_ID);

    public static final RegistryObject<MenuType<SpellCraftingMenu>> SPELL_CRAFTING_MENU = registerMenuType("spell_crafting_menu", SpellCraftingMenu::new);
    public static final RegistryObject<MenuType<RuneCraftingMenu>> RUNE_CRAFTING_MENU = registerMenuType("rune_crafting_menu", RuneCraftingMenu::new);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
}

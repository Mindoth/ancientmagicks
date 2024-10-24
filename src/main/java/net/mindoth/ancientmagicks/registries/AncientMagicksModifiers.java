package net.mindoth.ancientmagicks.registries;

import com.mojang.serialization.Codec;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.loot.AddItemModifier;
import net.mindoth.ancientmagicks.loot.RandomizeSpellFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, AncientMagicks.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", AddItemModifier.CODEC);



    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, AncientMagicks.MOD_ID);

    public static final RegistryObject<LootItemFunctionType> RANDOMIZE_SPELL_FUNCTION = LOOT_FUNCTIONS.register("randomize_spell",
            () -> new LootItemFunctionType(new RandomizeSpellFunction.Serializer()));
}

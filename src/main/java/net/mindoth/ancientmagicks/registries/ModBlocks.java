package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.block.SpellCraftingTableBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AncientMagicks.MOD_ID);

    public static final RegistryObject<Block> BREEZE_ROCK = registerBlock("breeze_rock",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> BREEZE_ROCK_STAIRS = registerBlock("breeze_rock_stairs",
            () -> new StairBlock(() -> ModBlocks.BREEZE_ROCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> BREEZE_ROCK_SLAB = registerBlock("breeze_rock_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));


    public static final RegistryObject<Block> BREEZE_BRICKS = registerBlock("breeze_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> BREEZE_BRICKS_STAIRS = registerBlock("breeze_brick_stairs",
            () -> new StairBlock(() -> ModBlocks.BREEZE_ROCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> BREEZE_BRICKS_SLAB = registerBlock("breeze_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));


    public static final RegistryObject<Block> SPELL_CRAFTING_TABLE = registerBlock("spell_crafting_table",
            () -> new SpellCraftingTableBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
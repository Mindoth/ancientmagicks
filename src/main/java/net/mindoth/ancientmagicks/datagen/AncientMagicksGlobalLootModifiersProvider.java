package net.mindoth.ancientmagicks.datagen;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.loot.AddItemModifier;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import org.apache.commons.lang3.Validate;

public class AncientMagicksGlobalLootModifiersProvider extends GlobalLootModifierProvider {

    public AncientMagicksGlobalLootModifiersProvider(PackOutput output) {
        super(output, AncientMagicks.MOD_ID);
    }

    @Override
    protected void start() {
        /*add("ancient_loot", new AddItemModifier(new LootItemCondition[] {
                getList(new String[] {
                        "abandoned_mineshaft", "simple_dungeon", "igloo_chest", "pillager_outpost",
                        "shipwreck_supply", "shipwreck_treasure",
                }, "chests/"), LootItemRandomChanceCondition.randomChance(0.50F).build()
        }, AncientMagicksItems.ANCIENT_TABLET.get()));*/
        add("ancient_loot", new AddItemModifier(new LootItemCondition[] {
                getList(new String[] {
                        "jungle_temple", "desert_pyramid", "underwater_ruin_big", "underwater_ruin_small",
                        "bastion_treasure", "bastion_bridge", "bastion_other",  "nether_bridge",
                        "stronghold_corridor", "stronghold_crossing", "stronghold_library",
                        "end_city_treasure", "ancient_city"
                }, "chests/"), LootItemRandomChanceCondition.randomChance(0.75F).build()
        }, AncientMagicksItems.ANCIENT_TABLET.get()));
        add("arcane_dust_drop", new AddItemModifier(new LootItemCondition[] {
                getList(new String[] {
                        "witch", "evoker", "illusioner"
                }, "entities/"), LootItemRandomChanceCondition.randomChance(1.0F).build()
        }, AncientMagicksItems.ARCANE_DUST.get()));
    }

    private LootItemCondition getList(String[] tables, String dest) {
        Validate.isTrue(tables.length > 0);
        LootItemCondition.Builder condition = null;
        for ( String s : tables ) {
            LootTableIdCondition.Builder builder = new LootTableIdCondition.Builder(new ResourceLocation(dest + s));
            condition = condition == null ? builder : condition.or(builder);
        }
        return condition.build();
    }
}

package net.mindoth.ancientmagicks.datagen;

import net.mindoth.ancientmagicks.AncientMagicks;
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
        add("dungeon_loot", new AddItemModifier(new LootItemCondition[] {
                getList(new String[] {
                        "simple_dungeon", "jungle_temple", "abandoned_mineshaft",
                        "bastion_treasure", "bastion_bridge", "bastion_hoglin_stable", "bastion_other",
                        "desert_pyramid", "end_city_treasure", "nether_bridge",
                        "stronghold_corridor", "stronghold_crossing", "stronghold_library",
                        "woodland_mansion", "underwater_ruin_big", "underwater_ruin_small", "shipwreck_treasure"
                }), LootItemRandomChanceCondition.randomChance(0.75F).build()
        }, AncientMagicksItems.ANCIENT_TABLET.get()));
    }

    private LootItemCondition getList(String[] chests) {
        Validate.isTrue(chests.length > 0);
        LootItemCondition.Builder condition = null;
        for ( String s : chests ) {
            LootTableIdCondition.Builder builder = new LootTableIdCondition.Builder(new ResourceLocation("chests/" + s));
            condition = condition == null ? builder : condition.or(builder);
        }
        return condition.build();
    }

    /*@Override
    protected void start() {
        add("tablet_from_simple_dungeon", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(1.0F).build()
        }, AncientMagicksItems.ANCIENT_TABLET.get()));
    }*/
}

package net.mindoth.ancientmagicks.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.AncientTabletItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksModifiers;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.List;
import java.util.Random;

public class RandomizeSpellFunction extends LootItemConditionalFunction {
    final NumberProvider qualityRange;

    protected RandomizeSpellFunction(LootItemCondition[] pPredicates, NumberProvider qualityRange) {
        super(pPredicates);
        this.qualityRange = qualityRange;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if ( stack.getItem() instanceof AncientTabletItem ) {
            int quality = this.qualityRange.getInt(context);
            List<SpellItem> list = AncientMagicks.SPELL_LIST.stream().filter(spell -> spell.spellTier == quality).toList();
            System.out.println(list);
            if ( !list.isEmpty() ) {
                SpellItem randomSpell = list.get(new Random().nextInt(list.size()));
                stack = new ItemStack(randomSpell);
            }
        }
        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return AncientMagicksModifiers.RANDOMIZE_SPELL_FUNCTION.get();
    }

    @SuppressWarnings("unchecked")
    public <N extends NumberProvider> N getQualityRange() {
        return (N) qualityRange;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RandomizeSpellFunction> {
        public void serialize(JsonObject json, RandomizeSpellFunction tabletFunction, JsonSerializationContext jsonDeserializationContext) {
            super.serialize(json, tabletFunction, jsonDeserializationContext);
            JsonObject quality = new JsonObject();
            tabletFunction.qualityRange.getType().getSerializer().serialize(quality, tabletFunction.getQualityRange(), jsonDeserializationContext);
            json.add("quality", quality);
        }

        public RandomizeSpellFunction deserialize(JsonObject json, JsonDeserializationContext jsonDeserializationContext, LootItemCondition[] lootConditions) {
            NumberProvider numberProvider = GsonHelper.getAsObject(json, "quality", jsonDeserializationContext, NumberProvider.class);

            return new RandomizeSpellFunction(lootConditions, numberProvider);
        }
    }
}
